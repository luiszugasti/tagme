package ru.eb02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.NoSuchElementException;
import org.apache.commons.lang.NullArgumentException;

/**
 *
 * The trecTopic class is a record of a single trecTopic as provided by the search results file.
 * It holds <strong>all</strong> entries for a query - and may get quite large.
 * We truly only care about reorganizing the top 200 docs.
 *
 */
public class trecTopic {
  private final int queryNum;
  private ArrayList<Tuple> docRankingsScores;
  private final StringBuilder remainingDocs;
  private int size;
  private double maxValue;
  private double minValue;

  public trecTopic(int queryNum) {
    this.queryNum = queryNum;
    this.remainingDocs = new StringBuilder();
    docRankingsScores = new ArrayList<>();
    size = 0;
  }

  /**
   * When running this class; the order HAS to be in form of decreasing score for docs.
   * So, ensure that when using this class!
   * If you want to reorder, maybe run updateScore with an empty hashmap? but then why is that
   * effective?
   * @param documentName the name of the document in TREC format
   * @param score the score assigned, by either original scoring or after centrality
   */
  public void addDocument(String documentName, double score) {
    // Be weary of adding duplicate docs; this does not check for duplicates!
    if (documentName == null) {
      throw new NullArgumentException("documentName is null.");
    }
    // Oh god. Code smell.
    if (this.docRankingsScores.size() < 200)
      docRankingsScores.add(new Tuple(documentName, score));
    else
      remainingDocs.append(queryNum)
                   .append(" Q0 ")
                   .append(documentName)
                   .append(" ")
                   .append(score)
                   .append(" ")
                   .append(Integer.valueOf(size + 1))
                   .append(" Default\n");
    size ++;
  }

  /**
   * Gets the rank of a chosen doc.
   * @param docName the name of the document to inquire its rank.
   * @return the int representation of this document's rank.
   */
  public int getRank (String docName) {
    int i = 0;
    for (Tuple sample : docRankingsScores) {
      i++;
      if (docName.equals(sample.getKey())) return i;
    }
    throw new NoSuchElementException("The provided docName is not in this collection: "
    + docName);
  }

  /**
   * First add all the values from the provided hash map to the tuple array,
   * multiplied by the provided lambda values.
   * This will take n time.
   * Then, sort the tuples according to the document score.
   * As a simple method I'm using Bubble Sort.
   * @param lambda1 value of lambda for weighing purposes.
   * @param updatedScores the updated scores object.
   * @return the newly updatedRanks in the top 200 docs.
   */
  public ArrayList<Tuple> updateRanks (HashMap<String, Double> updatedScores, double lambda1) {
    // New array list.
    ArrayList<Tuple> updatedRanks = new ArrayList<>();
    // Update max and min of the top 200 docs.
    computeMaxMin();
    // Go thru the ArrayList of tuples and update their values.
    for (Tuple entry : docRankingsScores) {
      if (updatedScores.get(entry.getKey()) == null)
        updatedRanks.add(new Tuple(entry.getKey(), entry.getValue()));
      else
        updatedRanks.add(new Tuple(
            entry.getKey(),
            ((lambda1*(entry.getValue()-minValue)) +
                  ((maxValue-minValue)*(1 - lambda1)*updatedScores.get(entry.getKey()))
                  + minValue)
            )
        );
    }
    /*
     Now, the tuples have their Key, Value pairs with the desired scores. They need to be sorted.
     This is done by Bubble Sort. Not sure if my implementation is "safe" I believe it is.
     Subtract 1 from size because bubble sort will compare with i + 1; subtracting 1 avoids an
     ArrayOutOfBounds exception.
    */
    boolean flag = true;
    while(flag) {
      flag = false;
      for (int i = 0; i < updatedRanks.size() - 1; i++){
        if (updatedRanks.get(i).getValue() < updatedRanks.get(i+1).getValue()) {
          flag = true;
          Collections.swap(updatedRanks, i, i+1);
        }
      }
    }
    return updatedRanks;
  }

  /**
   * Compute the maximum and minimum. Used only once all documents have been appended.
   */
  private void computeMaxMin() {
    maxValue = docRankingsScores.get(0).getValue();
    minValue = docRankingsScores.get(docRankingsScores.size()-1).getValue();
  }

  /**
   * Prints the trecTopic in TREC Eval friendly format.
   * @param replacementDocRankingsScores optionally provided if printing an updated set of rankings.
   * @return string representation of this topic.
   */
  public String toString(ArrayList<Tuple> replacementDocRankingsScores) {
    ArrayList<Tuple> iterator;
    if (replacementDocRankingsScores != null) {
      iterator = replacementDocRankingsScores;
    } else {
      iterator = docRankingsScores;
    }

    StringBuilder lines = new StringBuilder();
    for (Tuple entry : iterator) {
      lines.append(queryNum )
          .append(" Q0 ")
          .append(entry.getKey())
          .append(" ")
          .append(entry.getValue())
          .append(" ")
          .append(iterator.indexOf(entry) + 1)
          .append(" Default\n");
    }
    // remaining documents: not re-sorted
    lines.append(remainingDocs);
    return lines.toString();
  }

  /**
   * @return the size of this topic.
   */
  public int size() {
    return size;
  }

  /**
   * @return all the docRankingScores of this topic.
   */
  public ArrayList<Tuple> getDocRankingScores() {
    return docRankingsScores;
  }

  /**
   * @return the queryNum of this topic.
   */
  public int getQueryNum() {
    return queryNum;
  }
}

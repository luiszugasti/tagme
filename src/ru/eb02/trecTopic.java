package ru.eb02;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import org.apache.commons.lang.NullArgumentException;

public class trecTopic {
  private final int queryNum;
  private ArrayList<Tuple> docRankingsScores;
  private int size;
  private double maxValue; // TODO: Normalization of entries.
  private double minValue;

  public trecTopic(int queryNum) {
    this.queryNum = queryNum;
    docRankingsScores = new ArrayList<>();
    size = 0;
  }

  /**
   * When running this class; the order HAS to be in form of decreasing score for docs.
   * So, ensure that when using this class!
   * If you want to reorder, maybe run updatescore with an empty hashmap? but then why is that
   * effective?
   * @param documentName the name of the document in TREC format
   * @param score the score assigned, by either original scoring or after centrality
   */
  public void addDocument(String documentName, double score) {
    if (documentName == null) {
      throw new NullArgumentException("documentName is null.");
    }
    docRankingsScores.add(new Tuple(documentName, score));
    size ++;
  }

  public int size() {
    return size;
  }

  public ArrayList<Tuple> getDocRankingScores() {
    return docRankingsScores;
  }

  public int getQueryNum() {
    return queryNum;
  }

  // Super slow - constant time
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
   * TODO: Ensure that the entries for the *NEW* updated TREC do not change the old ones!
   */
  public void updateRanks (HashMap<String, Double> updatedScores, double lambda1, double lambda2) {
    // Go thru the ArrayList of tuples and update their values.
    for (Tuple entry : docRankingsScores) {
      if (updatedScores.get(entry.getKey()) == null) throw new
          IllegalArgumentException("The specified entry for " + entry.getValue() +
          " is not found in the provided Hash map.");

      entry.setValue(lambda1*entry.getValue()
          +
          lambda2*updatedScores.get(entry.getKey()));
    }
    /*
     Now, the tuples have their Key, Value pairs with the desired scores. However, they are
     not sorted. Now, sort them.
     This is done by Bubble Sort. Not sure if my implementation is "safe" I believe it is.
    */
    boolean flag = true;
    while(flag) {
      // iterate thru the array, comparing each two elements.
      flag = false;
      for (int i = 0; i < docRankingsScores.size(); i++){
        if (docRankingsScores.get(i).getValue() < docRankingsScores.get(i+1).getValue()) {
          // Swap the elements. Not sure if I'll need a list iterator for this.
          flag = true;
          Tuple temp = docRankingsScores.get(i);
          docRankingsScores.set(i, docRankingsScores.get(i+1));
          docRankingsScores.set(i + 1, temp);
        }
      }
    }
    // Once sorted, we are done.
  }

  @Override
  public String toString() {
    // Print in TREC Eval format.
    StringBuilder lines = new StringBuilder();
    for (Tuple entry : docRankingsScores) {
      lines.append(queryNum)
          .append(" Q0 ")
          .append(entry.getKey())
          .append(" ")
          .append(entry.getValue())
          .append(" ")
          .append(docRankingsScores.indexOf(entry) + 1)
          .append(" Default\n");
    }
    return lines.toString();
  }
}

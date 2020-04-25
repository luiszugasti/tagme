package ru.eb02;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import org.apache.commons.lang.NullArgumentException;

/**
 *
 * The trecTopic class is a record of a single trecTopic as provided by the search results file.
 * It holds <strong>all</strong> entries for a document - and may get quite large.
 * We truly only care about reorganizing the top 200 docs.
 *
 */
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
   * TODO: ArrayList? Why not String?
   * @param lambda1 value of lambda for weighing purposes.
   * @param updatedScores the updated scores object.
   * @
   */
  public ArrayList<Tuple> updateRanks (HashMap<String, Double> updatedScores, double lambda1) {
    // Go thru the ArrayList of tuples and update their values.
    for (Tuple entry : docRankingsScores) {
      if (updatedScores.get(entry.getKey()) == null)
        // it's not found; this means it's 100 percent become a stray vertex.
        break;

      entry.setValue(lambda1*entry.getValue()
          +
          (1 - lambda1)*updatedScores.get(entry.getKey()));
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
    // TODO: normalize ALL scores? Or just linear interpolation of top 200?
    //       if we do interpolation of top 200 (easier) then just remember the minimum and maximum
    //       values of the original top 200 and interpolate within this range!
    return new ArrayList<>();
  }

  /**
   * Prints the trecTopic in TREC Eval friendly format.
   * @return string representation of this topic.
   */
  @Override
  public String toString() {
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

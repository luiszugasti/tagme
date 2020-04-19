package ru.eb02;

import java.util.ArrayList;
import org.javatuples.Triplet;

public class trecTopic {
  private final int queryNum;
  private ArrayList<Triplet<String, Integer, Double>> docRankingsScores;
  private int size;

  public trecTopic(int queryNum) {
    this.queryNum = queryNum;
    docRankingsScores = new ArrayList<>();
    size = 0;
  }

  public void addDocument(String documentName, int rank, double score) {
    docRankingsScores.add(new Triplet(documentName, rank, score));
    size ++;
  }

  public int size() {
    return size;
  }

  public ArrayList<Triplet<String, Integer, Double>> getDocRankingScores() {
    return docRankingsScores;
  }

  public int getQueryNum() {
    return queryNum;
  }

}

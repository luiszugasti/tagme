package ru.eb02;

import edu.uci.ics.jung.graph.*;
import it.acubelab.tagme.RelatednessMeasure;
import it.acubelab.tagme.preprocessing.TopicSearcher;
import java.io.IOException;
import java.util.Map;
import edu.uci.ics.jung.algorithms.importance.*;

/**
 * I will forcibly leave threshold un-used, as this is meant to be the *final* graph.
 */
public class DocGraph {
  private Graph<String, Double> graph;
  private final double threshold;

  public int getQuery() {
    return query;
  }

  private int query;

  public DocGraph (double threshold, int query) {
    graph = new UndirectedSparseGraph<>();
    this.threshold = threshold;
    this.query = query;
  }

  public void addVertex(String docName) {
    // Only add it if it's not there already
    if (!graph.containsVertex(docName))
      graph.addVertex(docName);
  }

  /**
   * I hope to God that the relatedness measures and topicsearchers can be reused.
   * @param docA Complete docA's map of top hits.
   * @param docB Complete docB's map of top hits.
   * @param docNameA The name of docA
   * @param docNameB The name of docB
   * @param rel the relatedness measure.
   */
  public boolean addEdge(Map<Integer,Integer> docA, Map<Integer,Integer> docB,
                      String docNameA, String docNameB, RelatednessMeasure rel) {
    if (duplicateEdgeCheck(docNameA, docNameB)) return false;
    // build the exact value of this edge. Assumes that provided document names are already
    // vertices. Also does not take into account frequency of weights!
    double runningWeight = 0;

    for (Integer widA : docA.keySet()) {
      for (Integer widB : docB.keySet()) {
        runningWeight += rel.rel(widA, widB);
      }
    }
    // divide total size of docA, docB's keys by runningWeight
    runningWeight = (double)(docA.size() + docB.size()) / runningWeight;
    // add the edge.
    if (runningWeight > threshold) {
      return graph.addEdge(runningWeight, docNameA, docNameB);
    }
    // didn't add an edge
    return false;
  }

  public boolean addTuple(Map<Integer,Integer> docA, Map<Integer,Integer> docB,
      String docNameA, String docNameB, RelatednessMeasure rel) {
    this.addVertex(docNameA);
    this.addVertex(docNameB);
    return this.addEdge(docA, docB, docNameA, docNameB, rel);
  }

  /**
   * Checks whether an edge specified between docA, docB already exists.
   * If it does, then the method will return True. False otherwise.
   * @param docA is the first document for testing in the JUNG graph
   * @param docB is the second document for testing in the JUNG graph
   * @return True/false if the edge exists
   */
  public boolean duplicateEdgeCheck(String docA, String docB) {
    return this.graph.findEdge(docA, docB) != null;
  }

  /**
   * This method so far is a stub for functionality. I am not sure how Jung's centrality measures
   * work yet.
   * In essence: This method will compute the rankings of the documents; these rankings will be
   * normalized.
   */
  public void computeCentrality() {
    BetweennessCentrality ranker = new BetweennessCentrality(graph);
    ranker.setNormalizeRankings(true);
    ranker.evaluate();
    ranker.printRankings(true, true); // They're not normalized. Jung has a normalize method. but. it's. private.
                                                        // WHY?????
  }
}

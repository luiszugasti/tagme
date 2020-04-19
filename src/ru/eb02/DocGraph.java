package ru.eb02;

import edu.uci.ics.jung.graph.*;
import it.acubelab.tagme.RelatednessMeasure;
import it.acubelab.tagme.preprocessing.TopicSearcher;
import java.io.IOException;
import java.util.Map;

/**
 * I will forcibly leave threshold un-used, as this is meant to be the *final* graph.
 */
public class DocGraph {
  private Graph<String, Double> graph;
  private final double threshold;

  public DocGraph (double threshold) {
    graph = new UndirectedSparseGraph<>();
    this.threshold = threshold;
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
    // build the exact value of this edge. Assumes that provided document names are already
    // vertices. Also does not take into account frequency of weights!
    double runningWeight = 0;

    for (Integer widA : docA.keySet()) {
      for (Integer widB : docB.keySet()) {
        runningWeight += rel.rel(widA, widB);
      }
    }

    // divide total runningWeight by the sum of the size of docA, docB's keys.
    runningWeight = runningWeight / (double)(docA.size() + docB.size());
    // invert it.
    runningWeight = 1/runningWeight;
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

  public boolean duplicateEdgeCheck(String docA, String docB) {
    return this.graph.findEdge(docA, docB) != null;
  }
}

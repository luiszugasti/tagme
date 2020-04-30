package ru.eb02;

import edu.uci.ics.jung.algorithms.importance.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.importance.Ranking;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import it.acubelab.tagme.RelatednessMeasure;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * I will forcibly leave threshold un-used, as this is meant to be the <strong>final</strong> graph.
 * The DocGraph class holds a graph of at most 200 documents, connected by Tagme Entities.
 * </br>
 * The graph itself is composed of String vertices, and connected by edges with weight <strong>
 * double</strong>.
 * Use this class to perform <strong>centrality measures</strong>.
 *
 */
public class DocGraph implements Serializable {
  private Graph<SimpleVertex, SimpleEdge> graph;
  private final double threshold;
  private final int query;

  public int getQuery() {
    return query;
  }

  public DocGraph (double threshold, int query) {
    graph = new UndirectedSparseGraph<>();
    this.threshold = threshold;
    this.query = query;
  }

  public boolean addVertex(String doc) {
    return graph.addVertex(new SimpleVertex(doc));
  }

  /**
   * Adds an edge. Previous vertices must have already been added.
   * @param docA Complete docA's map of top hits.
   * @param docB Complete docB's map of top hits.
   * @param docNameA The name of docA
   * @param docNameB The name of docB
   * @param rel the relatedness measure.
   * @throws ConcurrentModificationException in the case that an edge that was about to be added,
   *         was added before <em>it was formally added</em>.
   */
  public boolean addEdge(Map<Integer,Integer> docA, Map<Integer,Integer> docB,
                      String docNameA, String docNameB, RelatednessMeasure rel) {
    SimpleVertex testVertex1 = new SimpleVertex(docNameA);
    SimpleVertex testVertex2 = new SimpleVertex(docNameB);

    if (duplicateEdgeCheck(testVertex1, testVertex2)) return false;
    double runningWeight = computeEdgeWeight(docA, docB, rel);
    SimpleEdge testEdge = new SimpleEdge(testVertex1.toString(), testVertex2.toString(), runningWeight);

    if (runningWeight > threshold && Double.isFinite(runningWeight)) {
      boolean result = graph.addEdge(testEdge, testVertex1, testVertex2,
          EdgeType.UNDIRECTED);
      if (!result) {
        /*
        Somehow an error occurred. We checked that the edge did not exist already, and now
        the edge does exist (graph.addEdge returned false).
        */
        throw new ConcurrentModificationException("Somehow an edge that was supposed to be added"
            + " was already added.\n" + testEdge.toString() + "\n" + testVertex1.toString() + "\n"
            + testVertex2.toString());
      }
      return true;
    }
    System.out.println("Edge weight of " + runningWeight + " not added.");
    return false;
  }

  /**
   * Overloaded logic for testing. IF YOU CHANGE THIS CHANGE THE OTHER
   */
  public boolean addEdge(double weight, String docNameA, String docNameB) {
    SimpleVertex testVertex1 = new SimpleVertex(docNameA);
    SimpleVertex testVertex2 = new SimpleVertex(docNameB);

    if (duplicateEdgeCheck(testVertex1, testVertex2)) return false;
    SimpleEdge testEdge = new SimpleEdge(testVertex1.toString(), testVertex2.toString(), weight);

    // if the edge is infinite - we don't want it. just don't create it.
    if (weight > threshold && Double.isFinite(weight)) {
      boolean result = graph.addEdge(testEdge, testVertex1, testVertex2,
          EdgeType.UNDIRECTED);
      if (!result) {
        throw new ConcurrentModificationException("Somehow an edge that was supposed to be added"
            + " was already added.\n" + testEdge.toString() + "\n" + testVertex1.toString() + "\n"
            + testVertex2.toString());
      }
      return true;
    }
    System.out.println("Edge weight of " + weight + " not added.");
    return false;
  }

//  public boolean addTuple(Map<Integer,Integer> docA, Map<Integer,Integer> docB,
//      String docNameA, String docNameB, RelatednessMeasure rel) {
//    this.addVertex(docNameA);
//    this.addVertex(docNameB);
//    return this.addEdge(docA, docB, docNameA, docNameB, rel);
//  }

  /**
   * Checks whether an edge specified between docA, docB already exists.
   * If it does, then the method will return True. False otherwise.
   * Not sure if this is the fastest way...
   * @param docA is the first document for testing in the JUNG graph
   * @param docB is the second document for testing in the JUNG graph
   * @return True/false if the edge exists
   */
  private boolean duplicateEdgeCheck(SimpleVertex docA, SimpleVertex docB) {
    return this.graph.findEdge(docA, docB) != null;
  }

  /**
   * Publicly available method, using Strings instead of SimpleVertex, whose use is reserved
   * only for this class.
   * @param docA is the first document for testing in the JUNG graph
   * @param docB is the second document for testing in the JUNG graph
   * @return True/false if the edge exists
   */
  public boolean duplicateEdgeCheck(String docA, String docB) {
    return (this.graph.findEdge(new SimpleVertex(docA), new SimpleVertex(docB))) != null;
  }

  /**
   * Computes Betweenness Centrality from built in JUNG method.
   * When iterating thru results, it performs normalization automatically
   * @param cutoff the minimum value an edge must be, if it is saved in the new graph for
   *               centrality.
   * @return A hashmap of {String, Double} entities: The document name linked to its centrality
   *         score. Normalized.
   */
  public HashMap<String, Double> computeCentrality(double cutoff) {
    Graph<SimpleVertex, SimpleEdge> subGraph;
    try {
      subGraph = createSubGraph(cutoff);
    } catch (IllegalStateException ex) {
      // No edges were trimmed
      return null;
    }
    BetweennessCentrality measure = new BetweennessCentrality(subGraph, true, false);
    measure.setRemoveRankScoresOnFinalize(false);
    measure.evaluate();

    // Generalization. Not one of my strengths with Java.
    List<Ranking<?>> ranking = measure.getRankings();
    HashMap<String, Double> reRankedDocs = new HashMap<>();
    double maxScore = ranking.get(0).rankScore;
    double minScore = ranking.get(ranking.size()-1).rankScore;

    // Awful code smell
    int rCounter = 0;
    for(Ranking i : ranking) {
      double normScore = (i.rankScore - minScore)/(maxScore - minScore);
      reRankedDocs.put(i.getRanked().toString(), normScore);
    }
    return reRankedDocs;
  }

  /**
   * Computes an edge's weight based on the entities provided. Uses tagme service rel.
   * Currently does not take into account frequency of weights!
   * @param docA First document's hashmap of entities in {wid, frequency} format.
   * @param docB Second document's hashmap of entities in {wid, frequency} format.
   * @param rel Relatedness measure to check comparison of entities.
   * @return a weight value between 0 and 1.
   */
  private static double computeEdgeWeight(Map<Integer,Integer> docA, Map<Integer,Integer> docB,
      RelatednessMeasure rel) {
    double runningWeight = 0;
    for (Integer widA : docA.keySet()) {
      for (Integer widB : docB.keySet()) {
        runningWeight += rel.rel(widA, widB);
      }
    }
    // return the total size of docA, docB's keys divided by runningWeight
    // hint: this may return "infinite" values.
    return (double)(docA.size() + docB.size()) / runningWeight;
  }

  /**
   * Creates a sub-graph from this DocGraph's current graph. Only used when performing centrality
   * measurements.
   * @param cutoff a value between 0 and 1; edges below this value are trimmed.
   * @return a subgraph of this.graph.
   * @throws IllegalStateException in the case that the sub-graph procedure did not trim edges.
   */
  private Graph<SimpleVertex, SimpleEdge> createSubGraph(double cutoff)
      throws IllegalStateException {
    // iterate thru all edges in a graph -> n^2 time worst case for sub-graphing
    Graph<SimpleVertex, SimpleEdge> subGraph = new UndirectedSparseGraph<>();
    Collection<SimpleEdge> allOriginalEdges = graph.getEdges();
    for (SimpleEdge sample : allOriginalEdges) {
      if (sample.getWeight() >= cutoff) {
        subGraph.addEdge(
            new SimpleEdge(sample.getVertex1(), sample.getVertex2(), sample.getWeight()),
            new SimpleVertex(sample.getVertex1()),
            new SimpleVertex(sample.getVertex2()));
      }
    }
    if(subGraph.getEdgeCount() == graph.getEdgeCount()) throw new IllegalStateException();
    return subGraph;
  }

  /*
  Serialization of the document graph and deserialization to file.
   */

  public void serializeDocGraph() {
    // this will write the DocGraph to file
    // Saving of object in a file
    ArrayList<String> output = new ArrayList<>();
    // get simple values
    output.add("Threshold: " + threshold);
    output.add("Query: " + query);

    // Invoke printing of edges
    Collection<SimpleEdge> edges = graph.getEdges();
    // edges now, first is the weight, then vertex1, then vertex2
    for (SimpleEdge edge : edges)
      output.add(edge.getWeight() + " " + edge.getVertex1() + " " + edge.getVertex2());

    // print it
    FileTools.writeFile(output, "docgraphs/" + query + "graph.ser", null);
  }

  public static DocGraph deSerializeDocGraph (int query) throws IOException {
    String results = FileTools.readFileUTF8("docgraphs/" + query + "graph.ser",
        true);

    // parse thru it
    String[] lines = results.split("\n");
    // first line is the threshold
    double threshold = Double.parseDouble(lines[0].split(" ")[1]);

    DocGraph deSerialized = new DocGraph(threshold, query);

    // parse thru the rest of the lines
    for (int i = 2; i < lines.length; i++) {
      String[] temp = lines[i].split(" ");
      deSerialized.addEdge(Double.parseDouble(temp[0]), temp[1], temp[2]);
    }
    return deSerialized;
  }
}

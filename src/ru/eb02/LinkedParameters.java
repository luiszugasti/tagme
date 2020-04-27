package ru.eb02;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * LinkedParameters class: a single collection of linked parameters.
 * Once instantiated, a single shot test can be run.
 */
public class LinkedParameters {
  final double lambda1;
  final double centralityCutoff;
  double trecResult;
  final int kill;
  final DocGraph docGraph;

  public LinkedParameters(double lambda1, double centralityCutoff, int kill, DocGraph docGraph)
      throws IllegalArgumentException {
    if (lambda1 < 0 || lambda1 > 1 || centralityCutoff < 0 || centralityCutoff > 1) {
      throw new IllegalArgumentException("Values provided for lambda1 or centralityCutoff"
          + " are illegal.\n"
          + "centralityCutoff: " + centralityCutoff
          + "lambda1: " + lambda1);
    }

    this.kill = kill;
    this.lambda1 = lambda1;
    this.centralityCutoff = centralityCutoff;
    this.docGraph = docGraph;
  }


  /**
   * Performs centrality for the provided graph, applies these results to the provided topic,
   * runs these results against TREC Eval, saves all related files, and returns the desired score.
   * @return A desired value from TREC eval.
   */
  public String runSubTest () {
//    // take provided graph (graph from the array of graphs) and run centrality
//    HashMap<String, Double> centrality = docGraph.computeCentrality(centralityCutoff);
//    // take results from centrality and apply them to single trecTopic
//    ArrayList<Tuple> result = tt.updateRanks(centrality, lambda1);
//    /* Run TREC Eval against these new results. */
//    // A: Convert results to string.
//    String results = tt.toString(result);
//    // B: Save results to file.
//    trecResult resultDeserialized = FileTools.openTrecScoresFile(results);
//    FileTools.writeTrecScoresFile();
    return "Stub";
  }
}

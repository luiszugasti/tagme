package ru.eb02;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * LinkedParameters class: a single collection of linked parameters.
 * Once instantiated, a single shot test can be run.
 */
public class LinkedParameters {
  final double lambda1;
  final double centralityCutoff;
  double trecResult;
  final int kill;
  final String runName;

  public LinkedParameters(double lambda1, double centralityCutoff, int kill, String runName)
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
    this.runName = runName;
  }

  public void runFullExperiment(Set<DocGraph> d, trecTopic[] t, String goldenQrels) {
    // This queue will hold all the toString representations of the results, for being printed out
    Queue<String> fullResults = new ConcurrentLinkedQueue<>();
    int i = 0;
    String lambda1Send = Double.toString(lambda1);
    String lambda2Send = Double.toString(1 - lambda1);
    String centralitySend = Double.toString(centralityCutoff);

    // Using a parallel stream, send the docgraph data and related trec topic data to the test.
    // I have briefly tested TREC eval and know that it does not require the entries to be in order.
    // so this will be fine.

    d.parallelStream().forEach((docGraph -> {
      fullResults.add(runSubTest(docGraph, t[docGraph.getQuery()-1]));
    }));

    // print all results into an "array list" for compatability
    ArrayList<String> output = new ArrayList<>(fullResults);

    // Now we have all the results for 200 docs. Run TREC eval against them and save.
    String pathToResults = FileTools.writeTrecSearchResultsFile(output, runName,
        lambda1Send, lambda2Send, centralitySend);
    System.out.println("The path that was sent! " + pathToResults);

    // get the trec results and save them here.
    ArrayList<String> results = FileTools.trecEvaler(goldenQrels, pathToResults);

    //
    FileTools.writeTrecScoresFile(results, runName, lambda1Send, lambda2Send, centralitySend);
  }

  /**
   * Performs centrality for the provided graph, applies these results to the provided topic,
   * runs these results against TREC Eval, saves all related files, and returns the desired score.
   * @return A desired value from TREC eval.
   */
  public String runSubTest (DocGraph d, trecTopic tt) {
    // take provided graph and run centrality
    HashMap<String, Double> centrality = d.computeCentrality(centralityCutoff);
    // take results from centrality and apply them to single trecTopic
    ArrayList<Tuple> result = tt.updateRanks(centrality, lambda1);
    return tt.toString(result);
  }
}

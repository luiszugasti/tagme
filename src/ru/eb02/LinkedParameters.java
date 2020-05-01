package ru.eb02;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * LinkedParameters class: a single collection of linked parameters.
 * Once instantiated, a single shot test can be run.
 */
public class LinkedParameters {
  final double[] lambda1;
  final double centralityCutoff;
  double trecResult;
  final int kill;
  final String runName;

  public LinkedParameters(double[] lambda1, double centralityCutoff, int kill, String runName) {
    this.kill = kill;
    this.lambda1 = lambda1;
    this.centralityCutoff = centralityCutoff;
    this.runName = runName;
  }

  public void runFullExperiment(ArrayList<DocGraph> d, trecTopic[] t, String goldenQrels, trecResult baseline) {
    // This queue will hold all the toString representations of the results, for being printed out
    Queue<ArrayList<Tuple>> fullResults = new ConcurrentLinkedQueue<>();
//    String lambda1Send = Double.toString(lambda1);
//    String lambda2Send = Double.toString(1 - lambda1);
//    String centralitySend = Double.toString(centralityCutoff);

    // Using a parallel stream, send the docgraph data and related trec topic data to the test.
    // I have briefly tested TREC eval and know that it does not require the entries to be in order.
    // so this will be fine.

    //I change to simple stream to see what happen
    d.parallelStream().forEach((docGraph -> {
      fullResults.add(runSubTest(docGraph, t[docGraph.getQuery()-1]));
    }));
    System.out.println("Whole subtest completed");

    // We have to print out the entries for INDIVIDUALLY linked arraylists of tuples (those
    // with the same values of centrality
    System.out.println("whole subtest complete");
    for (int i = 0; i < 10; i++) {
      for (ArrayList<Tuple> str : fullResults) {
      //hardcoded for 1000 test runs
        ArrayList<String> output = new ArrayList<>();
        String lambda1Send = String.valueOf(str.get(i).getValue());
        String lambda2Send = String.valueOf(1-str.get(i).getValue());
        String centralitySend = String.valueOf(centralityCutoff);
        output.add(str.get(i).getKey());

        // Now we have all the results for 200 docs. Run TREC eval against them and save.

        String pathToResults = FileTools.writeTrecSearchResultsFile(output, runName,
            lambda1Send, lambda2Send, centralitySend);
        System.out.println("The path that was sent! " + pathToResults);

        // get the trec results and save them here.
        ArrayList<String> results = FileTools.trecEvaler(goldenQrels, pathToResults);

        StringBuilder resultrec = new StringBuilder();
        for (String s : results) {
          resultrec.append(s).append("\n");
        }

        trecResult r = FileTools.openTrecScoresFile(resultrec.toString());
        System.out
            .println("l1: " + lambda1Send + "l2: " + lambda2Send + " centrality " + centralitySend +
                " IS MAP BETTER? " + Boolean.toString(r.getMap() > baseline.getMap()) +
                " IS RPREC BETTER? " + Boolean.toString(r.getRprec() > baseline.getRprec())
            );
        //
        FileTools
            .writeTrecScoresFile(results, runName + "TREC", lambda1Send, lambda2Send,
                centralitySend);
      }
    }
  }

  /**
   * Performs centrality for the provided graph, applies these results to the provided topic,
   * runs these results against TREC Eval, saves all related files, and returns the desired score.
   * OPTIMIZED: Now, for a single value of centrality, this method will run this centrality
   * value through ALL provided values of lambda (this way we don't have to compute the same
   * centrality for slightly different value of lamdba.
   * @return A desired value from TREC eval.
   */
  public ArrayList<Tuple> runSubTest (DocGraph d, trecTopic tt) {
    ArrayList<Tuple> resul = new ArrayList<>();
    // take provided graph and run centrality
    HashMap<String, Double> centrality = d.computeCentrality(centralityCutoff);
    // take results from centrality and apply them to single trecTopic
    // ARRAY LIST VALUES ARE GUARANTEED TO BE SYNCHRONIZED!
    if (centrality == null) {
      System.out.println("NULL.");
      for (double l : lambda1) {
        resul.add(new Tuple(tt.toString(null), l));
        System.out.println("NULL optimized @." + l);
      }
    } else {
      for (double l : lambda1) {
        System.out.println("no null.");
        resul.add(new Tuple(tt.toString(tt.updateRanks(centrality, l)), l));
        System.out.println("no null optimized @." + l);
      }
    }
    // IF THIS IS NUL, THIS WILL GET US NO RESULT!
    return resul;
  }
}

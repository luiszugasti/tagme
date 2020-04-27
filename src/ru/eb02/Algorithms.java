package ru.eb02;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Generic class for static implementations of algorithms.
 */
public class Algorithms {

  /**
   * gridSearchTrec assumes a list of graphs entries has already been provided to it.
   * It also accepts the steps for lambda1-lambda2; and steps for centralityCutoff.
   *
   * The organization of Grid search is for the entries to be synchronized.
   * Each complete run of Grid search will accumulate a return set of at most 200 trec topics,
   * each re-organized if necessary as per their underlying implementations. If there is an error
   * where the centrality of cases is zero (thus, we have a centralityCutoff that does not
   * sufficiently remove edges from the chosen graph), the whole set of tests is cancelled
   * and the value chosen for centralityCutoff is saved for further perusal.
   *
   * This set of values is intended so as to allow for removal of these values should subsequent
   * tests be required; thus, not running these values of centrality once more.
   *
   * Lastly, it includes a call to TREC eval, where specific hyper parameters from that result
   * are then evaluated and saved as a linked list.
   *
   */
  public static void gridSearchTrec (double[] lambdaSteps, double[] centralitySteps, int kill,
      trecTopic[] trecTopics, DocGraph[] docGraphs, trecResult baseTrecResult) {

    /**
     * LinkedParameters inner class: a single collection of linked parameters.
     * Once instantiated, a single shot test can be run.
     */
    class LinkedParameters {

      final double lambda1;
      final double centralityCutoff;
      double trecResult;
      final int kill;

      public LinkedParameters(double lambda1, double centralityCutoff, int kill)
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
      }

    }

    // Create array of LinkedParameters
    ArrayList<LinkedParameters> allPossibleCombinations = new ArrayList<>();
    for (double lambda : lambdaSteps) {
      for (double centrality : centralitySteps)
        allPossibleCombinations.add(new LinkedParameters(lambda, centrality, kill));
    }
  }

  /**
   * Performs centrality for the provided graph, applies these results to the provided topic,
   * runs these results against TREC Eval, saves all related files, and returns the desired score.
   * @param tt The trec topic for this test.
   * @param dc The document graph to perform centrality on.
   * @param centCutoff Centrality cutoff value, between 0 and 1.
   * @param lambda1 Value for weighing trec scores vs centrality scores.
   * @return A desired value from TREC eval.
   */
  public static String runSubTest (trecTopic tt, DocGraph dc,
      double centCutoff, double lambda1, String goldenQrels) {
    // take provided graph (graph from the array of graphs) and run centrality
    HashMap<String, Double> centrality = dc.computeCentrality(centCutoff);
    // take results from centrality and apply them to single trecTopic
    ArrayList<Tuple> result = tt.updateRanks(centrality, lambda1);
    /* Run TREC Eval against these new results. */
    // A: Convert results to string.
    String results = tt.toString(result);
    // B: Save results to file.
    trecResult resultDeserialized = FileTools.openTrecScoresFile(results);
    FileTools.writeTrecScoresFile();




    return "Stub";
  }
}

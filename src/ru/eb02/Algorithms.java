package ru.eb02;

import java.util.ArrayList;

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
      final double lambda2;
      final double centralityCutoff;
      double trecResult;
      final int kill;

      public LinkedParameters (double lambda1, double centralityCutoff, int kill) {
        if (lambda1 < 0 || lambda1 > 1 || centralityCutoff < 0 || centralityCutoff > 1) {
          throw new IllegalArgumentException("Values provided for lambda1 or centralityCutoff"
              + " are illegal.\n"
              + "centralityCutoff: " + centralityCutoff
              + "lambda1: " + lambda1);
        }

        this.kill = kill;
        this.lambda1 = lambda1;
        this.lambda2 = 1 - lambda1;
        this.centralityCutoff = centralityCutoff;
      }

      public String runSingleCentrality (trecTopic tt, DocGraph dc, trecResult tr) {
        // take provided graph and run centrality (graph from the array of graphs)
        // take results from centrality and apply them to single trecTopic
        // Return the ordered toString representation from trecTopic.
        return "Stub";
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
   * A single trecTest, called with values of lambda1, lambda2, and centrality cutoff.
   * It also accepts a
   */
  public static double trecTest (double lambda1, double lambda2, double centralityCutoff) {
    // STUB
    return 7.00;
  }
}

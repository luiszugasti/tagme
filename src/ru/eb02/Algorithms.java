package ru.eb02;

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
      trecTopic[] topics, DocGraph[] docGraphs, trecResult baseTrecResult) {

    class LinkedParameters {
      double lambda1;
      double lambda2;
      double centralityCutoff;
      double trecResult;
      // TODO: Use the valid flag to indicate that this result had no illegal values for centrality cutoff.
      boolean valid;

      public LinkedParameters (double lambda1, double lambda2, double centralityCutoff) {
        this.lambda1 = lambda1;
        this.lambda2 = lambda2;
        this.centralityCutoff = centralityCutoff;
      }

      public void runTest () {

      }

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

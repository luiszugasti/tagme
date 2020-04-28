package ru.eb02;

import java.util.ArrayList;
import java.util.HashMap;

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
public class GridSearchTREC {
  double[] centralitySteps;
  double[] lambdaSteps;
  int kill;
  trecTopic[] trecTopics;
  DocGraph[] docGraphs;
  trecResult baseTrecResult;
  String runName;
  String TRECEvalPath;
  String goldenQrels;

  /**
   * Constructor. Pass in all relevant file names and placements for running an experiment.
   * @param lambdaSteps Double array of lambda steps
   * @param centralitySteps Double array of centrality steps
   * @param kill stop value for killing a test
   * @param trecTopics all topics.
   * @param docGraphs all docGraphs.
   * @param baseTrecResult base trec result for comparing entries.
   */
  public GridSearchTREC(double[] lambdaSteps,
      double[] centralitySteps,
      int kill,
      trecTopic[] trecTopics,
      DocGraph[] docGraphs,
      trecResult baseTrecResult,
      String runName,
      String TRECEvalPath,
      String goldenQrels) {
    this.centralitySteps = centralitySteps;
    this.lambdaSteps = lambdaSteps;
    this.kill = kill;
    this.trecTopics = trecTopics;
    this.docGraphs = docGraphs;
    this.runName = runName;
    this.TRECEvalPath = TRECEvalPath;
    this.goldenQrels = goldenQrels;
  }

  /**
   * This is not a factory.
   * Creates all the experiments ever needed and then runs them.
   */
  public void experimentFactory() {
    // Create array of LinkedParameters
    ArrayList<LinkedParameters> allPossibleCombinations = new ArrayList<>();
    for (double lambda : lambdaSteps) {
      for (double centrality : centralitySteps) {
        allPossibleCombinations.add(new LinkedParameters(lambda, centrality, kill, runName));
      }
    }
    // Now, we have all the potential choices for running each experiment. Go through each Linked
    // Parameter and run the test.
    for (LinkedParameters p : allPossibleCombinations) {
      p.runFullExperiment(docGraphs, trecTopics, goldenQrels);
    }
  }
}

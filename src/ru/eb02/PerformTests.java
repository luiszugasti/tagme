package ru.eb02;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PerformTests {

  public static void main(String[] args) throws IOException, InterruptedException {
    // deserialize all docs found...
    // Usage string for command line interfaces
    String usage =
        "Hi! this is for running trec tests.\n"
            + "[-baselineSearchResultsPath file]\n"
            + "[-goldenQrelsPath file] \n"
            + "[-runName name]\n"
            + "[-TRECEvalPath file]\n"
            + "[-baselineSearchResultsScorePath file]\n"
            + "[-centralityValues file]\n"
            + "[-lambdaValues file]\n"
            + "[-baseForRuns path]\n"
            + "[-baseForDocs path]\n";

    if (args.length > 0 && ("-h".equals(args[0]) || "-help".equals(args[0]))) {
      System.out.println(usage);
      System.exit(0);
    }
    // STATIC directories for opening EVERYTHING.
    String baselineSearchResultsPath =
        "Tests/Baseline/baseline_tf_idf_search_results.test";
    String goldenQrelsPath =
        "Tests/Baseline/golden_qrels.test";
    String runName =
        "firstTestRun";
    String TRECEvalPath =
        "Tests/./trec_eval";
    String baselineSearchResultsScorePath =
        "Tests/Baseline/baseline_search_results_score.test";
    String centralityValues =
        "Runs/testRunCentralityValues.txt";
    String lambdaValues =
        "Runs/testRunLambdaValues.txt";
    String baseForRuns =
        "Runs/";
    // DO NOT PLACE THE DOC LINK IN YOUR PROJECT DIRECTORY!
    String baseForDocs =
        "../Docs/";

    // fetch all the related document directories for running a test.
    for(int i = 0; i < args.length; i++) {
      if ("-baselineSearchResultsPath".equals(args[i])) {
        baselineSearchResultsPath = args[i+1];
        i++;
      } else if ("-goldenQrelsPath".equals(args[i])) {
        goldenQrelsPath = args[i+1];
        i++;
      } else if ("-runName".equals(args[i])) {
        runName = args[i+1];
        i++;
      } else if ("-TRECEvalPath".equals(args[i])) {
        TRECEvalPath = args[i+1];
        i++;
      } else if ("-baselineSearchResultsScorePath".equals(args[i])) {
        baselineSearchResultsScorePath = args[i+1];
        i++;
      } else if ("-centralityValues".equals(args[i])) {
        centralityValues = args[i+1];
        i++;
      } else if ("-lambdaValues".equals(args[i])) {
        lambdaValues = args[i+1];
        i++;
      } else if ("-baseForRuns".equals(args[i])) {
        baseForRuns = args[i+1];
        i++;
      } else if ("-baseForDocs".equals(args[i])) {
        baseForDocs = args[i+1];
        i++;
      }
    }

    System.out.println("I will now deserialize all the docgraphs for you.");

    //READ Files process
    trecResult baseTrecScore = FileTools.openTrecScoresFile(FileTools.readFileUTF8(
        baselineSearchResultsScorePath, true
    ));

    // Open all the TrecTopics
    trecTopic[] completeTrecTopics = FileTools.openTrecSearchResultsFile(FileTools.readFileUTF8(
        baselineSearchResultsPath, true
    ));

    // deserialize all docgraphs
    ArrayList<DocGraph> allDocGraphs = new ArrayList<>();

    String documentDir = "../docgraphsnormalized/";

    String testdir = "../run1/";
    System.out.println("MAKE SURE THAT RUN1 DOES NOT HAVE STUFF IN IT");
    TimeUnit.SECONDS.sleep(1);
    System.out.println("5 SECONDS REMAIN");
    TimeUnit.SECONDS.sleep(1);
    System.out.println("4 SECONDS REMAIN");
    TimeUnit.SECONDS.sleep(1);
    System.out.println("3 SECONDS REMAIN");
    TimeUnit.SECONDS.sleep(1);
    System.out.println("2 SECONDS REMAIN");
    TimeUnit.SECONDS.sleep(1);
    System.out.println("1 SECONDS REMAIN");
    TimeUnit.SECONDS.sleep(1);
    System.out.println("0 SECONDS REMAIN OVERWRITINNNNNG");

    // Batch processing: Get all the docgraphs.
    for (int o = 1; o <= 200; o++) {
      allDocGraphs.add(DocGraph.deSerializeDocGraph(o));
    }

    System.out.println("doc graphs reloaded!");

//    System.out.println("TIME TO DIE");
//    for (DocGraph doc : allDocGraphs){
//      doc.serializeDocGraph();
//    }
//    System.out.println("die");
//    System.exit(99);

    double[] centralityValueArray = FileTools.readSelectedGridSearchParameters(
        FileTools.readFileUTF8(centralityValues, true)
    );
    double[] LambdaValueArray = FileTools.readSelectedGridSearchParameters(
        FileTools.readFileUTF8(lambdaValues, true)
    );

    // Now, run through all the tests.
    System.out.println("Running tests with values provided for centrality, lambda cutoff.");
    GridSearchTREC fullTest = new GridSearchTREC(LambdaValueArray,
        centralityValueArray,
        0,
        completeTrecTopics,
        allDocGraphs,
        baseTrecScore,
        runName,
        TRECEvalPath,
        goldenQrelsPath);

    fullTest.experimentFactory();

    System.out.println("Run completed!");
  }
}

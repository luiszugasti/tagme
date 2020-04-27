package ru.eb02;

import static org.junit.jupiter.api.Assertions.*;

import it.acubelab.tagme.Disambiguator;
import it.acubelab.tagme.RelatednessMeasure;
import it.acubelab.tagme.RhoMeasure;
import it.acubelab.tagme.Segmentation;
import it.acubelab.tagme.TagmeParser;
import it.acubelab.tagme.config.TagmeConfig;
import it.acubelab.tagme.preprocessing.TopicSearcher;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

class MainTest {

//  @Test
//  public void main() throws IOException {
  public static void main(String[] args) throws IOException {

    // Usage string for command line interfaces
    String usage =
        "In order to use me, please ensure following parameters are filled.\n"
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

    final long startTime = System.currentTimeMillis();

    // Initiate TAGME modules.
    TagmeConfig.init();
    String wikiLanguage = "en";
    RelatednessMeasure rel = RelatednessMeasure.create(wikiLanguage);
    TopicSearcher searcher = new TopicSearcher(wikiLanguage);
    TagmeParser parser = new TagmeParser(wikiLanguage, true);
    Disambiguator disamb = new Disambiguator(wikiLanguage);
    Segmentation segmentation = new Segmentation();
    RhoMeasure rho = new RhoMeasure();

    //READ Files process
    trecResult baseTrecScore = FileTools.openTrecScoresFile(FileTools.readFileUTF8(
        baselineSearchResultsScorePath, true
    ));
    trecTopic[] completeTrecTopics = FileTools.openTrecSearchResultsFile(FileTools.readFileUTF8(
        baselineSearchResultsPath, true
    ));
    double[] centralityValueArray = FileTools.readSelectedGridSearchParameters(
        FileTools.readFileUTF8(centralityValues, true)
    );
    double[] LambdaValueArray = FileTools.readSelectedGridSearchParameters(
        FileTools.readFileUTF8(lambdaValues, true)
    );
    FileTools.createFilePath(baseForRuns+runName);

    final long readDataTime = System.currentTimeMillis();
    System.out.println("Time to read data: " + (readDataTime - startTime));

    final long readDocsTime = System.currentTimeMillis();
    //SAVE to desired process pipelines
    HashMap<String, Doc> mapDocs = new HashMap<>();
    int i = 0;

    // puts 40,000 docs into memory.
    for (trecTopic t : completeTrecTopics) {
      for (Tuple tup : t.getDocRankingScores()) {
        if (mapDocs.containsKey(tup.getKey())) {
          System.out.println("duplicate found");
          continue;
        }
        String tmpFilePath = baseForDocs + tup.getKey();
        mapDocs.put(tup.getKey(),
            new Doc(FileTools.readFileUTF8(tmpFilePath, true),
                tup.getKey(),
                wikiLanguage,
                rel,
                disamb,
                segmentation,
                rho,
                parser));
        System.out.println("docs processed: " + i++);
      }
    }
    System.out.println("Time to read, process 40000 docs: " + (readDocsTime - readDataTime));

    //Create document graphs.
    DocGraph[] allDocGraphs = new DocGraph[200];
    for (int j = 0; j <200; j++) {
      allDocGraphs[i] = new DocGraph(0, i+1);
    }
//    ArrayList<LinkedDocGraphTrecTopic> topicsAndDocs = new ArrayList<>();

//    //Assign docgraphs to trectopics
//    for (DocGraph docGraph : allDocGraphs) {
//      topicsAndDocs.add(new LinkedDocGraphTrecTopic(docGraph, completeTrecTopics[docGraph.getQuery()-1]));
//    }
//
//    for (LinkedDocGraphTrecTopic ldt : topicsAndDocs) {
//      for (Tuple t1 : ldt.getTt().getDocRankingScores()) {
//        for (Tuple t2 : ldt.getTt().getDocRankingScores()) {
//
//        }
//    }
    //Fill all document graphs with docs (edges) and don't make them duplicate!
    // For each document graph,
    for (DocGraph docGraph : allDocGraphs) {
      // go through each tuple in the corresponding trectopics object,
      for (Tuple t1 : completeTrecTopics[docGraph.getQuery()-1].getDocRankingScores()) {
        for (Tuple t2 : completeTrecTopics[docGraph.getQuery()-1].getDocRankingScores()) {
          if(t2.getKey().equals(t1.getKey()))
            continue;
          // Why don't we check for duplicate edges?
          // i.e. edge1: v1 to v2, edged2: v2 to v1
          // JUNG quietly won't create these edges.
          docGraph.addEdge(mapDocs.get(t1.getKey()).getTopMap(),
              mapDocs.get(t2.getKey()).getTopMap(),
              t1.getKey(),
              t2.getKey(),
              rel);
        }
      }
    }

    // provided: all 200 *docGraph[]* have been filled with relevant docs.
    // all completeTrecTopics are created with important and non-important docs.
    // all relevant files are opened.

    // Now, run through all the tests.
    System.out.println("Running tests with values provided for centrality, lambda cutoff.");
    GridSearchTREC fullTest = new GridSearchTREC(LambdaValueArray,
        centralityValueArray,
        0,
        completeTrecTopics,
        allDocGraphs,
        baseTrecScore,
        runName,
        TRECEvalPath);

    System.out.println("Testing completed.");
  }
}
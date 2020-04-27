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
import java.util.HashMap;
import org.junit.jupiter.api.Test;

class MainTest {

//  @Test
//  public void main() throws IOException {
  public static void main(String[] args) throws IOException {
    final long startTime = System.currentTimeMillis();
    /*
    Stub for me to test how I'm going to run ALL these tests.
     */

    // Initiate TAGME modules.
    TagmeConfig.init();
    String wikiLanguage = "en";
    RelatednessMeasure rel = RelatednessMeasure.create(wikiLanguage);
    TopicSearcher searcher = new TopicSearcher(wikiLanguage);
    TagmeParser parser = new TagmeParser(wikiLanguage, true);
    Disambiguator disamb = new Disambiguator(wikiLanguage);
    Segmentation segmentation = new Segmentation();
    RhoMeasure rho = new RhoMeasure();

    // STATIC directories for opening EVERYTHING.
    final String baselineSearchResultsPath =
        "Tests/Baseline/baseline_tf_idf_search_results.test";
    final String goldenQrelsPath =
        "Tests/Baseline/golden_qrels.test";
    final String runName =
        "firstTestRun";
    final String TRECEvalPath =
        "Tests/./trec_eval";
    final String baselineSearchResultsScorePath =
        "Tests/Baseline/baseline_search_results_score.test";
    final String centralityValues =
        "Runs/testRunCentralityValues.txt";
    final String lambdaValues =
        "Runs/testRunLambdaValues.txt";
    final String baseForRuns =
        "Runs/";
    // DO NOT PLACE THE DOC LINK IN YOUR PROJECT DIRECTORY!
    final String baseForDocs =
        "../Docs/";

    //READ Files process
    trecResult baseTrecScore = FileTools.openTrecScoresFile(FileTools.readFileUTF8(
        baselineSearchResultsScorePath, true
    ));
    trecTopic[] completeTrecTopics = FileTools.openTrecSearchResultsFile(FileTools.readFileUTF8(
        baselineSearchResultsPath, true
    ));
    Double[] centralityValueArray = FileTools.readSelectedGridSearchParameters(
        FileTools.readFileUTF8(centralityValues, true)
    );
    Double[] LambdaValueArray = FileTools.readSelectedGridSearchParameters(
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

    //Fill all document graphs with docs (edges) and don't make them duplicate!
    // For each document graph,
    for (DocGraph docGraph : allDocGraphs) {
      // go through each tuple in the corresponding trectopics object,
      for (Tuple t : completeTrecTopics[docGraph.getQuery()-1].getDocRankingScores()) {
        //FIXME: Using the same top map! << FIX
        //And add an edge, comparing two docs within that trectopics object.
        docGraph.addEdge(mapDocs.get(t.getKey()).getTopMap(),
            mapDocs.get(t.getKey()).getTopMap(),
            t.getKey(),
            t.getKey(),
            rel);
      }
    }



    System.out.println("Testing completed.");
  }
}
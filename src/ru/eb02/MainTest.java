package ru.eb02;

import it.acubelab.tagme.Disambiguator;
import it.acubelab.tagme.RelatednessMeasure;
import it.acubelab.tagme.RhoMeasure;
import it.acubelab.tagme.Segmentation;
import it.acubelab.tagme.TagmeParser;
import it.acubelab.tagme.config.TagmeConfig;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class MainTest {

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

    System.out.println("This MainTest file will get all document entities, and document graphs.");
    System.out.println("It will then save them as .ser files so that they can be used in any\n" +
        "required grid search tests.");

    //READ Files process
    trecResult baseTrecScore = FileTools.openTrecScoresFile(FileTools.readFileUTF8(
        baselineSearchResultsScorePath, true
    ));

    // Open all the TrecTopics
    trecTopic[] completeTrecTopics = FileTools.openTrecSearchResultsFile(FileTools.readFileUTF8(
        baselineSearchResultsPath, true
    ));

    // Create a set of unique doc names (this gets only the doc names...)
    Set<String> allDocNames = new HashSet<>();
    for (trecTopic t : completeTrecTopics) {
      for (Tuple tup : t.getDocRankingScores())
        allDocNames.add(tup.getKey());
    }
    System.out.println("Set of all unique doc names created!");

    double[] centralityValueArray = FileTools.readSelectedGridSearchParameters(
        FileTools.readFileUTF8(centralityValues, true)
    );
    double[] LambdaValueArray = FileTools.readSelectedGridSearchParameters(
        FileTools.readFileUTF8(lambdaValues, true)
    );
    FileTools.createFilePath(baseForRuns+runName);

    int i = 0;

    // Initiate TAGME modules.
    TagmeConfig.init();
    String wikiLanguage = "en";
    RelatednessMeasure rel = RelatednessMeasure.create(wikiLanguage);
    System.out.println("Relatedness measure started.");
//    TopicSearcher searcher = new TopicSearcher(wikiLanguage);
//    System.out.println("Topic seracher started.");
    TagmeParser parser = new TagmeParser(wikiLanguage, true);
    System.out.println("Parser started.");
    Disambiguator disamb = new Disambiguator(wikiLanguage);
    System.out.println("Disambugator started.");
    Segmentation segmentation = new Segmentation();
    System.out.println("Segmentation started.");
    RhoMeasure rho = new RhoMeasure();
    System.out.println("RhoMeasure started.");

    System.out.println("Starting up the testing now.");

    // Basically what I need here is to get all the unique doc names from all the relationships
    // right?

    //SAVE to desired process pipelines
    Map<String, Doc> mapDocs = new ConcurrentHashMap<>();

    //Basically what this would look like in Streams:
    // create a set of doc_titles. DONE.
    // create our concurrent Map. DONE.
    // For each doc name, run the stream

    String finalBaseForDocs = baseForDocs;
    allDocNames.parallelStream().forEach((documentName) -> {
      try {
        mapDocs.put(documentName,
            new Doc(FileTools.readFileUTF8(finalBaseForDocs + documentName, true),
                documentName,
                wikiLanguage,
                rel,
                disamb,
                segmentation,
                rho,
                parser));
        // sanity
        System.out.println("Docs in mapDocs: " + mapDocs.size());
      } catch(IOException e) {
        System.out.println("A document was not found.");
        e.printStackTrace();
        System.exit(1);
      }
    });

    System.out.println("All docs were opened");

    // Now save the docs
    for (String key : mapDocs.keySet()){
      mapDocs.get(key).serializeDoc();
    }

    //Create document graphs

    Set<DocGraph> allDocGraphs = new HashSet<>();
    for (int j = 0; j <200; j++) {
      allDocGraphs.add(new DocGraph(0, i+1));
    }

    System.out.println("Generation of doc graphs is complete");
    // LOL this will also be a bottleneck. but leave it for now.

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

    // Above may be slow, but warranted; we may get thru it
    // save the graphs
    for (DocGraph doc : allDocGraphs){
      doc.serializeDocGraph();
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
        TRECEvalPath,
        goldenQrelsPath);

    fullTest.experimentFactory();

    System.out.println("Run completed!");
  }
}
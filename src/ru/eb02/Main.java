package ru.eb02;

import it.acubelab.tagme.Disambiguator;
import it.acubelab.tagme.RelatednessMeasure;
import it.acubelab.tagme.RhoMeasure;
import it.acubelab.tagme.Segmentation;
import it.acubelab.tagme.TagmeParser;
import it.acubelab.tagme.config.TagmeConfig;
import it.acubelab.tagme.preprocessing.TopicSearcher;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * (Currently) a sequentially based, entity finder for documents.
 */
public class Main {

  public static void main(String[] args) throws IOException {

    // Initiates the TAGME service with the configuration defined by me.
    TagmeConfig.init();
    String wikiLanguage = "en";
    RelatednessMeasure rel = RelatednessMeasure.create(wikiLanguage);
    TopicSearcher searcher = new TopicSearcher(wikiLanguage);
    TagmeParser parser = new TagmeParser(wikiLanguage, true);
    Disambiguator disamb = new Disambiguator(wikiLanguage);
    Segmentation segmentation = new Segmentation();
    RhoMeasure rho = new RhoMeasure();

    // TODO track of any failed docs? They will have null entities.

    // TODO: hardcoded directory!
    String documentDir = "/home/luis-zugasti/EB02 Document Corpus/clueweb09PoolFilesTest";
    // I wanna use ArrayList but my hand is forced with HashMap.
    // And turns out I'll just leave HashMap as is... whatever I figured out a way to prevent
    // duplicate edges that doesn't use this.
    HashMap<Doc, Integer> mapDocs = new HashMap<>();


    // Batch processing: Get all the entities.
    File folder = new File(documentDir);
    File[] docNameList = folder.listFiles();
    for(File file : docNameList) {
      if (file.isFile()) {
        String tmpFilePath = documentDir + "/" + file.getName();

        // docs will have their file names and stopword stripped text, added here.
        Doc temp = new Doc(FileTools.readFileUTF8(tmpFilePath, false), file.getName());

        // docs will then have their entities added here.
        temp.obtainEntities(wikiLanguage, rel, disamb, segmentation, rho, parser);
        mapDocs.put(temp, 1);
      }
    }

    // Batch processing: Compare each document to the other and build the graph.
    DocGraph finalGraph = new DocGraph(0.5);
    for (Doc documentA : mapDocs.keySet()) {
      for (Doc documentB : mapDocs.keySet()) {
        if (finalGraph.duplicateEdgeCheck(documentA.getDocName(), documentB.getDocName()) ||
            (documentA.getDocName()).equals(documentB.getDocName())) {
          // we are dealing with the same document OR about to add a duplicate edge,
          // which will raise an exception. Skip.
          break;
        }
        finalGraph.addTuple(documentA.getTopMap(), documentB.getTopMap(), documentA.getDocName(),
            documentB.getDocName(), rel);
      }
    }
  }
}

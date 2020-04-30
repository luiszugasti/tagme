package ru.eb02;

import it.acubelab.tagme.AnnotatedText;
import it.acubelab.tagme.Annotation;
import it.acubelab.tagme.Disambiguator;
import it.acubelab.tagme.RelatednessMeasure;
import it.acubelab.tagme.RhoMeasure;
import it.acubelab.tagme.Segmentation;
import it.acubelab.tagme.TagmeParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Doc {
  private final String strippedDocText;
  private final String docName;
  private HashMap<Integer,Integer> entityMap;
  private Map<Integer,Integer> topMap;

  /**
   * Constructor which assigns the docName to this instance and assigns the stripped text.
   */
  public Doc(String docText, String docName, String lang, RelatednessMeasure rel,
      Disambiguator disamb, Segmentation segmentation, RhoMeasure rho, TagmeParser parser) {
    Document document = Jsoup.parse(docText, "ASCII");
    this.docName = docName;
    strippedDocText = removeStopWords(document.text());
    this.obtainEntities(lang, rel, disamb, segmentation, rho, parser);
  }

  /**
   * alternate constructor
   */
  public Doc(HashMap<Integer, Integer> integerIntegerHashMap, String docName) {
    this.docName = docName;
    this.entityMap = integerIntegerHashMap;
    this.strippedDocText = "STUB - NOT USED";
    this.saveTopHits(5);
  }
  /**
   * For testing
   */
  public Doc(String docText, String docName) {
    Document document = Jsoup.parse(docText, "ASCII");
    this.docName = docName;
    strippedDocText = removeStopWords(document.text());
  }

  /**
   * Get Top Entities, as hardcoded
   * @return this.topMap
   */
  public Map<Integer, Integer> getTopMap() {
    return topMap;
  }

  /**
   * Get Stripped doc text
   * @return this.strippedDocText
   */
  public String getStrippedDocText() {
    return strippedDocText;
  }

  /**
   * Get document name
   * @return this.docName
   */
  public String getDocName() {
    return docName;
  }

  /**
   * Get this object's entity map from top n hits, and save it
   * @param topHits gets us *just* the top entity hits for this document.
   */
  private void saveTopHits(int topHits) {
    // copy pasta and I barely get it...
    // https://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values
    topMap =
        entityMap.entrySet().stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .limit(topHits)
            .collect(Collectors.toMap(
                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
  }

  /**
   * Helper method which removes the stop words from a string.
   */
  private static String removeStopWords(String str) {
    String stopWords = "!! ?! ?? !? ` `` '' -lrb- -rrb- -lsb- -rsb- , . : ; \" ' ? < > { } [ ] + - ( ) & % $ @ ! ^ # * .. ... 'll 's 'm a about above after again against all am an and any are aren't as at be because been before being below between both but by can can't cannot could couldn't did didn't do does doesn't doing don't down during each few for from further had hadn't has hasn't have haven't having he he'd he'll he's her here here's hers herself him himself his how how's i i'd i'll i'm i've if in into is isn't it it's its itself let's me more most mustn't my myself no nor not of off on once only or other ought our ours ourselves out over own same shan't she she'd she'll she's should shouldn't so some such than that that's the their theirs them themselves then there there's these they they'd they'll they're they've this those through to too under until up very was wasn't we we'd we'll we're we've were weren't what what's when when's where where's which while who who's whom why why's with won't would wouldn't you you'd you'll you're you've your yours yourself yourselves ### return arent cant couldnt didnt doesnt dont hadnt hasnt havent hes heres hows im isnt its lets mustnt shant shes shouldnt thats theres theyll theyre theyve wasnt were werent whats whens wheres whos whys wont wouldnt youd youll youre youve";
    String[] allWords = str.toLowerCase().split(" ");
    StringBuilder builder = new StringBuilder();
    for(String word : allWords) {
      if(!stopWords.contains(word)) {
        builder.append(word);
        builder.append(' ');
      }
    }
    // return the string with stop words removed.
    return builder.toString().trim();
  }

  /**
   * Instance method which queries the TAGME service to obtain entities of this doc.
   * Also includes call to saveTopHits
   */
  public void obtainEntities(String lang, RelatednessMeasure rel,
      Disambiguator disamb, Segmentation segmentation,
      RhoMeasure rho, TagmeParser parser) {
    AnnotatedText ann_text = new AnnotatedText(strippedDocText);

    parser.parse(ann_text);
    segmentation.segment(ann_text);
    disamb.disambiguate(ann_text, rel);
    rho.calc(ann_text, rel);

    List<Annotation> annots = ann_text.getAnnotations();
    HashMap<Integer, Integer> temp = new HashMap<>();

    // Build the annotation list for this document. Don't care about Rho.
    for (Annotation a : annots) {
      if (a.isDisambiguated() && a.getRho() >= 0.4) {
        if (!temp.containsKey(a.getTopic())){
          temp.put(a.getTopic(), 1);
        } else {
          temp.put(a.getTopic(), temp.get(a.getTopic()) + 1);
        }
      }
    }
    // Assign the Hashmap.
    entityMap = temp;

    // Get top hits.
    this.saveTopHits(5);
  }

  public void serializeDoc() {
    // Only care about the entityMap; nothing else
    ArrayList<String> output = new ArrayList<>();
    // get simple values
    for (Integer i : entityMap.keySet()) {
      output.add(i + " " + entityMap.get(i));
    }
    // print it
    FileTools.writeFile(output, "docs/" + docName + ".ser", null);
  }

  public static Doc deSerializeDoc(String docName) throws IOException {
    // instantiate a Doc with just hashmaps
    String[] results = FileTools.readFileUTF8("docs/" + docName /* ".ser"*/,
        true).split("\n");
    HashMap<Integer, Integer> hii = new HashMap<>();
    // go through each line to instantiate them
    for (String r : results) {
      String[] temp = r.split(" ");
      hii.put(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
    }
    return new Doc(hii, docName);
  }
}
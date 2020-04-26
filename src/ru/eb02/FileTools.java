package ru.eb02;

/*
 The file tools class is a collection of simple, static helper methods that allow interfacing
 with the current file system. Some methods are more specialized than others.

 A general usage of FileTools:
 1. Create your run folder with @createFilePath.
 2. Read the contents of the original TREC search results file into a trecTopic object using
    @openTrecSearchResultsFile.
 3. Read the contents of your baseline scores with @openTrecScoresFile and save it into a
    trecResult object.
 4. Perform your tests; anytime you need to run a TREC test, simply use one of the helper
    writer/reader methods. Also make sure to call trecEvaler which will return to you your
    results!
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileTools {
  public static final String GLOBALRESULTSPATH = "TagMe Engine Search results/";

  /**
   * Runs TREC eval and then returns its output.
   * @param goldenQrels path to Qrels (Correct answers)
   * @param resultEvalFile path to desired scores file to run against
   * @return String
   */
  public static ArrayList<String> trecEvaler(String goldenQrels, String resultEvalFile) {
    //Build command array
    String[] cmdArray = {"Tests/./trec_eval", goldenQrels, resultEvalFile};
    ArrayList<String> output = new ArrayList<>();

    // Run this command in the terminal
    try {
      Process p = Runtime.getRuntime().exec(cmdArray);
      InputStream stdOut = p.getInputStream();
      p.getErrorStream(); // I don't use it at all but may as well consume it
      InputStreamReader isr = new InputStreamReader(stdOut);
      BufferedReader br = new BufferedReader(isr);
      String line;
      while ( (line = br.readLine()) != null)
        output.add(line);
      int exitVal = p.waitFor();
      System.out.println("Process exitValue: " + exitVal);
    } catch (Throwable t)
    {
      t.printStackTrace();
    }
    return output;
  }
  /**
   * Opens a file in ASCII encoding.
   * @param filePath The filepath specified absolutely.
   * @return A single string containing all of the file contents.
   * @throws IOException In case the file specified is not found.
   */
  public static String readFileASCII(String filePath) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(filePath));

    // Empty string builder
    StringBuilder fileText = new StringBuilder();
    try {
      String line = br.readLine();

      while (line != null) {

        // Matches ascii chars. questions/5071236/java-regexp-to-match-ascii-characters
        if (line.matches("^[\\u0000-\\u007F]*$")) {
          fileText.append(line);
        }
        line = br.readLine();
      }
    } finally {
      br.close();
    }
    return fileText.toString();
  }

  /**
   * Opens a file in UTF8 encoding. Reference:
   * https://stackoverflow.com/questions/4964640/reading-inputstream-as-utf-8
   * @param filePath The filepath specified absolutely.
   * @param newLine A boolean flag that determines whether /n characters are appended
   *                at the end of lines, or not
   * @return A single string containing all of the file contents.
   * @throws IOException In case the file specified is not found.
   */
  public static String readFileUTF8(String filePath, boolean newLine) throws IOException {
    Charset UTF8 = StandardCharsets.UTF_8;
    InputStream input = new FileInputStream(filePath);
    BufferedReader br = new BufferedReader(new InputStreamReader(input, UTF8));
    StringBuilder fileText = new StringBuilder();
    String line;
    try {
      if (newLine) {
        while ((line = br.readLine()) != null) {
          fileText.append(line);
          fileText.append("\n");
        }
      } else {
        while ((line = br.readLine()) != null) {
          fileText.append(line);
        }
      }
    } finally {
      br.close();
    }
    return fileText.toString();
  }

  /**
   * A specialised method that opens a TREC formatted search results file, and fills the relevant
   * information into (at most) 200 TREC topics.
   * This method
   * @param filePath The filepath specified absolutely.
   * @return A trecTopic array consisting of all the TREC topics, their documents, scores, and
   *         rankings inherent in the trec data structure.
   * @throws IOException In case the file specified is not found.
   */
  public static trecTopic[] openTrecSearchResultsFile(String filePath) throws IOException {
    String trecResults = readFileUTF8(filePath, true);
    String[] lines = trecResults.split("\n");
    // 200 topics.. hardcoded.
    trecTopic[] trecTopics = new trecTopic[200];
    for (int i = 0; i < 200; i++) {
      trecTopics[i] =
          new trecTopic(i+1);
    }

    String[] lineInfo;

    for (String line : lines) {
      lineInfo = line.split("\\s+");
      trecTopics[Integer.parseInt(lineInfo[0]) - 1].addDocument(
          lineInfo[2],
          Double.parseDouble(lineInfo[4])
      );
    }
    return trecTopics;
    }

  /**
   * Opens a TREC scores text and creates a trecResult object.
   * It's pretty inefficient.
   * @param scoresText Text to de-serialize.
   * @return a trecResult object, containing all pertinent data for usage.
   */
  public static trecResult openTrecScoresFile(String scoresText) {
    scoresText = scoresText.replace("\n", " ");
    scoresText = scoresText.replaceAll("\t+", " ");
    scoresText = scoresText.replaceAll(" +", " ");
    String[] scores = scoresText.split(" ");
    trecResult tr = new trecResult(scores[2],
        Integer.parseInt(scores[5]),
        Integer.parseInt(scores[8]),
        Integer.parseInt(scores[11]),
        Integer.parseInt(scores[14]),
        Double.parseDouble(scores[17]),
        Double.parseDouble(scores[20]),
        Double.parseDouble(scores[23]),
        Double.parseDouble(scores[26]),
        Double.parseDouble(scores[29]),
        Double.parseDouble(scores[32]),
        Double.parseDouble(scores[35]),
        Double.parseDouble(scores[38]),
        Double.parseDouble(scores[41]),
        Double.parseDouble(scores[44]),
        Double.parseDouble(scores[47]),
        Double.parseDouble(scores[50]),
        Double.parseDouble(scores[53]),
        Double.parseDouble(scores[56]),
        Double.parseDouble(scores[59]),
        Double.parseDouble(scores[62]),
        Double.parseDouble(scores[65]),
        Double.parseDouble(scores[68]),
        Double.parseDouble(scores[71]),
        Double.parseDouble(scores[74]),
        Double.parseDouble(scores[77]),
        Double.parseDouble(scores[80]),
        Double.parseDouble(scores[83]),
        Double.parseDouble(scores[86]),
        Double.parseDouble(scores[89]),
        true
    );
    return tr;
  }

  /**
   * Writes a TREC search result file to a specified location and appends relevant run information
   * to the name of the file. As far as I know, this process is essential in order for comparing a
   * TREC run with TREC eval executable.
   * TODO: Specify a hashing algorithm to make matching values easier? But will that be necessary?
   * @param results The results of the sorted trecTopics, as an array of trecTopic objects.
   * @param runName The name of this run of tests.
   * @param lambda1 The value of lambda1. (High coupling with my generic algorithms!)
   * @param lambda2 The value of lambda2.
   * @param centCutoff The value for the centrality cutoff.
   */
  public static void writeTrecSearchResultsFile(String filePath, trecTopic[] results,
      String runName, String lambda1, String lambda2, String centCutoff) {
    ArrayList<String> output = new ArrayList<>();

    // Call toString method to get properly formatted output.
    for (trecTopic result : results) {
      output.add(result.toString());
    }

    // Send payload to this run's folder.
    writeFile(output, fileNameSchema(GLOBALRESULTSPATH + runName, lambda1,
        lambda2, centCutoff), null);
  }

  /**
   * Writes a TREC scores result file to a specified location. Refer to writeTrecSearchResultsFile
   * for more info.
   * Having this information on file allows us to keep a set of scores from TREC eval as well as
   * read from it for results. Inferred usage:
   * -  write a TREC search result file.
   * -  run TREC eval against this.
   * -  read TREC eval against this using the openUTF8 method, obtain the string as desired
   * and send it to this method. This method is actually just a file writer but keep it our little
   * secret??
   * @param text text to write as a TrecScoresFile.
   * @param runName The name of this run of tests.
   * @param lambda1 The value of lambda1.
   * @param lambda2 The value of lambda2.
   * @param centCutoff The value for the centrality cutoff.
   * @param score This run's valuable score (can assume it's MAP).
   */
  public static void writeTrecScoresFile(String text, String runName, String lambda1,
      String lambda2, String centCutoff, Double score) {
    ArrayList<String> output = new ArrayList<>();
    output.add(text);

    // Send payload to newly created folder.
    writeFile(output, fileNameSchema(GLOBALRESULTSPATH + runName, lambda1,
        lambda2, centCutoff, score), null);
  }

  /**
   * createFilePath simply creates a path for runs if the path does not exist at all.
   * @param runName The name of this run of tests.
   * @throws IllegalArgumentException in the case the path already exists.
   */
  public static void createFilePath (String runName) {
    if (!(new File(GLOBALRESULTSPATH + runName).mkdirs())) throw new
        IllegalArgumentException("path for: " + runName + " already exists and may contain"
        + "files.");
  }

  /**
   * Simply writes a file specified in the filePath.
   * @param contents List of contents, which will be written to line by line.
   * @param filePath Path of the file to write.
   * @param charset Specified character set to write the file. Default UTF-8.
   */
  private static void writeFile(List<String> contents, String filePath, Charset charset) {
    if (charset == null) charset = StandardCharsets.UTF_8;

    try {
      Files.write(Paths.get(filePath), contents, charset);
    } catch (IOException ex) {
      ex.printStackTrace();
    }

  }

  /**
   * Refer to fileNameSchema, this method writes a TREC Search Results file.
   * @param runName The name of this run of tests.
   * @param lambda1 The value of lambda1.
   * @param lambda2 The value of lambda2.
   * @param centCutoff The value for the centrality cutoff.
   */
  private static String fileNameSchema(String runName, String lambda1, String lambda2,
      String centCutoff) {

    return "run- "
        + runName
        + ", l1- "
        + lambda1
        + ", l2- "
        + lambda2
        + ", cc- "
        + centCutoff
        + ".txt";
  }

  /**
   * Helper method to create consistent file names. This method will write a consistent file
   * name to match a TREC result file with its Eval sibling.
   * Just a little note for me: If score is provided, then we know that this is printing out a
   * Scores file.
   * @param runName The name of this run of tests.
   * @param lambda1 The value of lambda1.
   * @param lambda2 The value of lambda2.
   * @param centCutoff The value for the centrality cutoff.
   * @param score Score is supplied when we are working with a TREC Scores file.
   */
  private static String fileNameSchema(String runName, String lambda1, String lambda2,
      String centCutoff, double score) {

    return "run- "
        + runName
        + ", score- "
        + score
        + ", l1- "
        + lambda1
        + ", l2- "
        + lambda2
        + ", cc- "
        + centCutoff
        + ".txt";
  }

  /**
   * Reads and returns a Double[] array from a gridSearchParams file.
   * File format can be found in the sample testRunCentralityValues.txt file in the directory.
   * Desirable naming format:
   * -  testRunCentralityValues.txt - values for centrality cutoff.
   *    testRunLambdaValues.txt - values for lambda values.
   * @param filePath path to selected gridSearchParams file.
   * @return A Double[] array of gridSearchParams.
   * @throws IOException in case the file is not found.
   */
  private static Double[] readSelectedGridSearchParameters(String filePath) throws IOException {
    String[] lines = (readFileUTF8(filePath, true)).split("\n");
    List<Double> gridSearchParams = new ArrayList<>();

    for (String line : lines)
      gridSearchParams.add(Double.parseDouble(line));

    return gridSearchParams.toArray(new Double[gridSearchParams.size()]);
  }
  public static void main(String[] args) throws IOException {
    // Simple and manual test of the output of each of the file readers.
    String fileDir = "/home/luis-zugasti/IdeaProjects/tagme-luis/result_TF_IDF.txt";
    String ASCII = readFileASCII(fileDir);
    String UTF8 = readFileUTF8(fileDir, true);
    trecTopic[] opentest = openTrecSearchResultsFile(fileDir);

    String trecResultFileDir = "/home/luis-zugasti/IdeaProjects/tagme-luis/Luis_trec_results.txt";
    trecResult tr = openTrecScoresFile(trecResultFileDir);
    System.out.println(tr.toString());
    System.out.println("Testing completed.");
  }
}

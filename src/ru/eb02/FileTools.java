package ru.eb02;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FileTools {

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

  // https://stackoverflow.com/questions/4964640/reading-inputstream-as-utf-8
  // I have read into this... so many imports! I assume it will work...
  // No official unit tests BUT I was able to test this and it does open UTF8 nicely.
  // I vouch for it. Wayyy better than what any unit test will give you.
  // Theres some unit testing at the bottom. Not Junit... but good enough for me.
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

  public static trecTopic[] openTrecEvalFile(String filePath) throws IOException {
    String trecResults = readFileUTF8(filePath, true);
    // TODO: Regex
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
   * It's pretty inefficient.
   * @param filePath is the fully qualified path to the trec results file.
   * @return a trecResult object, containing all pertinent data for usage.
   * @throws IOException In case the correct file is not found.
   */
  public static trecResult openTrecResultsFile(String filePath) throws IOException {
    String trecResults = readFileUTF8(filePath, true);
    trecResults = trecResults.replace("\n", " ");
    trecResults = trecResults.replaceAll("\t+", " ");
    trecResults = trecResults.replaceAll(" +", " ");
    String[] trecResultsFile = trecResults.split(" ");
    trecResult tr = new trecResult(trecResultsFile[2],
        Integer.parseInt(trecResultsFile[5]),
        Integer.parseInt(trecResultsFile[8]),
        Integer.parseInt(trecResultsFile[11]),
        Integer.parseInt(trecResultsFile[14]),
        Double.parseDouble(trecResultsFile[17]),
        Double.parseDouble(trecResultsFile[20]),
        Double.parseDouble(trecResultsFile[23]),
        Double.parseDouble(trecResultsFile[26]),
        Double.parseDouble(trecResultsFile[29]),
        Double.parseDouble(trecResultsFile[32]),
        Double.parseDouble(trecResultsFile[35]),
        Double.parseDouble(trecResultsFile[38]),
        Double.parseDouble(trecResultsFile[41]),
        Double.parseDouble(trecResultsFile[44]),
        Double.parseDouble(trecResultsFile[47]),
        Double.parseDouble(trecResultsFile[50]),
        Double.parseDouble(trecResultsFile[53]),
        Double.parseDouble(trecResultsFile[56]),
        Double.parseDouble(trecResultsFile[59]),
        Double.parseDouble(trecResultsFile[62]),
        Double.parseDouble(trecResultsFile[65]),
        Double.parseDouble(trecResultsFile[68]),
        Double.parseDouble(trecResultsFile[71]),
        Double.parseDouble(trecResultsFile[74]),
        Double.parseDouble(trecResultsFile[77]),
        Double.parseDouble(trecResultsFile[80]),
        Double.parseDouble(trecResultsFile[83]),
        Double.parseDouble(trecResultsFile[86]),
        Double.parseDouble(trecResultsFile[89]),
        true
    );
    return tr;
  }

  public static void writeTrecResult(trecTopic[] results) {
    StringBuilder output = new StringBuilder();

  }
  public static void main(String[] args) throws IOException {
    // simple test of the output of each of the file readers.
    String fileDir = "/home/luis-zugasti/IdeaProjects/tagme-luis/result_TF_IDF.txt";
    String ASCII = readFileASCII(fileDir);
    String UTF8 = readFileUTF8(fileDir, true);
    trecTopic[] opentest = openTrecEvalFile(fileDir);

    String trecResultFileDir = "/home/luis-zugasti/IdeaProjects/tagme-luis/Luis_trec_results.txt";
    trecResult tr = openTrecResultsFile(trecResultFileDir);
    System.out.println(tr.toString());
    System.out.println("Testing completed.");
  }
}

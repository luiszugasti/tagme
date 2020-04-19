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
          Integer.parseInt(lineInfo[3]),
          Double.parseDouble(lineInfo[4])
      );
      }
    return trecTopics;
    }

  public static void main(String[] args) throws IOException {
    // simple test of the output of each of the file readers.
    String fileDir = "/home/luis-zugasti/IdeaProjects/tagme-luis/result_TF_IDF.txt";
    String ASCII = readFileASCII(fileDir);
    String UTF8 = readFileUTF8(fileDir, true);
    trecTopic[] test = openTrecEvalFile(fileDir);

    System.out.println("Testing completed.");
  }
}

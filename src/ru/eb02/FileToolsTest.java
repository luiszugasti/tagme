package ru.eb02;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import org.apache.commons.lang.NullArgumentException;
import org.junit.jupiter.api.Test;

class FileToolsTest {

  @Test
  void readFileASCII() {
  }

  @Test
  void readFileUTF8() {
  }

  @Test
  void openTrecSearchResultsFile() {
  }

  @Test
  void openTrecScoresFile() {
  }

  @Test
  void writeTrecSearchResultsFile() {
  }

  @Test
  void writeTrecScoresFile() {
  }

  @Test
  void main() {
  }

  @Test
  void createFilePath() {
    // test that we can indeed create a directory
    String test = "testRun";
    FileTools.createFilePath(test);

    // test that, if the previous run created a directory, we should get an exception.
    assertThrows(IllegalArgumentException.class, () -> {
      FileTools.createFilePath(test);
    });
  }

  @Test
  void trecEvaler() {
    ArrayList<String> tst = FileTools.trecEvaler("Tests/Baseline/golden_qrels.test", "TagMe_Engine_Search_results/testRun/testRun.test");
    for (String str : tst)
      System.out.println(str);
  }
}
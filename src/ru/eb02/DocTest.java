package ru.eb02;

import static org.junit.jupiter.api.Assertions.*;

import it.unimi.dsi.fastutil.Hash;
import java.io.IOException;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

class DocTest {

  @Test
  void serializeTest() {
    HashMap<Integer, Integer> test = new HashMap<>();
    test.put(123123123, 123123123);
    test.put(12312123, 123123123);
    test.put(12123123, 123123123);
    test.put(12323123, 123123123);
    test.put(23123123, 123123123);

    Doc tester = new Doc(test, "docname");

    tester.serializeDoc();
  }

  @Test
  void deSerializeTest() throws IOException {
    Doc eyy = Doc.deSerializeDoc("docname");
    System.out.println("testing completed");
  }

}
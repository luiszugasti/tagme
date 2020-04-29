package ru.eb02;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import org.apache.commons.lang.NullArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class trecTopicTest {
  final int TREC_TOPIC = 7;

  @Test
  void addDocuments() {
    trecTopic tr = new trecTopic(TREC_TOPIC);
    // Simulate insertion as filetools will insert!
    tr.addDocument("clueweb09-88-02", 1);
    tr.addDocument("clueweb09-88-22", 0.9);
    tr.addDocument("clueweb09-88-92", 0.8);
    tr.addDocument("clueweb09-85-92", 0.7);
    tr.addDocument("clueweb09-82-92", 0.6);
    tr.addDocument("clueweb09-78-92", 0.5);
    tr.addDocument("clueweb09-48-92", 0.4);
    tr.addDocument("clueweb09-18-92", 0.3);
    tr.addDocument("clueweb09-88-52", 0.2);
    tr.addDocument("clueweb09-88-42", 0.1);

    // Simple. See if the size is correct.
    assertEquals(10, tr.size());

    // Simple. See if we get our exception.
    assertThrows(NoSuchElementException.class, () -> {
      tr.getRank("This is not a docName that's present");
    });

    // See what happens when adding an empty doc...
    assertThrows(NullArgumentException.class, () -> {
      tr.addDocument(null, 0.223);
    });

    // Is the order maintained after insertion? (that's the expected behavior)
    assertEquals(6, tr.getRank("clueweb09-78-92"));

    HashMap<String, Double> updater = new HashMap<>();
    updater.put("clueweb09-88-92", 0.5);
    updater.put("clueweb09-85-92", 0.5);
    updater.put("clueweb09-82-92", 0.5);
    updater.put("clueweb09-78-92", 0.5);
    updater.put("clueweb09-48-92", 0.5);
    updater.put("clueweb09-18-92", 0.5);
    updater.put("clueweb09-88-52", 0.5);
    updater.put("clueweb09-88-42", 0.5);
    updater.put("clueweb09-88-22", 0.5);

    final HashMap<String, Double> updaterTest = updater;

    ArrayList<Tuple> tester = tr.updateRanks(updaterTest, 0);
    // Quick check to see if this works.
    assertEquals(1.0,tester.get(0).getValue());
    assertEquals(0.55,tester.get(6).getValue());
  }

  @Test
  void addOverTwoHundredDocuments() {
    trecTopic tr = new trecTopic(TREC_TOPIC);
    for (int i = 0; i < 300; i++) {
      tr.addDocument(i + 1 + "tst", 1- (i/1000.0));
    }

    // Always make sure to review this method call if changes are made.
    System.out.print(tr.toString(null));
    System.out.println("Testing complete.");
  }

  @Test
  void addOverTwoHundredDocumentsAndReRank() {
    trecTopic tr = new trecTopic(TREC_TOPIC);
    HashMap<String, Double> th = new HashMap<>();
    HashMap<String, Double> to = new HashMap<>();
    for (int i = 0; i < 11100; i++) {
      tr.addDocument(i + 1 + "tst", 6 - (i*3 / 1000.0));
      // It doesn't matter if I put more docs in hashmap - we will only check top 200.
      th.put(i + 1 + "tst", (i / 1000.0));
    }
    ArrayList<Tuple> ta = tr.updateRanks(th, 0);
    System.out.println("Testing complete.");

//    System.out.println(tr.toString(ta));

    ArrayList<Tuple> tb = tr.updateRanks(to, 0);
    ArrayList<Tuple> tt = tr.updateRanks(to, 1);
    for (int i = 0; i < 200; i ++) {
      assertEquals(tb.get(i).getValue(), tt.get(i).getValue());
      assertEquals(tb.get(i).getKey(), tt.get(i).getKey());
    }
    System.out.println(tr.toString(tb));
  }
}
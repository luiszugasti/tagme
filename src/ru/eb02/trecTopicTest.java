package ru.eb02;

import static org.junit.jupiter.api.Assertions.*;

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
    tr.addDocument("clueweb09-88-92", 0.1);
    tr.addDocument("clueweb09-85-92", 0.2);
    tr.addDocument("clueweb09-82-92", 0.3);
    tr.addDocument("clueweb09-78-92", 0.4);
    tr.addDocument("clueweb09-48-92", 0.5);
    tr.addDocument("clueweb09-18-92", 0.6);
    tr.addDocument("clueweb09-88-52", 0.7);
    tr.addDocument("clueweb09-88-42", 0.8);
    tr.addDocument("clueweb09-88-22", 0.9);
    tr.addDocument("clueweb09-88-02", 1);

    // Simple. See if the size is correct.
    assertEquals(10, tr.size() );

    // Simple. See if we get our exception.
    assertThrows(NoSuchElementException.class, () -> {
      tr.getRank("This is not a docName that's present");
    });

    // See what happens when adding an empty doc...
    assertThrows(NullArgumentException.class, () -> {
      tr.addDocument(null, 0.223);
    });

    // Is the order maintained after insertion? (that's the expected behavior)
    assertEquals(6, tr.getRank("clueweb09-18-92"));

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

    // Quick check to see if this works.
    assertThrows(IllegalArgumentException.class, () -> {
      tr.updateRanks(updaterTest, 0);
        });

    // TODO: Update this test
    updater.put("clueweb09-88-02", 0.5);
    tr.updateRanks(updaterTest, 0.5);

    // when they've all been set to 0.5, all should keep their rank.
    assertEquals(6, tr.getRank("clueweb09-18-92"));

    // and now, when I update them to get a completely different order, they should
    // maintain that order.

    updater = null;
    assertThrows(NullArgumentException.class, () -> {
      tr.updateRanks(updaterTest, 0.223);
    });


    updater = new HashMap<>();
    updater.put("clueweb09-88-92", 1D);
    updater.put("clueweb09-85-92", 0.9);
    updater.put("clueweb09-82-92", 0.8);
    updater.put("clueweb09-78-92", 0.7);
    updater.put("clueweb09-48-92", 0.6);
    updater.put("clueweb09-18-92", 0.5);
    updater.put("clueweb09-88-52", 0.4);
    updater.put("clueweb09-88-42", 0.3);
    updater.put("clueweb09-88-22", 0.2);
    updater.put("clueweb09-88-02", 0.1);



  }

}
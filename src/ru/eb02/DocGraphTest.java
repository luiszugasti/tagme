package ru.eb02;

import static org.junit.jupiter.api.Assertions.*;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.event.GraphEvent.Vertex;
import edu.uci.ics.jung.graph.util.EdgeType;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;
import org.junit.jupiter.api.Test;

class DocGraphTest {

  @Test
  void serialize() {
    DocGraph testGraph = new DocGraph(0, 9);
    testGraph.addEdge(0.3, "test2", "test1");
    testGraph.serializeDocGraph();
  }

  @Test
  void deserialize() throws IOException {
    DocGraph testGraph = DocGraph.deSerializeDocGraph(9);
    DocGraph testGraphCompare = new DocGraph(0, 9);
    testGraphCompare.addEdge(0.3, "test2", "test1");
    System.out.println("testing complete");
  }

  @Test
  void testDocGraphMethods() {
    // Creation
    DocGraph testGraph = new DocGraph(0, 9);

    // Add vertices (there is no actual need to add vertices though!)
    // testGraph.addVertex("test1");
    // testGraph.addVertex("test2");
    // testGraph.addVertex("test3");
    // testGraph.addVertex("test4");

    // Add edges (no issues to create)
    testGraph.addEdge(0.3, "test2", "test1");
//    testGraph.addEdge(0.4, "test3", "test1");
//    testGraph.addEdge(0.5, "test4", "test1");
//    testGraph.addEdge(0.6, "test2", "test3");
    testGraph.addEdge(0.7, "test23", "test4");
    testGraph.addEdge(0.8, "test32", "test23");
    testGraph.addEdge(0.1, "test33", "test24");
    testGraph.addEdge(0.2, "test234", "test24");
    testGraph.addEdge(0.83, "test35", "test24");
    testGraph.addEdge(0.812, "test36", "test24");
    testGraph.addEdge(0.721, "test27", "test24");
    testGraph.addEdge(0.812, "test38", "test24");
    testGraph.addEdge(0.12, "test83", "test24");
    testGraph.addEdge(0.343, "test23", "test24");
    testGraph.addEdge(0.6543, "test34", "test24");
    testGraph.addEdge(0.23, "test31", "test24");
    testGraph.addEdge(0.423, "test214", "test24");
    testGraph.addEdge(0.52, "test37", "test24");
    testGraph.addEdge(0.5456, "test31", "test24");

    //how does computeCentralityWork?
    testGraph.computeCentrality(0.7);

    // Add an edge (to get an issue)
    boolean edgeShouldNotBeAdded =
        testGraph.addEdge(0.3, "test2", "test1");
    boolean edgeShouldNotBeAdded2 =
        testGraph.addEdge(0.9, "test2", "test1");
    boolean edgeShouldNotBeAdded3 =
        testGraph.addEdge(0.3, "test1", "test2");
    boolean edgeShouldNotBeAdded4 =
        testGraph.addEdge(0.1, "test1", "test2");

    // Test remove edges (all edges that are below weight 0.7)
    // First, test if we can do a deep copy of graphs (THIS DOES NOT DO A DEEP COPY)
    DocGraph testGraph2 = testGraph;
    boolean edgeShouldNotBeAddedToOriginalGraph =
        testGraph2.addEdge(0.6, "test3", "test1");


    // Add a vertex (to get an issue)
    boolean vertexShouldNotBeAdded =
        testGraph.addVertex("test1");

    assertFalse(edgeShouldNotBeAdded);
    assertFalse(edgeShouldNotBeAdded2);
    assertFalse(edgeShouldNotBeAdded3);
    assertFalse(edgeShouldNotBeAdded4);
    assertFalse(vertexShouldNotBeAdded);

    System.out.println("Testing completed");

  }

  @Test
  void testJUNGFunctionality() {

    // I won't touch the DocGraph per se - at least not here, because I don't have
    // TAGME running.
    // I will check JUNG graphs in general though.
    Graph<SimpleVertex, SimpleEdge> g = new UndirectedSparseGraph<>();
    String repetitive = "simplevertex4";

    // Vertex creation
    SimpleVertex n1 = new SimpleVertex("simplevertex1");
    SimpleVertex n2 = new SimpleVertex("simplevertex2");
    SimpleVertex n3 = new SimpleVertex("simplevertex3");
    SimpleVertex n4 = new SimpleVertex(repetitive);
    SimpleVertex n5 = new SimpleVertex("simplevertex5");

    // add them to the graph, of course!
    g.addVertex(n1);
    g.addVertex(n2);
    g.addVertex(n3);
    g.addVertex(n4);
    g.addVertex(n5);

    // Test how adding vertices works
    boolean graphDoesContainVertexN1 = g.containsVertex(new SimpleVertex(repetitive));
    boolean graphDoesNotContainThisVertex = g.containsVertex(new SimpleVertex("troll"));
    boolean graphMustNotAddThisVertex = g.addVertex(new SimpleVertex(repetitive));

    assertTrue(graphDoesContainVertexN1);
    assertFalse(graphDoesNotContainThisVertex);
    assertFalse(graphMustNotAddThisVertex);

    // Edge creation
    SimpleEdge e2 = new SimpleEdge(n2.toString(), n1.toString(), 48);
    SimpleEdge e3 = new SimpleEdge(n3.toString(), n1.toString(), 19);
    SimpleEdge e4 = new SimpleEdge(n4.toString(), n1.toString(), 48);
    SimpleEdge e5 = new SimpleEdge(n5.toString(), n1.toString(), 48);

    // JUNG quietly will not add edges. nice. but annoying.
    // This is HOW WE MUST ADD EDGES!
    g.addEdge(e2, n2, n1, EdgeType.UNDIRECTED);
    g.addEdge(e3, n3, n1, EdgeType.UNDIRECTED);
    g.addEdge(e4, n4, n1, EdgeType.UNDIRECTED);
    g.addEdge(e5, n5, n1, EdgeType.UNDIRECTED);

    SimpleEdge testEdgeExist = g.findEdge(n1, n2);

    // test1, 2 and 3 **should** be the same
    SimpleEdge test1 = new SimpleEdge(n5.toString(), n1.toString(), 48);
    SimpleEdge test2 = new SimpleEdge(n1.toString(), n5.toString(), 48);
    SimpleEdge test3 = new SimpleEdge(n1.toString(), n5.toString(), 4);
    boolean test1EqualsTest2 = test1.equals(test2);
    boolean test1EqualsTest3 = test1.equals(test3);

    boolean containsEdge = g.containsEdge(test1);
    boolean containsEdge2 = g.containsEdge(new SimpleEdge(n1.toString(), n5.toString(), 1));
    boolean containsEdge3 = g.containsEdge(new SimpleEdge(n1.toString(), n5.toString(), 48));

    // All edges
    assertTrue(containsEdge);
    assertTrue(containsEdge2);
    assertTrue(containsEdge3);
    assertThrows(IllegalArgumentException.class, () ->
    {
      g.addEdge(new SimpleEdge(n1.toString(), n5.toString(), 48), n1, n5, EdgeType.UNDIRECTED);
    });

    System.out.println("Testing complete");
  }
}
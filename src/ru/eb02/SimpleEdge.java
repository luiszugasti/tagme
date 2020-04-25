package ru.eb02;

import java.util.Objects;
/**
 * A simple edge implementation. Created because JUNG does not readily work
 * with simple types.
 */
public class SimpleEdge {

  private final double weight;
  private final String vertex1;
  private final String vertex2;

  public SimpleEdge(String i, String j, double weight) {
    if (i.equals(j)) throw new IllegalArgumentException("Vertices provided are one and the same.");
    // We always sort i, j according to lexical order. That way, when comparing via hashcode,
    // we get consistent results.
    boolean order = (0 > i.compareTo(j));
    if (order) {
      vertex1 = i;
      vertex2 = j;
    } else {
      vertex1 = j;
      vertex2 = i;
    }
    this.weight = weight;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SimpleEdge mySimpleEdge = (SimpleEdge) o;
    return vertex1.equals(mySimpleEdge.vertex1)
      && vertex2.equals(mySimpleEdge.vertex2);
  }

  @Override
  public int hashCode() {

    return Objects.hash(vertex1, vertex2); // ID is all I care about.
  }

  @Override
  public String toString() {
    return vertex1+ " " + vertex2;
  }

  public double getWeight() {
    return weight;
  }

  public String getVertex1() {
    return vertex1;
  }

  public String getVertex2() {
    return vertex2;
  }
}

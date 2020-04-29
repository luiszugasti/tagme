package ru.eb02;

import java.util.Objects;

/**
 * A simple vertex implementation. Created because JUNG does not readily work
 * with simple types.
 */
public class SimpleVertex {
  private final String id;

  public SimpleVertex(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SimpleVertex that = (SimpleVertex) o;
    return getId().equals(that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

  @Override
  public String toString() {
    return id;
  }
}

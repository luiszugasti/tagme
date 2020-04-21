package ru.eb02;

/**
 * Simple implementation of a tuple - does not even go as far as a tuple.
 * No immutability. Just keeping two data types linked.
 */
public class Tuple {
  private final String stringKey;
  private double doubleValue;

  public Tuple (String key, double value) {
    stringKey = key;
    doubleValue = value;
  }

  public String getKey() {
    return stringKey;
  }

  public double getValue() {
    return doubleValue;
  }

  public void setValue(double doubleValue) {
    this.doubleValue = doubleValue;
  }
}

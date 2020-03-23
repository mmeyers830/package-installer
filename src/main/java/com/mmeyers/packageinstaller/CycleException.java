package com.mmeyers.packageinstaller;

public class CycleException extends Exception {
  public CycleException() {
    super("cycle detected in graph");
  }
}

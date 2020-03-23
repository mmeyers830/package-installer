package com.mmeyers.packageinstaller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PackageInstaller {
  public void install(List<String> input) {
    // parse input to adjList
    // detect if cycle is present
    // DFS to figure out install order
    // print install order
  }

  public Map<String, String> parseInput(List<String> input) {
    Map<String, String> parsed = new HashMap<>();

    return parsed;
  }



  public boolean isCyclic(Map<String, String> adjList) {

    return false;
  }

  public List<String> getInstallOrder(Map<String, String> adjList) {
    List<String> installOrder = new LinkedList<>();


    return installOrder;
  }

  public void print(List<String> installOrder) {

  }
}

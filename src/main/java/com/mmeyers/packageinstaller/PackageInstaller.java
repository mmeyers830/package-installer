package com.mmeyers.packageinstaller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PackageInstaller {
  private Map<String, String> visitedBy = new HashMap<>();

  public String install(List<String> input) throws CycleException {
    Map<String, String> adjList = parseInput(input);
    List<String> installOrder = getInstallOrder(adjList);
    return format(installOrder);
  }

  public Map<String, String> parseInput(List<String> input) {
    Map<String, String> parsed = new HashMap<>();
    for (String str : input) {
      String[] split = str.split(": ");
      // if there are no deps, we just skip it
      if (split.length < 2) {
        continue;
      }
      parsed.put(split[0], split[1]);
    }

    return parsed;
  }

  public List<String> getInstallOrder(Map<String, String> adjList) throws CycleException {
    List<String> installOrder = new LinkedList<>();
    for (Map.Entry<String, String> entry : adjList.entrySet()) {
      installOrder.addAll(getInstallOrderRecursive(adjList, new LinkedList<>(), entry.getKey(), entry.getKey()));
    }
    return installOrder;
  }

  public List<String> getInstallOrderRecursive(Map<String, String> adjList, List<String> installOrder, String current, String cycleStart) throws CycleException {
    // If we've already visited, we want to either return the current order, or if we've visited it
    // in our current cycle, then we throw a CycleException
    if (visitedBy.containsKey(current)) {
      if (visitedBy.get(current).equals(cycleStart)) {
        throw new CycleException();
      }
      return installOrder;
    }
    visitedBy.put(current, cycleStart);

    List<String> order = new LinkedList<>(installOrder);
    // check if there's anything more in our graph, if so, then keep stepping in
    if (adjList.containsKey(current)) {
      order = getInstallOrderRecursive(adjList, order, adjList.get(current), cycleStart);
    }

    // on our way back out of the recursion, we add the current node to our order
    order.add(current);
    return order;
  }

  public String format(List<String> installOrder) {
    return String.join(", ", installOrder);
  }
}

package com.mmeyers.packageinstaller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PackageInstallerTest {
  private PackageInstaller installer;

  @Before
  public void init() {
    installer = new PackageInstaller();
  }

  @Test
  public void testBasicInput() throws Exception {
    List<String> input = new LinkedList<>();
    input.add("KittenService: CamelCaser");
    input.add("CamelCaser: ");

    Map<String, String> expected = new HashMap<>();
    expected.put("KittenService", "CamelCaser");


    Map<String, String> actual = installer.parseInput(input);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testLongInput() throws Exception {
    List<String> input = new LinkedList<>();
    input.add("KittenService: ");
    input.add("Leetmeme: Cyberportal");
    input.add("Cyberportal: Ice");
    input.add("CamelCaser: KittenService");
    input.add("Fraudstream: Leetmeme");
    input.add("Ice: ");

    Map<String, String> expected = new HashMap<>();
    expected.put("Leetmeme", "Cyberportal");
    expected.put("Cyberportal", "Ice");
    expected.put("CamelCaser", "KittenService");
    expected.put("Fraudstream", "Leetmeme");

    Map<String, String> actual = installer.parseInput(input);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testParseCyclicInput() throws Exception {
    List<String> input = new LinkedList<>();
    input.add("KittenService: ");
    input.add("Leetmeme: Cyberportal");
    input.add("Cyberportal: Ice");
    input.add("CamelCaser: KittenService");
    input.add("Fraudstream: ");
    input.add("Ice: Leetmeme");

    Map<String, String> expected = new HashMap<>();
    expected.put("Leetmeme", "Cyberportal");
    expected.put("Cyberportal", "Ice");
    expected.put("CamelCaser", "KittenService");
    expected.put("Ice", "Leetmeme");

    Map<String, String> actual = installer.parseInput(input);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetInstallOrderSuccess() throws Exception {
    Map<String, String> adjList = new HashMap<>();
    adjList.put("Leetmeme", "Cyberportal");
    adjList.put("Cyberportal", "Ice");
    adjList.put("CamelCaser", "KittenService");
    adjList.put("Fraudstream", "Leetmeme");

    List<String> expected = Arrays.asList("Ice", "Cyberportal", "Leetmeme", "KittenService", "CamelCaser", "Fraudstream");
    List<String> actual = installer.getInstallOrder(adjList);

    Assert.assertEquals(expected, actual);
  }

  @Test(expected = CycleException.class)
  public void testGetInstallOrderWithCycle() throws Exception {
    Map<String, String> adjList = new HashMap<>();
    adjList.put("Leetmeme", "Cyberportal");
    adjList.put("Cyberportal", "Ice");
    adjList.put("CamelCaser", "KittenService");
    adjList.put("Ice", "Leetmeme");

    installer.getInstallOrder(adjList);
  }

  @Test(expected = CycleException.class)
  public void testBasicCycle() throws Exception {
    Map<String, String> adjList = new HashMap<>();
    adjList.put("A", "B");
    adjList.put("B", "C");
    adjList.put("C", "A");

    installer.getInstallOrder(adjList);
  }

  @Test(expected = CycleException.class)
  public void testIntenseCycle() throws Exception {
    Map<String, String> adjList = new HashMap<>();
    adjList.put("B", "C");
    adjList.put("A", "B");
    adjList.put("D", "B");
    adjList.put("C", "E");
    adjList.put("F", "E");
    adjList.put("G", "E");
    // second graph that includes cycle
    adjList.put("H", "I");
    adjList.put("I", "J");
    adjList.put("J", "K");
    adjList.put("L", "M");
    adjList.put("N", "J");
    adjList.put("O", "N");
    adjList.put("M", "J");
    adjList.put("K", "N");

    installer.getInstallOrder(adjList);
  }

  @Test
  public void testGraphWithNoCycle() throws Exception {
    Map<String, String> adjList = new HashMap<>();
    adjList.put("B", "C");
    adjList.put("A", "B");
    adjList.put("D", "B");
    adjList.put("C", "E");
    adjList.put("F", "E");
    adjList.put("G", "E");
    // second graph
    adjList.put("H", "I");
    adjList.put("I", "J");
    adjList.put("J", "K");
    adjList.put("L", "M");
    adjList.put("N", "J");
    adjList.put("O", "N");

    List<String> expected = Arrays.asList("E", "C", "B", "A", "D", "F", "G",
        "K", "J", "I", "H", "M", "L", "N", "O");
    List<String> actual = installer.getInstallOrder(adjList);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void fullInstall() throws Exception {
    List<String> input = new LinkedList<>();
    input.add("KittenService: ");
    input.add("Leetmeme: Cyberportal");
    input.add("Cyberportal: Ice");
    input.add("CamelCaser: KittenService");
    input.add("Fraudstream: Leetmeme");
    input.add("Ice: ");

    String expected = "Ice, Cyberportal, Leetmeme, KittenService, CamelCaser, Fraudstream";
    String actual = installer.install(input);
    Assert.assertEquals(expected, actual);
  }

  @Test(expected = CycleException.class)
  public void fullInstallWithCycle() throws Exception {
    List<String> input = new LinkedList<>();
    input.add("I: J");
    input.add("C: D");
    input.add("B: C");
    input.add("L: N");
    input.add("M: N");
    input.add("J: L");
    input.add("A: B");
    input.add("F: G");
    input.add("N: ");
    input.add("G: C");
    input.add("E: F");
    input.add("K: L");
    input.add("H: I");
    input.add("D: H");
    input.add("L: A");

    installer.install(input);
  }

  @Test
  public void fullInstallWithNoCycleAndWeirdOrder() throws Exception {
    List<String> input = new LinkedList<>();
    input.add("I: J");
    input.add("C: D");
    input.add("B: C");
    input.add("L: N");
    input.add("M: N");
    input.add("J: L");
    input.add("A: B");
    input.add("F: G");
    input.add("N: ");
    input.add("G: C");
    input.add("E: F");
    input.add("K: L");
    input.add("H: I");
    input.add("D: ");


    String expected = "D, C, B, A, G, F, E, N, L, J, I, H, K, M";
    String actual = installer.install(input);
    Assert.assertEquals(expected, actual);
  }
}

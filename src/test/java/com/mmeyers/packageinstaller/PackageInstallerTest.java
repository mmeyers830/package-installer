package com.mmeyers.packageinstaller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class PackageInstallerTest {
  private PackageInstaller installer;

  @BeforeClass
  public void init() {
    installer = new PackageInstaller();
  }

  @Test
  public void testBasicCycle() throws Exception {
    Map<String, String> adjList = new HashMap<>();
    adjList.put("A", "B");
    adjList.put("B", "C");
    adjList.put("C", "A");

    Assert.assertTrue(installer.isCyclic(adjList));
  }

  @Test
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

    Assert.assertTrue(installer.isCyclic(adjList));
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

    Assert.assertTrue(installer.isCyclic(adjList));
  }

  @Test
  public void testBasicInput() throws Exception {
    List<String> input = new LinkedList<>();
    input.add("KittenService: CamelCaser");
    input.add("CamelCaser: ");

    Map<String, String> expected = new HashMap<>();
    expected.put("CamelCase", "CamelCaser");


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
  public void testGetInstallOrder() throws Exception {
    Map<String, String> adjList = new HashMap<>();
    adjList.put("Leetmeme", "Cyberportal");
    adjList.put("Cyberportal", "Ice");
    adjList.put("CamelCaser", "KittenService");
    adjList.put("Fraudstream", "Leetmeme");

    List<String> expected = Arrays.asList("Ice", "Cyberportal", "Leetmeme", "KittenService", "CamelCaser", "Fraudstream");
    List<String> actual = installer.getInstallOrder(adjList);

    Assert.assertEquals(expected, actual);
  }
}

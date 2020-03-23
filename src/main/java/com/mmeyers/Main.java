package com.mmeyers;

import com.mmeyers.packageinstaller.CycleException;
import com.mmeyers.packageinstaller.PackageInstaller;
import java.util.Arrays;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    PackageInstaller installer = new PackageInstaller();
    List<String> input = Arrays.asList("KittenService: ", "Leetmeme: Cyberportal", "Cyberportal: Ice", "CamelCaser: KittenService", "Fraudstream: ", "Ice: Leetmeme");
    try {
      String str = installer.install(input);
      System.out.println(str);
    } catch (CycleException e) {
      System.out.println(e);
    }
  }
}

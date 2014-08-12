package com.orientechnologies.orient.test.database;

import org.testng.annotations.Test;

public class OTestServerMainRunner {

  @Test
  public void test() throws Exception {
    OTestServerMain tests = new OTestServerMain();
    tests.start();
    System.out.println(tests.getConnection());
    OTestServerMain tests1 = new OTestServerMain();
    tests1.start();
    System.out.println(tests1.getConnection());

    System.out.println(tests.stop());
    System.out.println(tests1.stop());
  }
}

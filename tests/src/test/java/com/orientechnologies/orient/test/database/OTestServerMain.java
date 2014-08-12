package com.orientechnologies.orient.test.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintStream;

import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.OServerMain;
import com.orientechnologies.orient.server.network.OServerNetworkListener;
import com.orientechnologies.orient.server.network.protocol.binary.ONetworkProtocolBinary;

public class OTestServerMain extends OServerMain {

  public static void main(final String[] args) throws Exception {

    PrintStream out = System.out;
    PrintStream err = System.err;
    PrintStream log = new PrintStream(new File("out.txt"));
    System.setOut(log);
    System.setErr(log);
    OServer instance = new OServer();
    try {
      instance.startup(OTestServerMain.class.getClassLoader().getResourceAsStream("orientdb-server-config.xml"));
      instance.activate();
      out.println(true);
      for (OServerNetworkListener list : instance.getNetworkListeners()) {
        if (list.isActive() && list.getProtocolType().equals(ONetworkProtocolBinary.class)) {
          out.println(list.getListeningAddress(false));
        }
      }
      instance.awaitTerminate();
      System.exit(0);
    } catch (Exception e) {
      instance.shutdown();
      instance.awaitTerminate();
      out.println(false);
      System.exit(1);
    } finally {
      System.setOut(out);
      System.setErr(err);
    }
  }

  private Process process;
  private String  connection;

  public OTestServerMain() {
  }

  public boolean start() throws Exception {
    process = Runtime.getRuntime().exec(
        "mvn exec:java -Dexec.mainClass=com.orientechnologies.orient.test.database.OTestServerMain -Dexec.classpathScope=test -q");
    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    String line = reader.readLine();
    if (line.equals("true")) {
      connection = reader.readLine();
      return true;
    } else {
      process.waitFor();
      return false;
    }
  }

  public int stop() throws InterruptedException {
    process.destroy();
    return process.waitFor();
  }

  public String getConnection() {
    return connection;
  }

}

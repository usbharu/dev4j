package io.github.usbharu.dtf.cui;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import io.github.usbharu.dtf.core.ToolLoader;
import java.io.IOException;

public class Main {

  public static void main(String[] args) {
    try (ScanResult scan = new ClassGraph().enableAllInfo().scan()) {
      for (ClassInfo classInfo : scan.getClassesWithAnnotation(
          "io.github.usbharu.dtf.annotation.Author")) {
        System.out.println("classInfo = " + classInfo);
        Class.forName(classInfo.getName());
        if (classInfo.getName().equals("io.github.usbharu.dtf.DuplicateLineRemover")) {
//          System.out.println("classInfo = " + classInfo);
//          ToolLoader.use(ToolLoader.TOOLS.get(ToolLoader.TOOLS.size() - 1),
//              new String[] {"D:\\Documents\\test\\docker-compose.yml",
//                  "D:\\Documents\\test\\docker-compose.yml.save"});
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    System.out.println("ToolLoader.tools = " + ToolLoader.TOOLS);
  }
}

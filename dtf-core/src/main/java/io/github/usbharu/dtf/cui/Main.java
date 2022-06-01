package io.github.usbharu.dtf.cui;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import io.github.usbharu.dtf.core.Tool;
import io.github.usbharu.dtf.core.ToolLoader;
import java.io.IOException;

public class Main {

  public static void main(String[] args) {
    try (ScanResult scan = new ClassGraph().enableAllInfo().scan()) {
      for (ClassInfo classInfo : scan.getClassesWithAnnotation(
          "io.github.usbharu.dtf.annotation.Author")) {
        Class.forName(classInfo.getName());
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    if (args.length <= 0) {
      return;
    }
    for (Tool tool : ToolLoader.TOOLS) {
      if (tool.getClass().getSimpleName().equals(args[0])) {
        System.out.println("tool = " + tool);
      }
    }
  }
}

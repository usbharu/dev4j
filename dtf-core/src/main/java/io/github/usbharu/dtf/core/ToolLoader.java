package io.github.usbharu.dtf.core;

import io.github.usbharu.dtf.util.UniqueArrayList;
import java.io.File;
import java.util.List;

public class ToolLoader {

  public static final List<Tool> TOOLS = new UniqueArrayList<>();

  static {
    File toolsDir = new File("tools").getAbsoluteFile();
    System.out.println("toolsDir = " + toolsDir);
  }

  public static boolean addTool(Tool tool) {
    System.out.println("addTool: " + tool.getClass().getName());
    return TOOLS.add(tool);

  }

  public static Object use(Tool tool, String[] args) {
    return tool.pickup(args);
  }
}

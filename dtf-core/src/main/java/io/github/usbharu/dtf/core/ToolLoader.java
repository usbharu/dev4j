package io.github.usbharu.dtf.core;

import java.util.ArrayList;
import java.util.List;

public class ToolLoader {

  public static final List<Tool> tools = new ArrayList<>();

  public static boolean addTool(Tool tool) {
    System.out.println("addTool: " + tool.getClass().getName());
    return tools.add(tool);

  }

  public static Object use(Tool tool, String[] args) {
    return tool.pickup(args);
  }
}

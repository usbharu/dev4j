package io.github.usbharu.dev4j.core;

import io.github.usbharu.dev4j.api.Tool;
import java.util.ArrayList;
import java.util.List;

public class ToolLoader {

  private static final List<Tool> tools = new ArrayList<>();

  public static void addTool(Tool tool) {
    tools.add(tool);
  }

  public static Object use(Tool tool,String[] args){
    return tool.pickup(args);
  }
}

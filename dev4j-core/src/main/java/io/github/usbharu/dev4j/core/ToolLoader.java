package io.github.usbharu.dev4j.core;

import io.github.usbharu.dev4j.api.Tool;
import java.util.ArrayList;
import java.util.List;

public class ToolLoader {

  private static final List<Tool> tools = new ArrayList<>();

  public void addTool(Tool tool) {
    tools.add(tool);
  }
}
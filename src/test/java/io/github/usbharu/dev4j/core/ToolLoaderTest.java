package io.github.usbharu.dev4j.core;

import static org.junit.jupiter.api.Assertions.*;

import io.github.usbharu.dev4j.api.AbstractTool;
import org.junit.jupiter.api.Test;

class ToolLoaderTest {

  @Test
  void use() {
    Tool tool = new AbstractTool() {
      @Override
      public Object[] pickup(String[] args) {
        return args;
      }
    };

    ToolLoader.use(tool, new String[] {"hahaha"});
  }
}

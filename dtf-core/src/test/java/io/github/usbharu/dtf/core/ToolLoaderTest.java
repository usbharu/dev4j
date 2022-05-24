package io.github.usbharu.dtf.core;

import io.github.usbharu.dtf.api.AbstractTool;
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

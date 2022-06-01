package io.github.usbharu.dtf.tools;

import io.github.usbharu.dtf.annotation.Author;
import io.github.usbharu.dtf.annotation.Tool;
import io.github.usbharu.dtf.api.AbstractTool;
import io.github.usbharu.dtf.core.ToolLoader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@Author("usbharu")
public class LineCounter extends AbstractTool {

  static boolean b = ToolLoader.addTool(new LineCounter());

  @Tool
  public Object[] count(String path) {
    try (Stream<String> lines = Files.lines(Path.of(path))) {
      System.out.println("lines.count() = " + lines.count());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return new Object[] {path};
  }
}

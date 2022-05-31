package io.github.usbharu.dtf;

import io.github.usbharu.dtf.annotation.Author;
import io.github.usbharu.dtf.annotation.Tool;
import io.github.usbharu.dtf.api.AbstractTool;
import io.github.usbharu.dtf.core.ToolLoader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Author("usbharu")
public class DuplicateLineRemover extends AbstractTool {

  static boolean a = ToolLoader.addTool(new DuplicateLineRemover());

  @Tool
  public Object[] remove(String path, String outputPath) {
    try (BufferedReader bufferedReader = new BufferedReader(
        new InputStreamReader(new FileInputStream(path)));
        PrintWriter pw = new PrintWriter(outputPath);) {
      List<String> lines = new ArrayList<>();
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        if (!lines.contains(line)) {
          lines.add(line);
          pw.println(line);
        }
      }
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return new Object[] {new File(outputPath)};
  }
}

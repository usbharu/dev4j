package io.github.usbharu.devTools4j.api;


import io.github.usbharu.devTools4j.core.Tool;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTool implements Tool {

  @Override
  public String getAuthor() {
    return "";
  }

  @Override
  public List<Class<?>> argument() {
    return new ArrayList<>();
  }

  @Override
  public List<Class<?>> response() {
    return new ArrayList<>();
  }
}

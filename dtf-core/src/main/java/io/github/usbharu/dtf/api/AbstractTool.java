package io.github.usbharu.dtf.api;


import io.github.usbharu.dtf.core.Tool;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTool implements Tool {

  @Override
  public String getAuthor() {
    return "";
  }

  @Override
  public Object[] pickup(String[] args) {
    return new Object[0];
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

package io.github.usbharu.dev4j.core;

import io.github.usbharu.dev4j.api.Tool;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTool implements Tool {

  @Override
  public List<Class<?>> argument() {
    return new ArrayList<>();
  }

  @Override
  public List<Class<?>> response() {
    return new ArrayList<>();
  }
}
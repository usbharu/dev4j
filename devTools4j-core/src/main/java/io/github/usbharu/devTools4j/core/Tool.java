package io.github.usbharu.devTools4j.core;

import java.util.List;

public interface Tool {

  String getAuthor();

  Object[] pickup(String[] args);

  List<Class<?>> argument();

  List<Class<?>> response();
}

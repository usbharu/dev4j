package io.github.usbharu.dev4j.api;

import java.util.List;

public interface Tool {

  String getAuthor();

  Object[] pickup(String[] args);

  List<Class<?>> argument();

  List<Class<?>> response();
}

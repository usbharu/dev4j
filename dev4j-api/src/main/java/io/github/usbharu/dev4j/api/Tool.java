package io.github.usbharu.dev4j.api;

import java.util.List;

public interface Tool {

  String getAuthor();

  Object[] use(Object... arg);

  List<Class<?>> argument();

  List<Class<?>> response();
}
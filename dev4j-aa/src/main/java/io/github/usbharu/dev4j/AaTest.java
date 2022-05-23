package io.github.usbharu.dev4j;

import io.github.usbharu.dev4j.annotation.Author;
import io.github.usbharu.dev4j.api.AbstractTool;

@Author
public class AaTest extends AbstractTool {

  //test
  @Override
  public String getAuthor() {
    return super.getAuthor();
  }

  @Override
  public Object[] pickup(String[] arg) {
    return new Object[0];
  }

  public Object[] use(Object... arg) {
    return new Object[0];
  }
}

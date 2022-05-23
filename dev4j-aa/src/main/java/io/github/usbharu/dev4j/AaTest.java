package io.github.usbharu.dev4j;

import io.github.usbharu.dev4j.annotation.Author;
import io.github.usbharu.dev4j.annotation.Tool;
import io.github.usbharu.dev4j.api.AbstractTool;

@Tool()
@Author("usbharu")
public class AaTest extends AbstractTool {

  //test
  @Override
  public String getAuthor() {
    return super.getAuthor();
  }

  @Override
  public Object[] pickup(String[] arg) {
    String s = arg[0];
    int i = Integer.parseInt(arg[1]);
    boolean b = Boolean.parseBoolean(arg[2]);
    return use(s, i, b);
  }

  public Object[] use(String p1, int p2, boolean p3) {
    return new Object[] {p1, p2, p3};
  }
}

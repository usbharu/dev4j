package io.github.usbharu.devTools4j;

import io.github.usbharu.devTools4j.annotation.Author;
import io.github.usbharu.devTools4j.annotation.Tool;
import io.github.usbharu.devTools4j.api.AbstractTool;


@Author("usbharu")
public class AaTest extends AbstractTool {

  //test
  @Override
  public String getAuthor() {
    return super.getAuthor();
  }

  @Override
  public Object[] pickup(String[] arg) {
  }

  public void test(String[] arg){
    System.out.println("arg = " + arg);
  }

  @Tool
  public Object[] use(int a,boolean v) {
    return new Object[] {};
  }
}

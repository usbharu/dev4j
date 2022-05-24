package io.github.usbharu.dev4j;

import static java.lang.Integer.*;
import static java.lang.String.*;

import io.github.usbharu.dev4j.annotation.Author;
import io.github.usbharu.dev4j.annotation.Tool;
import io.github.usbharu.dev4j.api.AbstractTool;


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

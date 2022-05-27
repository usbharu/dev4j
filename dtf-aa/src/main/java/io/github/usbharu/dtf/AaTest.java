package io.github.usbharu.dtf;

import io.github.usbharu.dtf.annotation.Author;
import io.github.usbharu.dtf.annotation.Tool;
import io.github.usbharu.dtf.api.AbstractTool;


@Author("usbharu")
public class AaTest extends AbstractTool {

  //test
  @Override
  public String getAuthor() {
    return super.getAuthor();
  }


  public void test(String[] arg){
    System.out.println("arg = " + arg);
  }

  @Tool
  public Object[] use(int a,boolean v) {
    return new Object[] {};
  }
}

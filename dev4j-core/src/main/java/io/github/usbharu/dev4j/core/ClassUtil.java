package io.github.usbharu.dev4j.core;

import com.sun.tools.javac.comp.Annotate;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Enter;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.MemberEnter;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.util.Context;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassUtil {

  private static final Method blockAnnotations;
  private static final Method unblockAnnotations;
  private static final Method memberEnter;

  static {
    try {
      memberEnter =
          getMethod(MemberEnter.class, "memberEnter", JCTree.JCClassDecl.class, Env.class);
      blockAnnotations = getMethod(Annotate.class, "blockAnnotations");
      unblockAnnotations = getMethod(Annotate.class, "unblockAnnotations");
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  private ClassUtil() {
  }

  public static void injectMethod(JCClassDecl classDecl, JCMethodDecl methodDecl, Context context) {
    classDecl.defs.append(methodDecl);
    MemberEnter mem = MemberEnter.instance(context);
    Annotate annotate = Annotate.instance(context);
    Enter enter = Enter.instance(context);
    Env<AttrContext> env = enter.getEnv(classDecl.sym);

    invokeSneaky(blockAnnotations, annotate);
    invokeSneaky(memberEnter, mem, methodDecl, env);
    invokeSneaky(unblockAnnotations, annotate);
  }

  public static Object invokeSneaky(Method method, Object reciver, Object... args) {
    try {
      return method.invoke(reciver, args);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  public static Method getMethod(Class<?> c, String methodName, Class<?>... types)
      throws NoSuchMethodException {
    Method m = null;
    Class<?> tmpC = c;
    while (c != null) {
      try {
        m = c.getDeclaredMethod(methodName, types);
        break;
      } catch (NoSuchMethodException ignored) {
      }
      c = c.getSuperclass();
    }

    if (m == null) {
      throw new NoSuchMethodException(tmpC.getName());
    }
    m.setAccessible(true);
    return m;
  }

}
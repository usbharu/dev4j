package io.github.usbharu.dtf.annotation;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;
import io.github.usbharu.dtf.util.ASTUtils;


public class Dev4jVisitor extends TreeScanner<Void,Void> {

  private final TreeMaker maker;
  private final Names names;

  public Dev4jVisitor(Context context) {
    super();
    maker = TreeMaker.instance(context);
    names = Names.instance(context);
  }

  @Override
  public Void visitMethod(MethodTree methodTree, Void unused) {
    if (methodTree instanceof JCMethodDecl) {
      JCMethodDecl method = (JCMethodDecl) methodTree;
      System.out.println("method = " + method);
      System.out.println("method.sym = " + method.sym);
      if (method.sym.toString().equals("getAuthor()")) {
        method.restype=maker.Ident(names.fromString("String"));
        method.body=ASTUtils.makeLiteralReturnBlock(maker,"testest");
      }else if (method.name.toString().equals("")){

      }
      System.out.println("method = " + method);
    }

    return super.visitMethod(methodTree, unused);
  }

  @Override
  public Void visitAnnotation(AnnotationTree annotationTree, Void unused) {
    System.out.println("annotationTree = " + annotationTree);
    return super.visitAnnotation(annotationTree, unused);
  }
}

package io.github.usbharu.dtf.annotation;

import com.google.auto.service.AutoService;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;
import io.github.usbharu.dtf.util.ASTUtils;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@AutoService(Processor.class)
@SupportedAnnotationTypes("*")
public class AnnotationProcessor extends AbstractProcessor {

  private final Logger LOGGER = LoggerFactory.getLogger(getClass());

  private Trees trees;
  private Context context;
  private JavacElements elementUtils;
  private TreeMaker maker;
  private Names names;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    trees = Trees.instance(processingEnv);
    context = ((JavacProcessingEnvironment) processingEnv).getContext();
    elementUtils = ((JavacElements) processingEnv.getElementUtils());
    maker = TreeMaker.instance(context);
    names = Names.instance(context);
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

    for (Element element : roundEnv.getElementsAnnotatedWith(Author.class)) {
      System.out.println("element = " + element);
      generateGetAuthor(element);
      System.out.println("element end = " + element);
    }
    for (Element element : roundEnv.getElementsAnnotatedWith(Tool.class)) {
      System.out.println("element = " + element);
      generatePickup(element);
      System.out.println("element end = " + element);
    }
    return false;
  }

  private void generatePickup(Element element) {
    Element enclosingElement = element.getEnclosingElement();
    JCTree clazz = elementUtils.getTree(enclosingElement);
    JCTree tree = elementUtils.getTree(element);
    JCMethodDecl jcMethodDecl = (JCMethodDecl) tree;
    JCClassDecl jcClassDecl = (JCClassDecl) clazz;
    Tool tool = element.getAnnotation(Tool.class);
    if (tool != null) {

      List<JCVariableDecl> arg = List.of(
          maker.VarDef(maker.Modifiers(Flags.PARAMETER + Flags.VarFlags), names.fromString("arg"),
              maker.TypeArray(maker.Ident(names.fromString("String"))), null));
//      System.out.println("arg = " + arg);
      JCMethodDecl pickup = maker.MethodDef(maker.Modifiers(0), names.fromString("pickup"),
          maker.TypeArray(maker.Ident(names.fromString("Object"))), List.nil(), arg, List.nil(),
          null, null);
//          ASTUtils.makeEmptyMethod(maker, names, jcClassDecl, "pickup(java.lang.String[])");
      List<JCStatement> statements = List.nil();
      List<JCVariableDecl> params = jcMethodDecl.params;
      LOGGER.debug("generate params size: {}", params.size());
      for (int i = 0, paramsSize = params.size(); i < paramsSize; i++) {
        JCVariableDecl param = params.get(i);
        String s = literalToParseMethodName(param.vartype.toString());
//        System.out.println("s = " + s);
        List<JCExpression> args =
            List.of(maker.Indexed(maker.Ident(names.fromString("arg")), maker.Literal(i)));
        JCExpressionStatement exec = maker.Exec(maker.Apply(null,
            maker.Select(maker.Ident(names.fromString(primitiveToObject(param.vartype.toString()))),
                names.fromString(s)), args));
//        System.out.println("exec = " + exec);
        JCExpression expression = exec.getExpression();
//        System.out.println("expression = " + expression);
        JCVariableDecl jcVariableDecl =
            maker.VarDef(maker.Modifiers(Flags.LocalVarFlags),
                names.fromString(param.name.toString()),
                maker.Ident(names.fromString(primitiveToObject(param.vartype.toString()))),
                expression);
        statements = statements.append(jcVariableDecl);
      }
      List<JCExpression> expressions = List.nil();
      for (JCVariableDecl param : params) {
        expressions = expressions.append(maker.Ident(names.fromString(param.name.toString())));
      }
      JCExpressionStatement exec =
          ASTUtils.exec(maker, names, jcMethodDecl.name.toString(), expressions);
      statements = statements.append(maker.Return(exec.getExpression()));
      pickup.body = maker.Block(0, statements);
      pickup.params = arg;
      pickup.restype = maker.TypeArray(maker.Ident(names.fromString("Object")));
//      System.out.println("pickup = " + pickup);
//      System.out.println("jcClassDecl = " + jcClassDecl);
    }
    System.out.println("jcClassDecl = " + jcClassDecl);
  }

  private void generateGetAuthor(Element element) {
    Author annotation = element.getAnnotation(Author.class);
    JCTree tree = elementUtils.getTree(element);
    JCClassDecl jcClassDecl = (JCClassDecl) tree;

    if (annotation != null) {
      System.out.println("generate get author");
      String author = annotation.value();
      jcClassDecl.defs = jcClassDecl.defs.prepend(
          ASTUtils.makeLiteralReturnMethod(maker, names, jcClassDecl, "getAuthor()", "String",
              author));
      System.out.println("jcClassDecl = " + jcClassDecl);
    }
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latest();
  }

  private String literalToParseMethodName(String literalName) {
    switch (literalName) {
      case "String":
        return "valueOf";
      case "int":
        return "parseInt";
      case "boolean":
        return "parseBoolean";
      default:
        throw new IllegalArgumentException(literalName);
    }
  }

  private String primitiveToObject(String primitiveName) {
    switch (primitiveName) {
      case "String":
        return "String";
      case "int":
        return "Integer";
      case "boolean":
        return "Boolean";
      default:
        throw new IllegalArgumentException(primitiveName);
    }
  }
}

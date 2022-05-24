package io.github.usbharu.dev4j.annotation;

import com.google.auto.service.AutoService;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;
import io.github.usbharu.dev4j.util.ASTUtils;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;


@AutoService(Processor.class)
@SupportedAnnotationTypes("*")
public class AnnotationProcessor extends AbstractProcessor {

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
      Author annotation = element.getAnnotation(Author.class);
      JCTree tree = elementUtils.getTree(element);
      JCClassDecl jcClassDecl = (JCClassDecl) tree;

      if (annotation != null) {
        String author = annotation.value();
        ASTUtils.makeLiteralReturnMethod(maker, names, jcClassDecl, "getAuthor()", "String",
            author);
        System.out.println("jcClassDecl = " + jcClassDecl);
      }
    }
    for (Element element : roundEnv.getElementsAnnotatedWith(Tool.class)) {
      Element enclosingElement = element.getEnclosingElement();
      JCTree clazz = elementUtils.getTree(enclosingElement);
      JCTree tree = elementUtils.getTree(element);
      JCMethodDecl jcMethodDecl = (JCMethodDecl) tree;
      System.out.println("jcMethodDecl.body = " + jcMethodDecl.body);
      JCClassDecl jcClassDecl = (JCClassDecl) clazz;
      Tool tool = element.getAnnotation(Tool.class);
      System.out.println("tool = " + tool);
      if (tool != null) {

        List<JCVariableDecl> arg = List.of(
            maker.VarDef(maker.Modifiers(Flags.PARAMETER), names.fromString("arg"),
                maker.TypeArray(maker.Ident(names.fromString("String"))), null));
        System.out.println("arg = " + arg);
        JCMethodDecl pickup =
            ASTUtils.makeEmptyMethod(maker, names, jcClassDecl, "pickup(java.lang.String[])");
        List<JCStatement> statements = List.nil();
        List<JCVariableDecl> params = jcMethodDecl.params;
        for (int i = 0, paramsSize = params.size(); i < paramsSize; i++) {
          JCVariableDecl param = params.get(i);
          String s = literalToParseMethodName(param.vartype.toString());
          System.out.println("s = " + s);
          List<JCExpression> args =
              List.of(maker.Indexed(maker.Ident(names.fromString("arg")), maker.Literal(i)));
          JCExpressionStatement exec = maker.Exec(maker.Apply(null,maker.Select(maker.Ident(names.fromString(
              primitiveToObject(param.vartype.toString()))),names.fromString(s)),args));
//          JCExpressionStatement exec = ASTUtils.exec(maker, names, s, args);
          System.out.println("exec = " + exec);
          JCExpression expression = exec.getExpression();
          System.out.println("expression = " + expression);
          JCVariableDecl jcVariableDecl =
              maker.VarDef(maker.Modifiers(Flags.PARAMETER),
                  names.fromString(param.name.toString()),
                  maker.Ident(names.fromString(primitiveToObject(param.vartype.toString()))),
                  expression);
          statements = statements.append(jcVariableDecl);
        }
        List<JCExpression> expressions = List.nil();
        for (JCVariableDecl param : params) {
          expressions =
              expressions.append(maker.Ident(names.fromString(param.name.toString())));
        }
        JCExpressionStatement exec =
            ASTUtils.exec(maker, names, jcMethodDecl.name.toString(), expressions);
        statements =
            statements.append(maker.Return(exec.getExpression()));
        pickup.body = maker.Block(0, statements);
        pickup.params = arg;
//        ASTUtils.injectMethod(jcClassDecl, pickup);
        System.out.println("pickup = " + pickup);
        System.out.println("jcClassDecl = " + jcClassDecl);

//        JCExpressionStatement exec =
//            maker.Exec(maker.Apply(List.nil(), maker.Ident(names.fromString("Integer.parseInt")),
//                List.of(maker.Literal("100"))));
//        JCVariableDecl jcVariableDecl =
//            maker.VarDef(maker.Modifiers(0), names.fromString("integerParsed"),
//                maker.Ident(names.fromString("int")), exec.getExpression());
//        System.out.println("jcVariableDecl = " + jcVariableDecl);
      }
    }
    return false;
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

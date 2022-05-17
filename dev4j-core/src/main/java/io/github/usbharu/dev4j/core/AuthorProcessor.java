package io.github.usbharu.dev4j.core;

import com.google.auto.service.AutoService;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreeScanner;
import com.sun.source.util.Trees;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.parser.ParserFactory;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("io.github.usbharu.dev4j.api.Author")
public class AuthorProcessor extends AbstractProcessor {

  private Trees trees;
  private Context context;
  private TreeMaker maker;
  private JavacElements elements;
  private ParserFactory parserFactory;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    trees = Trees.instance(processingEnv);
    context = ((JavacProcessingEnvironment) processingEnv).getContext();
    maker = TreeMaker.instance(context);
    elements = JavacElements.instance(context);
    parserFactory = ParserFactory.instance(context);
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    roundEnv.getRootElements().stream().map(this::toUnit).forEach(compilationUnitTree -> {
      System.out.println("----CompilationUnitTree----");
      compilationUnitTree.accept(new AuthorVisitor(), null);
      System.out.println(compilationUnitTree);
    });
    return false;
  }

  private CompilationUnitTree toUnit(Element element) {
    TreePath path = trees.getPath(element);
    return path.getCompilationUnit();
  }

  private class AuthorVisitor extends TreeScanner<Void, Void> {

    @Override
    public Void visitMethod(MethodTree node, Void unused) {
      if (node instanceof JCMethodDecl) {
        JCMethodDecl md = (JCMethodDecl) node;
        if ("getAuthor".equals(md.name.toString())) {
          md.body.stats =
              List.of(parserFactory.newParser("return \"hello\";", false, false, false)
                  .parseStatement());
          System.out.println("md.body.stats = " + md.body.stats);
        }
      }
      return super.visitMethod(node, unused);
    }
  }
}
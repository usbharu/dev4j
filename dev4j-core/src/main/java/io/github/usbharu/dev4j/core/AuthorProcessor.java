package io.github.usbharu.dev4j.core;

import com.google.auto.service.AutoService;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.parser.ParserFactory;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCReturn;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Names;
import io.github.usbharu.dev4j.api.Author;
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
  private JavacElements elementUtils;
  private ParserFactory parserFactory;
  private Names names;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    this.elementUtils = ((JavacElements) processingEnv.getElementUtils());
    context = ((JavacProcessingEnvironment) processingEnv).getContext();
    this.maker = TreeMaker.instance(context);
    names = Names.instance(context);
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Author.class);
    System.out.println("elements.size() = " + elements.size());
    if (!elements.isEmpty()) {
      for (Element element : elements) {
        JCClassDecl classDecl = (JCClassDecl) elementUtils.getTree(element);
        String className = element.getSimpleName().toString();
        System.out.println("className = " + className);
        ListBuffer<JCTree.JCStatement> methodBlockStatements = new ListBuffer<>();
        System.out.println("names = " + names);
        JCReturn jcReturn = maker.Return(maker.Literal(element.getAnnotation(Author.class).value()));
        JCBlock block = maker.Block(0, List.of(jcReturn));
        classDecl.defs = classDecl.defs.prepend(
            maker.MethodDef(maker.Modifiers(Flags.PUBLIC),
                names.fromString("getAuthor"),
                maker.Ident(names.fromString("String")),
                List.nil(),
                List.nil(),
                List.nil(),
                block,
                null));
        System.out.println("classDecl = " + classDecl);
      }
    }
    return false;
  }
}
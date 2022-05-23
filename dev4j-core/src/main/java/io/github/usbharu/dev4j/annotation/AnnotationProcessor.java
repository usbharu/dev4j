package io.github.usbharu.dev4j.annotation;

import com.google.auto.service.AutoService;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
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
    for (Element element : roundEnv.getRootElements()) {
      Author annotation = element.getAnnotation(Author.class);
      JCTree tree = elementUtils.getTree(element);
      JCClassDecl jcClassDecl = (JCClassDecl) tree;

      if (annotation != null) {
        String author = annotation.value();
        ASTUtils.makeLiteralReturnMethod(maker, names, jcClassDecl, "getAuthor()", "String",
            author);
        System.out.println("jcClassDecl = " + jcClassDecl);
      }
      Tool tool = element.getAnnotation(Tool.class);
      System.out.println("tool = " + tool);
      if (tool != null) {
        System.out.println("aaaaaaaa "+ASTUtils.castBlock(maker, names, jcClassDecl,
                maker.Ident(names.fromString("String"))));
        System.out.println("jcClassDecl = " + jcClassDecl);
      }


    }
    return false;
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latest();
  }
}

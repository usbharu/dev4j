package io.github.usbharu.dev4j.annotation;

import com.google.auto.service.AutoService;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.util.Context;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;


@AutoService(Processor.class)
@SupportedAnnotationTypes("*")
public class AnnotationProcessor extends AbstractProcessor {

  private Trees trees;
  private Context context;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    trees = Trees.instance(processingEnv);
    context = ((JavacProcessingEnvironment) processingEnv).getContext();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    roundEnv.getRootElements().stream().map(this::toUnit).forEach(c -> c.accept(new Dev4jVisitor(context),null));
    return false;
  }

  private CompilationUnitTree toUnit(Element element){
    TreePath path = trees.getPath(element);
    return path.getCompilationUnit();
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latest();
  }
}

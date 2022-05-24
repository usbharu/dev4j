package io.github.usbharu.dtf.annotation;

import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.parser.ParserFactory;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCReturn;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;
import java.util.ArrayList;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

//@AutoService(Processor.class)
//@SupportedAnnotationTypes("io.github.usbharu.dtf.annotation.Author")
public class AuthorProcessor extends AbstractProcessor {

  private Trees trees;
  private Context context;
  private TreeMaker maker;
  private JavacElements elementUtils;
  private ParserFactory parserFactory;
  private Names names;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    super.init(processingEnv);
    this.elementUtils = ((JavacElements) processingEnv.getElementUtils());
    context = ((JavacProcessingEnvironment) processingEnv).getContext();
    this.maker = TreeMaker.instance(context);
    names = Names.instance(context);
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Author.class);
    if (!elements.isEmpty()) {
      for (Element element : elements) {
        System.out.println("element = " + element);
        JCTree tree = elementUtils.getTree(element);
        JCClassDecl classDecl = (JCClassDecl) tree;
        JCMethodDecl jcMethodDecl = hasGetAuthor((JCClassDecl) tree);
        if (jcMethodDecl != null) {
          java.util.List<JCTree> list = new ArrayList<>();
          for (JCTree def : classDecl.defs) {
            if (!jcMethodDecl.equals(def)) {
              System.out.println("def = " + def);
              list.add(def);
            }
          }
          classDecl.defs = List.nil();
          for (JCTree jcTree : list) {
            classDecl.defs = classDecl.defs.prepend(jcTree);
          }
        }
        String className = element.getSimpleName().toString();
        System.out.println("className = " + className);

        System.out.println("names = " + names);
        JCReturn jcReturn =
            maker.Return(maker.Literal(element.getAnnotation(Author.class).value()));
        JCBlock block = maker.Block(0, List.of(jcReturn));
        classDecl.defs = classDecl.defs.prepend(
            maker.MethodDef(maker.Modifiers(Flags.PUBLIC+Flags.BAD_OVERRIDE),
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

  private JCMethodDecl hasGetAuthor(JCClassDecl tree) {
    for (JCTree def : tree.defs) {
      if (def instanceof JCMethodDecl && ((JCMethodDecl) def).sym.flatName()
          .toString().equals("getAuthor")) {
        return ((JCMethodDecl) def);
      }
    }
    return null;
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latest();
  }
}

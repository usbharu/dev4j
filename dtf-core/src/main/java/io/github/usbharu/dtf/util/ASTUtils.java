package io.github.usbharu.dtf.util;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCReturn;
import com.sun.tools.javac.tree.JCTree.JCTypeCast;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ASTUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(ASTUtils.class);
  private ASTUtils() {
  }

  public static JCReturn makeLiteralReturn(TreeMaker maker, Object literal) {
    return maker.Return(maker.Literal(literal));
  }

  public static JCBlock makeLiteralReturnBlock(TreeMaker maker, Object literal) {
    return maker.Block(0, List.of(makeLiteralReturn(maker, literal)));
  }

  public static JCBlock makeEmptyBlock(TreeMaker maker) {
    return maker.Block(0, List.nil());
  }

  /**
   * シンボルを元にクラスからメソッドを検索します. シンボルとは,
   * <pre>
   * {@code
   * public static main(String[] args){
   *
   * }
   * }
   * </pre>
   * というメソッドの場合シンボルは {@code main(java.lang.String[]) } となります。
   *
   * @param classDecl 検索するクラス。
   * @param sym       シンボル
   * @return 見つかったメソッド。なければ {@code null}
   */
  public static JCMethodDecl searchMethod(JCClassDecl classDecl, String sym) {
    LOGGER.debug("search method class: {} search symbol: {}", classDecl.name, sym);
    for (JCTree def : classDecl.defs) {
      LOGGER.trace("searching def: {}", def);
      try {
        if (def instanceof JCMethodDecl) {
          MethodSymbol symbol = ((JCMethodDecl) def).sym;
          LOGGER.trace("searching def type: {} returnType: {} params: {} tsym:{}", symbol.type,
              symbol.getReturnType(), symbol.params, symbol.type.tsym);

          if (symbol.toString().equals(sym)) {
            LOGGER.debug("searched method: {}", sym);
            return ((JCMethodDecl) def);
          }
        }
      } catch (NullPointerException e) {
        LOGGER.warn("not symbol", e);
      }
    }
    LOGGER.debug("{} not found", sym);
    return null;
  }

  public static JCMethodDecl makeEmptyMethod(TreeMaker maker, Names names, JCClassDecl classDecl,
      String name) {
    LOGGER.debug("make empty method: {}", name);
    JCMethodDecl jcMethodDecl = searchMethod(classDecl, name);
//    System.out.println("jcMethodDecl = " + jcMethodDecl);
    if (jcMethodDecl == null) {
      LOGGER.debug("make new empty method: {}", name);
      jcMethodDecl =
          makeNoParamMethod(maker, names, maker.Modifiers(Flags.PUBLIC + Flags.BAD_OVERRIDE),
              symbolToName(name), "void",
              makeEmptyBlock(maker));
      classDecl.defs = classDecl.defs.prepend(jcMethodDecl);
    } else {
      jcMethodDecl.body = makeEmptyBlock(maker);

    }
    return jcMethodDecl;
  }

  public static JCMethodDecl makeLiteralReturnMethod(TreeMaker maker, Names names,
      JCClassDecl classDecl, String sym, String type, Object literal) {
    LOGGER.debug("make literal return method");
    JCMethodDecl jcMethodDecl = searchMethod(classDecl, sym);
    if (jcMethodDecl == null) {
      LOGGER.debug("make method: {}", sym);
      JCMethodDecl jcMethodDecl1 = makeNoParamMethod(maker, maker.Modifiers(Flags.PUBLIC),
          names.fromString(symbolToName(sym)), maker.Ident(names.fromString(type)),
          makeLiteralReturnBlock(maker, literal));
//      jcMethodDecl1.sym = new MethodSymbol(Flags.PUBLIC,names.fromString(symbolToName(sym)),
//          new MethodType(List.nil(),maker.Ident(names.fromString("String")).type, List.nil(),null) ,classDecl.sym);
//      System.out.println("jcMethodDecl1 = " + jcMethodDecl1);
//      System.out.println("jcMethodDecl1.sym = " + jcMethodDecl1.sym.toString());
      return jcMethodDecl1;
    }
    jcMethodDecl.restype = maker.Ident(names.fromString(type));
    jcMethodDecl.body = makeLiteralReturnBlock(maker, literal);
//    jcMethodDecl.sym = new MethodSymbol(Flags.PUBLIC,names.fromString(symbolToName(sym)),maker.Ident(names.fromString("String")).type,classDecl.sym);
    return jcMethodDecl;
  }

  public static JCMethodDecl makeNoParamMethod(TreeMaker maker, JCTree.JCModifiers mods, Name name,
      JCExpression restype, JCBlock block) {
    return maker.MethodDef(mods, name, restype, List.nil(), List.nil(), List.nil(), block, null);
  }

  public static JCMethodDecl makeNoParamMethod(TreeMaker maker, Names names, JCModifiers mods,
      String name, String restype, JCBlock block) {
    return makeNoParamMethod(maker, mods, names.fromString(name),
        maker.Ident(names.fromString(restype)), block);
  }

  public static String symbolToName(String symbol) {
    return symbol.substring(0, symbol.indexOf("("));
  }

  public static JCBlock castBlock(TreeMaker maker, Names names, JCTree tree,
      JCExpression expression) {
    JCTypeCast jcTypeCast = maker.TypeCast(tree, expression);
    JCVariableDecl castTest =
        maker.VarDef(maker.Modifiers(Flags.PUBLIC), names.fromString("castTest"), expression,
            jcTypeCast);
    return maker.Block(0, List.of(castTest));
  }

//  public static JCBlock stringParsePrimitiveBlock(TreeMaker maker,Names names,)

  public static JCClassDecl injectMethod(JCClassDecl classDecl, JCMethodDecl methodDecl) {
    classDecl.defs = classDecl.defs.prepend(methodDecl);
    return classDecl;
  }

  public static JCExpressionStatement exec(TreeMaker maker, Names names, String name,
      List<JCExpression> args) {
    return maker.Exec(maker.Apply(List.nil(), maker.Ident(names.fromString(name)), args));
  }
}

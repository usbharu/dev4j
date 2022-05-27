package io.github.usbharu.dtf.util;

import com.sun.tools.javac.code.Flags;
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

public class ASTUtils {

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
    for (JCTree def : classDecl.defs) {
      if (def instanceof JCMethodDecl && ((JCMethodDecl) def).sym.toString()
          .equals(sym)) {
        return ((JCMethodDecl) def);
      }
    }
    return null;
  }

  public static JCMethodDecl makeEmptyMethod(TreeMaker maker, Names names, JCClassDecl classDecl,
      String name) {
    JCMethodDecl jcMethodDecl = searchMethod(classDecl, name);
    if (jcMethodDecl == null) {
      jcMethodDecl =
          makeNoParamMethod(maker, names, maker.Modifiers(Flags.PUBLIC), symbolToName(name), "void",
              makeEmptyBlock(maker));
      classDecl.defs = classDecl.defs.prepend(
          jcMethodDecl);
    } else {
      jcMethodDecl.body = makeEmptyBlock(maker);

    }
    return jcMethodDecl;
  }

  public static JCMethodDecl makeLiteralReturnMethod(TreeMaker maker, Names names,
      JCClassDecl classDecl, String sym, String type, Object literal) {
    JCMethodDecl jcMethodDecl = searchMethod(classDecl, sym);
    if (jcMethodDecl == null) {
      return makeNoParamMethod(maker, maker.Modifiers(Flags.PUBLIC),
          names.fromString(symbolToName(sym)),
          maker.Ident(names.fromString(type)), makeLiteralReturnBlock(maker, literal));
    }
    jcMethodDecl.restype = maker.Ident(names.fromString(type));
    jcMethodDecl.body = makeLiteralReturnBlock(maker, literal);
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

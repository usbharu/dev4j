package io.github.usbharu.dev4j.util;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCReturn;
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
          makeNoParamMethod(maker, names, maker.Modifiers(Flags.PUBLIC), name, "void",
              makeEmptyBlock(maker));
      classDecl.defs.prepend(
          jcMethodDecl);
    } else {
      jcMethodDecl.body = makeEmptyBlock(maker);

    }
    return jcMethodDecl;
  }

  public static JCMethodDecl makeLiteralReturnMethod(TreeMaker maker,Names names,JCClassDecl classDecl,String sym,Object literal){
    JCMethodDecl jcMethodDecl = searchMethod(classDecl, sym);
    if (jcMethodDecl == null) {
      return makeNoParamMethod(maker, maker.Modifiers(Flags.PUBLIC),
          names.fromString(symbolToName(sym)),
          maker.Ident(names.fromString("String")), makeLiteralReturnBlock(maker, literal));
    }
    jcMethodDecl.restype= maker.Ident(names.fromString("String"));
    jcMethodDecl.body=makeLiteralReturnBlock(maker, literal);
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

  public static String symbolToName(String symbol){
    return symbol.substring(0, symbol.indexOf("("));
  }
}

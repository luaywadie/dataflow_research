/* This file was generated with JastAdd2 (http://jastadd.org) version 2.3.3 */
package arithmetic.example;
/**
 * @ast node
 * @declaredat Arithmetic.ast:6
 * @astdecl intOp : Operand ::= <INT:String>;
 * @production intOp : {@link Operand} ::= <span class="component">&lt;INT:{@link String}&gt;</span>;

 */
public class intOp extends Operand implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public intOp() {
    super();
  }
  /**
   * Initializes the child array to the correct size.
   * Initializes List and Opt nta children.
   * @apilevel internal
   * @ast method
   * @declaredat ASTNode:10
   */
  public void init$Children() {
  }
  /**
   * @declaredat ASTNode:12
   */
  @ASTNodeAnnotation.Constructor(
    name = {"INT"},
    type = {"String"},
    kind = {"Token"}
  )
  public intOp(String p0) {
    setINT(p0);
  }
  /** @apilevel low-level 
   * @declaredat ASTNode:21
   */
  protected int numChildren() {
    return 0;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:25
   */
  public void flushAttrCache() {
    super.flushAttrCache();
  }
  /** @apilevel internal 
   * @declaredat ASTNode:29
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /** @apilevel internal 
   * @declaredat ASTNode:33
   */
  public intOp clone() throws CloneNotSupportedException {
    intOp node = (intOp) super.clone();
    return node;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:38
   */
  public intOp copy() {
    try {
      intOp node = (intOp) clone();
      node.parent = null;
      if (children != null) {
        node.children = (ASTNode[]) children.clone();
      }
      return node;
    } catch (CloneNotSupportedException e) {
      throw new Error("Error: clone not supported for " + getClass().getName());
    }
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @deprecated Please use treeCopy or treeCopyNoTransform instead
   * @declaredat ASTNode:57
   */
  @Deprecated
  public intOp fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:67
   */
  public intOp treeCopyNoTransform() {
    intOp tree = (intOp) copy();
    if (children != null) {
      for (int i = 0; i < children.length; ++i) {
        ASTNode child = (ASTNode) children[i];
        if (child != null) {
          child = child.treeCopyNoTransform();
          tree.setChild(child, i);
        }
      }
    }
    return tree;
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The subtree of this node is traversed to trigger rewrites before copy.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:87
   */
  public intOp treeCopy() {
    intOp tree = (intOp) copy();
    if (children != null) {
      for (int i = 0; i < children.length; ++i) {
        ASTNode child = (ASTNode) getChild(i);
        if (child != null) {
          child = child.treeCopy();
          tree.setChild(child, i);
        }
      }
    }
    return tree;
  }
  /**
   * Replaces the lexeme INT.
   * @param value The new value for the lexeme INT.
   * @apilevel high-level
   */
  public void setINT(String value) {
    tokenString_INT = value;
  }
  /** @apilevel internal 
   */
  protected String tokenString_INT;
  /**
   * Retrieves the value for the lexeme INT.
   * @return The value for the lexeme INT.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Token(name="INT")
  public String getINT() {
    return tokenString_INT != null ? tokenString_INT : "";
  }
}

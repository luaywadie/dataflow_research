/* This file was generated with JastAdd2 (http://jastadd.org) version 2.3.3 */
/**
 * @ast node
 * @declaredat Arithmetic.ast:46
 * @astdecl ArithmeticExpressionNode : StatementNode ::= lOp:Operand <OPERATOR:String> rOp:Operand;
 * @production ArithmeticExpressionNode : {@link StatementNode} ::= <span class="component">lOp:{@link Operand}</span> <span class="component">&lt;OPERATOR:{@link String}&gt;</span> <span class="component">rOp:{@link Operand}</span>;

 */
public class ArithmeticExpressionNode extends StatementNode implements Cloneable {
  /**
   * @aspect accept
   * @declaredat ASTAccept.jadd:13
   */
  Object accept(AstVisitor v) {
        return v.visit(this);
    }
  /**
   * @declaredat ASTNode:1
   */
  public ArithmeticExpressionNode() {
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
    children = new ASTNode[2];
  }
  /**
   * @declaredat ASTNode:13
   */
  @ASTNodeAnnotation.Constructor(
    name = {"lOp", "OPERATOR", "rOp"},
    type = {"Operand", "String", "Operand"},
    kind = {"Child", "Token", "Child"}
  )
  public ArithmeticExpressionNode(Operand p0, String p1, Operand p2) {
    setChild(p0, 0);
    setOPERATOR(p1);
    setChild(p2, 1);
  }
  /** @apilevel low-level 
   * @declaredat ASTNode:24
   */
  protected int numChildren() {
    return 2;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:28
   */
  public void flushAttrCache() {
    super.flushAttrCache();
  }
  /** @apilevel internal 
   * @declaredat ASTNode:32
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /** @apilevel internal 
   * @declaredat ASTNode:36
   */
  public ArithmeticExpressionNode clone() throws CloneNotSupportedException {
    ArithmeticExpressionNode node = (ArithmeticExpressionNode) super.clone();
    return node;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:41
   */
  public ArithmeticExpressionNode copy() {
    try {
      ArithmeticExpressionNode node = (ArithmeticExpressionNode) clone();
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
   * @declaredat ASTNode:60
   */
  @Deprecated
  public ArithmeticExpressionNode fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:70
   */
  public ArithmeticExpressionNode treeCopyNoTransform() {
    ArithmeticExpressionNode tree = (ArithmeticExpressionNode) copy();
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
   * @declaredat ASTNode:90
   */
  public ArithmeticExpressionNode treeCopy() {
    ArithmeticExpressionNode tree = (ArithmeticExpressionNode) copy();
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
   * Replaces the lOp child.
   * @param node The new node to replace the lOp child.
   * @apilevel high-level
   */
  public void setlOp(Operand node) {
    setChild(node, 0);
  }
  /**
   * Retrieves the lOp child.
   * @return The current node used as the lOp child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="lOp")
  public Operand getlOp() {
    return (Operand) getChild(0);
  }
  /**
   * Retrieves the lOp child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the lOp child.
   * @apilevel low-level
   */
  public Operand getlOpNoTransform() {
    return (Operand) getChildNoTransform(0);
  }
  /**
   * Replaces the lexeme OPERATOR.
   * @param value The new value for the lexeme OPERATOR.
   * @apilevel high-level
   */
  public void setOPERATOR(String value) {
    tokenString_OPERATOR = value;
  }
  /** @apilevel internal 
   */
  protected String tokenString_OPERATOR;
  /**
   * Retrieves the value for the lexeme OPERATOR.
   * @return The value for the lexeme OPERATOR.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Token(name="OPERATOR")
  public String getOPERATOR() {
    return tokenString_OPERATOR != null ? tokenString_OPERATOR : "";
  }
  /**
   * Replaces the rOp child.
   * @param node The new node to replace the rOp child.
   * @apilevel high-level
   */
  public void setrOp(Operand node) {
    setChild(node, 1);
  }
  /**
   * Retrieves the rOp child.
   * @return The current node used as the rOp child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="rOp")
  public Operand getrOp() {
    return (Operand) getChild(1);
  }
  /**
   * Retrieves the rOp child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the rOp child.
   * @apilevel low-level
   */
  public Operand getrOpNoTransform() {
    return (Operand) getChildNoTransform(1);
  }
}

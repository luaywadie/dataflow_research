/* This file was generated with JastAdd2 (http://jastadd.org) version 2.3.3 */
/**
 * @ast node
 * @declaredat Arithmetic.ast:44
 * @astdecl AssignmentNode : StatementNode ::= <ID:String> ArithmeticExpressionNode;
 * @production AssignmentNode : {@link StatementNode} ::= <span class="component">&lt;ID:{@link String}&gt;</span> <span class="component">{@link ArithmeticExpressionNode}</span>;

 */
public class AssignmentNode extends StatementNode implements Cloneable {
  /**
   * @aspect accept
   * @declaredat ASTAccept.jadd:18
   */
  Object accept(ASTVisitor v) {
        return v.visit(this);
    }
  /**
   * @declaredat ASTNode:1
   */
  public AssignmentNode() {
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
    children = new ASTNode[1];
  }
  /**
   * @declaredat ASTNode:13
   */
  @ASTNodeAnnotation.Constructor(
    name = {"ID", "ArithmeticExpressionNode"},
    type = {"String", "ArithmeticExpressionNode"},
    kind = {"Token", "Child"}
  )
  public AssignmentNode(String p0, ArithmeticExpressionNode p1) {
    setID(p0);
    setChild(p1, 0);
  }
  /** @apilevel low-level 
   * @declaredat ASTNode:23
   */
  protected int numChildren() {
    return 1;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:27
   */
  public void flushAttrCache() {
    super.flushAttrCache();
  }
  /** @apilevel internal 
   * @declaredat ASTNode:31
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /** @apilevel internal 
   * @declaredat ASTNode:35
   */
  public AssignmentNode clone() throws CloneNotSupportedException {
    AssignmentNode node = (AssignmentNode) super.clone();
    return node;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:40
   */
  public AssignmentNode copy() {
    try {
      AssignmentNode node = (AssignmentNode) clone();
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
   * @declaredat ASTNode:59
   */
  @Deprecated
  public AssignmentNode fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:69
   */
  public AssignmentNode treeCopyNoTransform() {
    AssignmentNode tree = (AssignmentNode) copy();
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
   * @declaredat ASTNode:89
   */
  public AssignmentNode treeCopy() {
    AssignmentNode tree = (AssignmentNode) copy();
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
   * Replaces the lexeme ID.
   * @param value The new value for the lexeme ID.
   * @apilevel high-level
   */
  public void setID(String value) {
    tokenString_ID = value;
  }
  /** @apilevel internal 
   */
  protected String tokenString_ID;
  /**
   * Retrieves the value for the lexeme ID.
   * @return The value for the lexeme ID.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Token(name="ID")
  public String getID() {
    return tokenString_ID != null ? tokenString_ID : "";
  }
  /**
   * Replaces the ArithmeticExpressionNode child.
   * @param node The new node to replace the ArithmeticExpressionNode child.
   * @apilevel high-level
   */
  public void setArithmeticExpressionNode(ArithmeticExpressionNode node) {
    setChild(node, 0);
  }
  /**
   * Retrieves the ArithmeticExpressionNode child.
   * @return The current node used as the ArithmeticExpressionNode child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="ArithmeticExpressionNode")
  public ArithmeticExpressionNode getArithmeticExpressionNode() {
    return (ArithmeticExpressionNode) getChild(0);
  }
  /**
   * Retrieves the ArithmeticExpressionNode child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the ArithmeticExpressionNode child.
   * @apilevel low-level
   */
  public ArithmeticExpressionNode getArithmeticExpressionNodeNoTransform() {
    return (ArithmeticExpressionNode) getChildNoTransform(0);
  }
}

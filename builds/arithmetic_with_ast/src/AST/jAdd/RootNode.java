/* This file was generated with JastAdd2 (http://jastadd.org) version 2.3.3 */
/**http://jastadd.org/web/documentation/reference-manual.php#AbstractSyntaxWhat does a .ast file do?A .ast file allows us to specify the types of nodes from which we can construct an AST. Notice thathere, we do not have any tokens that do not convey meaning. For example, in the AST,AssignmentNode only holds the ID we are assigning, and what is being assigned to it. There is nounnecessary token, such as "=". We do not need the "=" token because by (our own) definition, we knowthe semantics of an AssignmentNode.What happens when I run JastAdd tool on this file?A class is generated for each rule. In addition to a class for each rule, there are also a few otherhelper classes, and parent classes. You can see them in the parent directory (AST) to this one.The tool also generates very useful comments for these classes, so definitely go read through those.Why do we have abstract node types/when is it useful?When we have a grammar rule or subrule that contains a choice. Notice that the JastAdd syntax does nothave any mechanism for choice. Therefore, by creating this abstract parent class, we can effectivelyexpress the choice of different types of statements.For example, if you notice the grammar rule statement, it is only a choice between two other rules,thus StatementNode is abstract.Similarly, arithmeticExpression contains two choice points (for the left and right operands), so wedefine an abstract Operand node to represent this choice.This inheritance is expressed in the form child: parent ::= ...A note on the naming: I named the left hand sides (LHS) to end with Node if they correspond to a rule inthe grammar, and without Node if they don't. Also, if the RHS is just a token, I named the LHS starting witha lowercase letter. I think that this is something we should establish as a convention for the project, but Iwelcome your feedback on this.What does <SOMETHING> or <SOMETHING:TYPE> mean?It represents a nonterminal (token) that will be represented as a member of the LHS's class.<SOMETHING> is the same as <SOMETHING:String>. If the token is not a string, then you must specify the type,for example <INT:int> means that the token <INT> is of type int.What does the syntax lOp:Operand mean?It means that we are naming one of the children of the node lOp, and we are saying that it must be anOperator node. When there is only one type of a given child node, naming is optional, but it is requiredwhen there are two more children of the same type (to avoid ambiguity and naming conflicts),
 * @ast node
 * @declaredat Arithmetic.ast:42
 * @astdecl RootNode : ASTNode ::= StatementNode*;
 * @production RootNode : {@link ASTNode} ::= <span class="component">{@link StatementNode}*</span>;

 */
public class RootNode extends ASTNode<ASTNode> implements Cloneable {
  /**
   * @aspect accept
   * @declaredat ASTAccept.jadd:11
   */
  Object accept(ASTVisitor v) {
        return v.visit(this);
    }
  /**
   * @declaredat ASTNode:1
   */
  public RootNode() {
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
    setChild(new List(), 0);
  }
  /**
   * @declaredat ASTNode:14
   */
  @ASTNodeAnnotation.Constructor(
    name = {"StatementNode"},
    type = {"List<StatementNode>"},
    kind = {"List"}
  )
  public RootNode(List<StatementNode> p0) {
    setChild(p0, 0);
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
  public RootNode clone() throws CloneNotSupportedException {
    RootNode node = (RootNode) super.clone();
    return node;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:40
   */
  public RootNode copy() {
    try {
      RootNode node = (RootNode) clone();
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
  public RootNode fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:69
   */
  public RootNode treeCopyNoTransform() {
    RootNode tree = (RootNode) copy();
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
  public RootNode treeCopy() {
    RootNode tree = (RootNode) copy();
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
   * Replaces the StatementNode list.
   * @param list The new list node to be used as the StatementNode list.
   * @apilevel high-level
   */
  public void setStatementNodeList(List<StatementNode> list) {
    setChild(list, 0);
  }
  /**
   * Retrieves the number of children in the StatementNode list.
   * @return Number of children in the StatementNode list.
   * @apilevel high-level
   */
  public int getNumStatementNode() {
    return getStatementNodeList().getNumChild();
  }
  /**
   * Retrieves the number of children in the StatementNode list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the StatementNode list.
   * @apilevel low-level
   */
  public int getNumStatementNodeNoTransform() {
    return getStatementNodeListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the StatementNode list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the StatementNode list.
   * @apilevel high-level
   */
  public StatementNode getStatementNode(int i) {
    return (StatementNode) getStatementNodeList().getChild(i);
  }
  /**
   * Check whether the StatementNode list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasStatementNode() {
    return getStatementNodeList().getNumChild() != 0;
  }
  /**
   * Append an element to the StatementNode list.
   * @param node The element to append to the StatementNode list.
   * @apilevel high-level
   */
  public void addStatementNode(StatementNode node) {
    List<StatementNode> list = (parent == null) ? getStatementNodeListNoTransform() : getStatementNodeList();
    list.addChild(node);
  }
  /** @apilevel low-level 
   */
  public void addStatementNodeNoTransform(StatementNode node) {
    List<StatementNode> list = getStatementNodeListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the StatementNode list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setStatementNode(StatementNode node, int i) {
    List<StatementNode> list = getStatementNodeList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the StatementNode list.
   * @return The node representing the StatementNode list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="StatementNode")
  public List<StatementNode> getStatementNodeList() {
    List<StatementNode> list = (List<StatementNode>) getChild(0);
    return list;
  }
  /**
   * Retrieves the StatementNode list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the StatementNode list.
   * @apilevel low-level
   */
  public List<StatementNode> getStatementNodeListNoTransform() {
    return (List<StatementNode>) getChildNoTransform(0);
  }
  /**
   * @return the element at index {@code i} in the StatementNode list without
   * triggering rewrites.
   */
  public StatementNode getStatementNodeNoTransform(int i) {
    return (StatementNode) getStatementNodeListNoTransform().getChildNoTransform(i);
  }
  /**
   * Retrieves the StatementNode list.
   * @return The node representing the StatementNode list.
   * @apilevel high-level
   */
  public List<StatementNode> getStatementNodes() {
    return getStatementNodeList();
  }
  /**
   * Retrieves the StatementNode list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the StatementNode list.
   * @apilevel low-level
   */
  public List<StatementNode> getStatementNodesNoTransform() {
    return getStatementNodeListNoTransform();
  }
}

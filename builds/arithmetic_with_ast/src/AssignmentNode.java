package AST;


public class AssignmentNode extends StatementNode implements Cloneable {
    // Declared in ArithmeticParser.ast line 3

    public AssignmentNode() {
        super();

        setChild(null, 0);
    }

    // Declared in ArithmeticParser.ast line 3
    public AssignmentNode(String p0, ArithmeticExpressionNode p1) {
        setID(p0);
        setChild(p1, 0);
    }

    public Object clone() throws CloneNotSupportedException {
        AssignmentNode node = (AssignmentNode)super.clone();
    return node;
    }
    public ASTNode copy() {
      try {
          AssignmentNode node = (AssignmentNode)clone();
          if(children != null) node.children = (ASTNode[])children.clone();
          return node;
      } catch (CloneNotSupportedException e) {
      }
      System.err.println("Error: Could not clone node of type " + getClass().getName() + "!");
      return null;
    }
    public ASTNode fullCopy() {
        AssignmentNode res = (AssignmentNode)copy();
        for(int i = 0; i < getNumChild(); i++) {
          ASTNode node = getChildNoTransform(i);
          if(node != null) node = node.fullCopy();
          res.setChild(node, i);
        }
        return res;
    }
    public void flushCache() {
        super.flushCache();
    }
  protected int numChildren() {
    return 1;
  }
    // Declared in ArithmeticParser.ast line 3
    private String tokenString_ID;
    public void setID(String value) {
        tokenString_ID = value;
    }
    public String getID() {
        return tokenString_ID;
    }


    // Declared in ArithmeticParser.ast line 3
    public void setArithmeticExpressionNode(ArithmeticExpressionNode node) {
        setChild(node, 0);
    }
    public ArithmeticExpressionNode getArithmeticExpressionNode() {
        return (ArithmeticExpressionNode)getChild(0);
    }

    public ArithmeticExpressionNode getArithmeticExpressionNodeNoTransform() {
        return (ArithmeticExpressionNode)getChildNoTransform(0);
    }


    // Declared in accept.jadd at line 9

//    Object StatementNode.accept(AstVisitor v) {
//        return v.visit(this);
//    }

    Object accept(AstVisitor v) {
        return v.visit(this);
    }

}

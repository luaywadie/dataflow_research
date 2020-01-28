package AST;


public class ArithmeticExpressionNode extends StatementNode implements Cloneable {
    // Declared in ArithmeticParser.ast line 4

    public ArithmeticExpressionNode() {
        super();

        setChild(null, 0);
        setChild(null, 1);
    }

    // Declared in ArithmeticParser.ast line 4
    public ArithmeticExpressionNode(operand p0, String p1, operand p2) {
        setChild(p0, 0);
        setOPERATOR(p1);
        setChild(p2, 1);
    }

    public Object clone() throws CloneNotSupportedException {
        ArithmeticExpressionNode node = (ArithmeticExpressionNode)super.clone();
    return node;
    }
    public ASTNode copy() {
      try {
          ArithmeticExpressionNode node = (ArithmeticExpressionNode)clone();
          if(children != null) node.children = (ASTNode[])children.clone();
          return node;
      } catch (CloneNotSupportedException e) {
      }
      System.err.println("Error: Could not clone node of type " + getClass().getName() + "!");
      return null;
    }
    public ASTNode fullCopy() {
        ArithmeticExpressionNode res = (ArithmeticExpressionNode)copy();
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
    return 2;
  }
    // Declared in ArithmeticParser.ast line 4
    public void setlOp(operand node) {
        setChild(node, 0);
    }
    public operand getlOp() {
        return (operand)getChild(0);
    }

    public operand getlOpNoTransform() {
        return (operand)getChildNoTransform(0);
    }


    // Declared in ArithmeticParser.ast line 4
    private String tokenString_OPERATOR;
    public void setOPERATOR(String value) {
        tokenString_OPERATOR = value;
    }
    public String getOPERATOR() {
        return tokenString_OPERATOR;
    }


    // Declared in ArithmeticParser.ast line 4
    public void setrOp(operand node) {
        setChild(node, 1);
    }
    public operand getrOp() {
        return (operand)getChild(1);
    }

    public operand getrOpNoTransform() {
        return (operand)getChildNoTransform(1);
    }


    // Declared in accept.jadd at line 13


    Object accept(AstVisitor v) {
        return v.visit(this);
    }

}

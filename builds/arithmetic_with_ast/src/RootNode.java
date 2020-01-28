package AST;


public class RootNode extends ASTNode implements Cloneable {
    // Declared in ArithmeticParser.ast line 1

    public RootNode() {
        super();

        setChild(new List(), 0);
    }

    // Declared in ArithmeticParser.ast line 1
    public RootNode(List p0) {
        setChild(p0, 0);
    }

    public Object clone() throws CloneNotSupportedException {
        RootNode node = (RootNode)super.clone();
    return node;
    }
    public ASTNode copy() {
      try {
          RootNode node = (RootNode)clone();
          if(children != null) node.children = (ASTNode[])children.clone();
          return node;
      } catch (CloneNotSupportedException e) {
      }
      System.err.println("Error: Could not clone node of type " + getClass().getName() + "!");
      return null;
    }
    public ASTNode fullCopy() {
        RootNode res = (RootNode)copy();
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
    // Declared in ArithmeticParser.ast line 1
    public void setStatementNodeList(List list) {
        setChild(list, 0);
    }

    public int getNumStatementNode() {
        return getStatementNodeList().getNumChild();
    }

    public StatementNode getStatementNode(int i) {
        return (StatementNode)getStatementNodeList().getChild(i);
    }

    public void addStatementNode(StatementNode node) {
        List list = getStatementNodeList();
        list.setChild(node, list.getNumChild());
    }

    public void setStatementNode(StatementNode node, int i) {
        List list = getStatementNodeList();
        list.setChild(node, i);
    }
    public List getStatementNodeList() {
        return (List)getChild(0);
    }

    public List getStatementNodeListNoTransform() {
        return (List)getChildNoTransform(0);
    }


    // Declared in accept.jadd at line 2
    public Object accept(AstVisitor v) {
        return v.visit(this);
    }

}

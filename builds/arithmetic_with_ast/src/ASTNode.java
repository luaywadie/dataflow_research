package AST;



// Generated with JastAdd II (http://jastadd.cs.lth.se) version R20060729

public class ASTNode implements Cloneable {
    // Declared in null line 0

    public ASTNode() {
        super();

    }

    public Object clone() throws CloneNotSupportedException {
        ASTNode node = (ASTNode)super.clone();
    return node;
    }
    public ASTNode copy() {
      try {
          ASTNode node = (ASTNode)clone();
          if(children != null) node.children = (ASTNode[])children.clone();
          return node;
      } catch (CloneNotSupportedException e) {
      }
      System.err.println("Error: Could not clone node of type " + getClass().getName() + "!");
      return null;
    }
    public ASTNode fullCopy() {
        ASTNode res = (ASTNode)copy();
        for(int i = 0; i < getNumChild(); i++) {
          ASTNode node = getChildNoTransform(i);
          if(node != null) node = node.fullCopy();
          res.setChild(node, i);
        }
        return res;
    }
    public void flushCache() {
    }
  static public boolean IN_CIRCLE = false;
  static public boolean CHANGE = false;
  static public boolean LAST_CYCLE = false;
   static public boolean generatedWithCircularEnabled = true;
   static public boolean generatedWithCacheCycle = true;
  public ASTNode getChild(int i) {
    return getChildNoTransform(i);
  }
  private int childIndex;
  public int getIndexOfChild(ASTNode node) {
    if(node.childIndex < getNumChild() && node == getChildNoTransform(node.childIndex))
      return node.childIndex;
    for(int i = 0; i < getNumChild(); i++)
      if(getChildNoTransform(i) == node) {
        node.childIndex = i;
        return i;
      }
    return -1;
  }

  public void addChild(ASTNode node) {
    setChild(node, getNumChild());
  }
  public ASTNode getChildNoTransform(int i) {
    return children[i];
  }
  protected ASTNode parent;
  protected ASTNode[] children;
  protected int numChildren;
  protected int numChildren() {
    return numChildren;
  }
  public int getNumChild() {
    return numChildren();
  }
  public void setChild(ASTNode node, int i) {
    if(children == null) {
      children = new ASTNode[i + 1];
    } else if (i >= children.length) {
      ASTNode c[] = new ASTNode[i << 1];
      System.arraycopy(children, 0, c, 0, children.length);
      children = c;
    }
    children[i] = node;
    if(i >= numChildren) numChildren = i+1;
    if(node != null) { node.setParent(this); node.childIndex = i; }
  }
  public void insertChild(ASTNode node, int i) {
    if(children == null) {
      children = new ASTNode[i + 1];
      children[i] = node;
    } else {
      ASTNode c[] = new ASTNode[children.length + 1];
      System.arraycopy(children, 0, c, 0, i);
      c[i] = node;
      if(i < children.length)
        System.arraycopy(children, i, c, i+1, children.length-i);
      children = c;
    }
    numChildren++;
    if(node != null) { node.setParent(this); node.childIndex = i; }
  }
  public ASTNode getParent() {
    return parent;
  }
  public void setParent(ASTNode node) {
    parent = node;
  }
}

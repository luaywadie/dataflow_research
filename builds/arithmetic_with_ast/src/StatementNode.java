package AST;


public abstract class StatementNode extends ASTNode implements Cloneable {
    // Declared in ArithmeticParser.ast line 2

    public StatementNode() {
        super();

    }

    public Object clone() throws CloneNotSupportedException {
        StatementNode node = (StatementNode)super.clone();
    return node;
    }
    public void flushCache() {
        super.flushCache();
    }
  protected int numChildren() {
    return 0;
  }
}

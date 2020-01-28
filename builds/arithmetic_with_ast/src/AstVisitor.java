package arithmetic.example;

interface AstVisitor<T> {
    T visit(RootNode node);
//    public abstract T visit(StatementNode node);
    T visit(AssignmentNode node);
    T visit(ArithmeticExpressionNode node);
    T visit(StatementNode node);
}
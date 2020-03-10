/**
 * The interface that all visitors must implement. The interface has a generic type parameter because
 * different visitors may return objects of varying types.
 * If new AST node types are added (via Arithmetic.ast), then the according method signatures will need
 * to be added here.
 *
 * @param <T> The return type of the operation; different operations will require different return types
 *
 * What's the difference between ASTVisitor and ArithmeticVisitor?
 *           ASTVisitor is the visitor interface for the AST nodes (and will be written by us), whereas
 *           ArithmeticVisitor is the visitor interface for the CST nodes (and is generated by ANTLR).
 */
interface ASTVisitor<T> {
    T visit(RootNode node);

    T visit(AssignmentNode node);

    T visit(ArithmeticExpressionNode node);
}
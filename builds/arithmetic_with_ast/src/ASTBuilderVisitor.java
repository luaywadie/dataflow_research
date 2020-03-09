/* TODO: This not the correct way to implement the visitor pattern, so this must be
updated.
 */

/**
 * Creates AST nodes (whose classes are generated by JastAdd) from CST nodes
 * (whose classes are generated by ANTLR and end in Context)
 */
public class ASTBuilderVisitor extends ArithmeticBaseVisitor<ASTNode> {
    /*
       root node is constructed and subsequent calls to visitStatement will return either
       AssignmentNodes or ArithmeticExpressionNodes, which will be added to the ArrayList
       called statementList of the root node
     */
    @Override
    public RootNode visitRoot(ArithmeticParser.RootContext ctx) {
        System.out.println("Visiting Root");
        RootNode rootNode = new RootNode();
        List statementNodeList = new List();
        // call visit on each statement
        int numStatements = ctx.statement().size();
        // visits each statement
        for (int i = 0; i < numStatements; i++) {
            statementNodeList.add(visitStatement(ctx.statement(i)));
        }
        rootNode.setStatementNodeList(statementNodeList);
        return rootNode;
    }

    /* here no nodes will be constructed directly, but nodes will be constructed indirectly
       by the calls to visitAssignment or visitArithmeticExpression
     */
    @Override
    public StatementNode visitStatement(ArithmeticParser.StatementContext ctx) {
        System.out.println("Visiting Statement");
        // decide whether node is assignment or arithmeticExpression and visit according to this
        if (ctx.assignment() != null) { // visit an assignment
            return visitAssignment(ctx.assignment());
        } else if (ctx.arithmeticExpression() != null)  { // visit an arithmeticExpression
            return visitArithmeticExpression(ctx.arithmeticExpression());
        } else {
            throw new RuntimeException("Something has gone wrong");
        }
    }

    @Override
    public StatementNode visitAssignment(ArithmeticParser.AssignmentContext ctx) {
        System.out.println("Visiting Assignment");
        String id = ctx.ID().getText();
        ArithmeticExpressionNode arithmeticExpression = (ArithmeticExpressionNode) visitArithmeticExpression(ctx.arithmeticExpression());
        return new AssignmentNode(id, arithmeticExpression);
    }

    @Override
    @SuppressWarnings("null")
    public StatementNode visitArithmeticExpression(ArithmeticParser.ArithmeticExpressionContext ctx) {
        System.out.println("Visiting ArithmeticExpression");
        System.err.println(ctx);
        String operator = ctx.OPERATOR().getText();

        // unwrap String values from Token for each operand
        /* TODO: There's got to be a way to determine the type of operand from the ArithmeticExpressionContext class, find out how.
         */
        String lOpToken = ctx.lOp.getText();
        String rOpToken = ctx.rOp.getText();
        System.out.println("LEN: " + rOpToken.length());
        Operand rOp = null;
        Operand lOp = null;
        // determine types of each operand by what type of character they begin with
        // this is important when evaluating the expression
        if (Character.isLetter(rOpToken.charAt(0))) {
            rOp = new idOp(rOpToken);
        } else if (Character.isDigit(rOpToken.charAt(0))) {
            rOp = new intOp(Integer.parseInt(rOpToken));
        } else throw new RuntimeException("rOp is not a valid operand - " + rOp);

        if (Character.isLetter(lOpToken.charAt(0))) {
            lOp = new idOp(lOpToken);
        } else if (Character.isDigit(lOpToken.charAt(0))) {
            lOp = new intOp(Integer.parseInt(lOpToken));
        } else throw new RuntimeException("lOp is not a valid operand - " + lOp);

//        return new ArithmeticExpressionNode(operator, lOpType, rOpType, lOp, rOp);
        return new ArithmeticExpressionNode(lOp, operator, rOp);
    }
}
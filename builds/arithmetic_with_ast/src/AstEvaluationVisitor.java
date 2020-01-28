package arithmetic.example;

import java.util.HashMap;

public class AstEvaluationVisitor implements AstVisitor<Integer> {
    static HashMap<String, Integer> varTable = new HashMap<>();
    private static int indentLevel = 1;

    public Integer visit(RootNode node) {
        System.out.println("--Begin Evaluation at Root--");
        for (int i = 0; i < node.getNumStatementNode(); i++) {
            node.getStatementNode(i).accept(this);
        }
        return null;
    }

//    public Integer visit(StatementNode node) {
//        if (node instanceof AssignmentNode) {
//            return visit((AssignmentNode) node);
//        } else if (node instanceof ArithmeticExpressionNode) {
//            return visit((ArithmeticExpressionNode) node);
//        }
//        return null;
//    }

    public Integer visit(StatementNode node) {
        if (node instanceof AssignmentNode) {
            return (Integer) ((AssignmentNode)node).accept(this);
        } else if (node instanceof ArithmeticExpressionNode) {
            return visit((ArithmeticExpressionNode) node);
        }
        return null;
    }

    public Integer visit(AssignmentNode node) {
        for (int i = 0; i < indentLevel; i++) System.out.println("\t");
        System.out.println("ASSIGN to var " + node.getID() + ":");
        // evaluate the expression to determine variable value
        indentLevel++;
        Integer assignValue = visit(node.getArithmeticExpressionNode());
        indentLevel--;
        // bind variable & value
        varTable.put(node.getID(), assignValue);
        for (int i = 0; i < indentLevel; i++) System.out.print("\t");
        System.out.println("ASSIGN: The value " + assignValue + " has been assigned to the variable " + node.getID());
        System.out.println();
        return null;
    }

    public Integer visit(ArithmeticExpressionNode node) {
        Integer lOpValue = null;
        Integer rOpValue = null;
        Integer value = null;

        if (node.getlOp() instanceof intOp) { // integer
            intOp lOp = (intOp)node.getlOp();
            lOpValue = Integer.parseInt(lOp.getINT());
            for (int i = 0; i < indentLevel; i++) System.out.print("\t");
            System.out.println("Left op is an INT: " + lOpValue);
        } else if (node.getlOp() instanceof idOp) { // ID (variable)
            idOp lOp = (idOp)node.getlOp();
            String id = lOp.getID();
            // need to get value from a symbol table
            if ((value = varTable.get(id)) != null) {
                lOpValue = value;
            } else {
                for (int i = 0; i < indentLevel; i++) System.err.print("\t");
                System.err.println("No value has been defined for " + id + ". Program will end now.");
                System.exit(-1);
            }
            for (int i = 0; i < indentLevel; i++) System.out.print("\t");
            System.out.println("Left op is an ID: " + id + " = " + lOpValue);
        }
        // now the right operand
        if (node.getrOp() instanceof intOp) { // integer
            intOp rOp = (intOp)node.getrOp();
            rOpValue = Integer.parseInt(rOp.getINT());
            for (int i = 0; i < indentLevel; i++) System.out.print("\t");
            System.out.println("Left op is an INT: " + lOpValue);
        } else if (node.getrOp() instanceof idOp) { // ID (variable)
            idOp rOp = (idOp)node.getrOp();
            String id = rOp.getID();
            // need to get value from a symbol table
            if ((value = varTable.get(id)) != null) {
                rOpValue = value;
            } else {
                for (int i = 0; i < indentLevel; i++) System.err.print("\t");
                System.err.println("No value has been defined for " + id + ". Program will end now.");
                System.exit(-1);
            }
            for (int i = 0; i < indentLevel; i++) System.out.print("\t");
            System.out.println("Right op is an ID: " + id + " = " + rOpValue);
        }

        // determine the operation to perform
        switch (node.getOPERATOR()) {
            case "+": case "plus":
                value = lOpValue + rOpValue;
                break;
            case "-": case "minus":
                value = lOpValue - rOpValue;
                break;
            case "*": case "times":
                value = lOpValue * rOpValue;
                break;
            case "/": case "divby":
                value = lOpValue / rOpValue;
                break;
            case "%": case "mod":
                value = lOpValue % rOpValue;
                break;
            case "^": case "pow":
                if (rOpValue < 1) {
                    for (int i = 0; i < indentLevel; i++) System.err.print("\t");
                    System.err.println("Exponent must be positive and greater than 0: use division instead of negative exponent: -exiting");
                    System.exit(-1);
                }
                value = pow(lOpValue, rOpValue, 1);
        }
        for (int i = 0; i < indentLevel; i++) System.out.print("\t");
        System.out.println("Expression evaluated: " + lOpValue + node.getOPERATOR() + rOpValue + " = " + value);
        return value;
    }

    private int pow(int base, int exp, int startVal) {
        if (exp  == 0) {
            return startVal;
        } else {
            return pow(base, exp-1, base*startVal);
        }
    }
}
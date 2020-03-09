import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter filename:");
        String fileName = scanner.next();
        scanner.close();
        try {
            // use file for source of CharStream, which will be used by the lexer
            CharStream stream = CharStreams.fromFileName(fileName);
            ArithmeticLexer lexer = new ArithmeticLexer(stream);
            // provides tokens to parser
            BufferedTokenStream tokenStream = new BufferedTokenStream(lexer);
            ArithmeticParser parser = new ArithmeticParser(tokenStream);
            // get the root CST node
            ArithmeticParser.RootContext cstRoot = parser.root();
            // build the AST by visiting the cstRoot, which will descend the CST
            ASTBuilderVisitor astBuilder = new ASTBuilderVisitor();
            // begin building the AST from the root CST node
            RootNode astRoot = (RootNode) cstRoot.accept(astBuilder);
            System.out.println();
            // evaluate, beginning at the root node and recursively descending through the AST
            ASTEvaluationVisitor evaluator = new ASTEvaluationVisitor();
            astRoot.accept(evaluator);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }
}

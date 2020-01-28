import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Run {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter filename:");
        String fileName = scanner.next();
        scanner.close();
        try {
            // use file for source of CharStream, which will be used by the lexer
            CharStream stream = CharStreams.fromFileName(fileName);
            ArithmeticLexer lexer = new ArithmeticParserLexer(stream);
            // provides tokens to parser
            BufferedTokenStream tokenStream = new BufferedTokenStream(lexer);
            ArithmeticParser parser = new ArithmeticParserParser(tokenStream);
            // get the root CST node
            ArithmeticParser.RootContext cstRoot = parser.root();
            // build the AST by visiting the cstRoot, which will descend the CST
            AstBuilderVisitor astBuilder = new AstBuilderVisitor();
            // OLD RootNode astRoot = astBuilder.visitRoot(cstRoot);
            // NEW: build using accept
            RootNode astRoot = (RootNode) cstRoot.accept(astBuilder);
            System.out.println();
            // now evaluate beginning with the root, which will call Visit on the children nodes
            AstEvaluationVisitor evaluator = new AstEvaluationVisitor();
            // OLD evaluator.Visit(astRoot);
            // NEW: evaluate using accept
            astRoot.accept(evaluator);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }
}
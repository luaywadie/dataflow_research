:: Executes the procedure to compile a .g4 file and executes (runs) the results
:: Compile G4
:: Compile Java
:: %1 => First Argument

call antlr4 %1.g4
call javac %1*.java

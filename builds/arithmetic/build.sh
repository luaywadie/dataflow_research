#!/usr/bin/env bash

rm *.java
rm *.class
antlr4 $1.g4
javac $1*.java
java -Xmx500M -cp "/usr/local/lib/antlr-4.7.1-complete.jar:$CLASSPATH" org.antlr.v4.gui.TestRig $1 $2 $3

- <b>Inline Java Grammar</b>
  - <b>@header</b>: Declares unique imports and package name.
     ```
       @header {
         package research

         import Datetime
         import Pandas as pd
       }
     ```
  - <b>@members</b>: Declares inline scripting when compiling from a lexer.
     ```
       @members {
         int count = 0;
         count += 1;
         System.out.println(count);
       }
     ```
    - We can use declared tokens inside from rules directly.
     ```
       @members {
        public boolean isPositive(int a) { if (a >= 0) ? return true : return false;}; 
       }
       
       eval : a=NUMBER { System.out.println(isPositive($a.int)); };
       
       NUMBER : [0-9]+
     ```

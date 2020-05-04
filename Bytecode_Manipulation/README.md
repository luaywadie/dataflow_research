# Bytecode extractor via javap command
This is a very basic procedure to release the **bytecode** from specific java class file(s).
- Navigate to the directory of the .class file.
- Enter this command: `javap -cp -p FileName.class`. Replace _FileName_ with your class name.
  - `-cp` is for Classpath, this is not required if you have an environment CLASSPATH already preset.
  - `-p` exposes the private methods within that .class file
- Alternatively, if you want to store the output in a local text file, add: ` > Bytecode_Result.txt` at the end of the javap command.

For full option parameters and other useful commands: https://docs.oracle.com/javase/8/docs/technotes/tools/windows/javap.html

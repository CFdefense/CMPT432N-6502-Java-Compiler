<h2 align="center">6502 Microprocessor Compiler That Translates High-Level Code into Executable 6502 Microprocessor Machine Code</h3>  
<h3 align="center">Project Demo</h4>
<div align="center">
    <img src="https://github.com/CFdefense/CFdefense.github.io/blob/main/public/media/projects/compiler.gif?raw=true" alt="Description of the GIF" width="750">
</div>
 <h3 align="center">Project Information</h4>
 <p align="center">
  I started and completed this project during my free time while interning at Avangrid Networks in the summer of 2024. During my freshman year, my upperclassmen friends could not stop talking about this class and the hardships it brought them. As such I thought I would give it my best and try to push myself to complete the class independently of any teaching. Originally this class is taught during the junior or senior year of a CS degree at Marist College. It is titled 'Design of Compilers' and is taught in person by Professor Labouseur. I want to give a big shout-out to Alan Labouseur who provided all of the materials and study information I needed to climb the mountain that was this project free of charge on <a href="https://www.labouseur.com/courses/compilers/"><strong>his website</strong></a>
  <br />
  <br />
My goal when starting this project was to gain a deeper knowledge and skill with Java through a large-scale and difficult project. This compiler is currently by far my largest project and has consumed many many hours of my life, however, as a result, I can say this project is the one I am most proud of. I created this functional 6502 microprocessor compiler through hours of headache, self-study, and determination. 
 </p>
 <br />
  <h3 align="center">Project Overview and Limitations</h3> 
  <p align="center">
  This Project is a 6502 Microprocessor Compiler, the 6502 microprocessor is an 8-bit microprocessor that was widely used in the late 1970s and 1980s. Developed by MOS Technology, it became one of the most popular CPUs for home computers, video game consoles, and embedded systems. The 6502 Microprocessor is most notably the processor used in the Atari 2600 and Nintendo NES. The purpose of a Compiler is to translate high-level language that is easy for developers into machine code that the machine (in our case the 6502 microprocessor) can better understand and execute. 
  <br />
  <br />
   This project is split into 4 unique sub-projects, each a segment of the compiler that builds on the last segments. The final project is the FINAL complete version of the compiler. This setup was very intuitive and made each aspect of the compiler easy to comprehend while still challenging to implement. Along with each sub-project, I assembled my own set of test programs to test various edge cases and functionality. Each test has a unique purpose indicated by its name and was designed to check both the continued successful functionality of previous sub-projects and the current one being built.
   <br />
   <br />
   Due to the complexity of a Compiler and the vast amount of differing ways in which we can write high-level language, it becomes necessary for the sake of realistic expectations to set limitations on ourselves for this project. These limitations will help us create an intuitive compiler that will be both amazing and practical in the sense that it can be reasonably created in a few months time. To create this perfect balance, Professor Labouseur restricted the acceptable high-level grammar that this compiler will allow for use. <a href="https://www.labouseur.com/courses/compilers/grammar.pdf"><strong>See Acceptable Grammer Here</strong></a>
  </p>
  <br />
  <h3 align="center">Lets Dive Into The Compiler!</h3> 
  <h4 align="center">How to Execute</h4> 
  <p align="center">
   Before we start, for any of the sub-projects, executing is as simple as copying the full path of the testName.txt test file, running the project, and pasting the path where prompted.
  <br />
   <pre><code class="language-shell">
  PS C:\Users\You\EpicCompiler>
   <br />
  Enter File Input for Testing Lexer C:\Users\You\EpicCompiler\Project4\testfiles\testName.txt
  </code></pre>
  </p>
  <br />
  <h4 align="center">Project One: Lexical Analysis</h4> 
  <p align="center">
   <a href="https://www.labouseur.com/courses/compilers/project1.pdf">Project One</a> starts the compiler off with the lexer, the lexer's job is to run lexical analysis which is the first check our high-level language will have to pass. Lexical analysis involves checking every single character written in our high-level language for the creation of a token stream. A token can be a word such as <strong>string</strong> or <strong>int</strong> or a character such as <strong>a</strong> or <strong>{</strong> Checking means to compare the words and characters in our high-level language to our acceptable grammar. This might seem easy, however, how can this be done effectively? and how will we differentiate between a character and a word? The solution to this question is the main challenge of this first project. This can be solved using regular expressions aka Regex. Regex is a powerful and efficient import that can be used for pattern recognition, ie it will recognize an <strong>I</strong> followed by an <strong>N</strong> and then a <strong>T</strong> as INT instead of each individual character. Using this tool we read through our imported high-level language and can form accurate tokens. Regex also has another great purpose in identifying bad characters, in such an event we can throw errors to the terminal for changes to be made in the high-level language. Following the completion of this project will we have a functioning lexer that can correctly convert high-level language into a stream of useful tokens, ignoring spaces and throwing errors for any unwanted symbols. We are now ready to move on to parsing.
  </p>
  <br />
  <h4 align="center">Project Two: Parsing </h4> 
   <p align="center">
   <a href="https://www.labouseur.com/courses/compilers/project2.pdf">Project Two</a> continues with parsing which is the next obstacle for our high-level code to overcome. Parsing involves the process of validating tokens in the token stream. For example, to parse we think of our high-level language as a sentence. A sentence is defined to have a subject-verb pair, a complete thought, a capital letter in the beginning, and a period in the end. When we parse we look to ensure our code follows its intended structure. For example, as per our grammar, a Program must be made up of a <strong>Block</strong> and an end-of-program <strong>$</strong>. To continue, a <strong>block</strong> is defined to start with a <strong>{</strong>, followed by a <strong>statement list</strong> and concluded with a <strong>}</strong>. The process of parsing is the consuming of our token stream and validation that these definitions are met. If at any point an expected character or structure is not present it will throw an error for noncompliance with the grammar structure. In addition to this checking of valid structure, our consumed tokens are translated into a Concrete Syntax Tree, which is a tree representation of the parsed tokens. Our <strong>Program Token</strong> stands at the top followed by its respective children and then its children's children. This tree will serve as a more contextual representation of the previous token stream and will help us further down the line. The main challenge of this project was the recursive aspect of its programming. Both the CST and the parsing process required a strong understanding of call stacks and tree traversals (Thank you CMPT435). Debugging was my best friend during this project and for the most part of the compiler. Debugging is amazing. Following the completion of the parser, we now have translated our token stream into a contextualized concrete syntax tree and checked to ensure the structure of our code was up to par with our defined grammar, throwing any errors/warnings if not.
  </p>
  <br />
  <h4 align="center">Project Three: Semantic Analysis</h4> 
 <p align="center">
   <a href="https://www.labouseur.com/courses/compilers/project3.pdf">Project Three</a> begins with more tree traversal. First, we convert our <strong>CST</strong> into an Abstract Syntax Tree <Strong>(AST)</Strong>. The AST is as the name implies, Abstract, there is minimal context and we only keep "the good stuff" - Professor Labouseur. Converting our CST to an AST allows for a more easy interpretation of types and scopes aka <strong>Semantic Analysis</strong>. Semantic Analysis is the next and last big test our high-level language must endure. Semantic Analysis will check our variables for type comparisons and assignments, ensuring we are comparing similar types and assigning correct types. In addition, we will scope-check these variables to make sure they are not being re-declared, used outside of their scope, and a few other errors. As semantic analysis is conducted we will create a table of these variables and their information into a <strong>Symbol Table</strong> allowing for quick interpretation after analysis. Following Semantic Analysis we will have an AST, Symbol Table, and will have ensured our variables, assignments, and conversions are all type-checked and scope-checked. Finally, with confidence in the quality and stability of our high-level code, we can now convert it into 6502 microprocessor machine code.
  </p>
  <br />
  <h4 align="center">Project Four: Code Generation</h4> 
  <p align="center">
   <a href="https://www.labouseur.com/courses/compilers/project4.pdf">Project Four</a> brings us to the end, finally! However, for me, this was the hardest project by far. I believe this was because I was self-studying this course without anyone to turn to for advice. As such the only materials I had to go off of were Professor Labouseur's examples. Edge cases and ambiguity made this final part extremely challenging and I can remember multiple times where I felt as though I was going crazy trying to figure this project out. The final project involves converting your abstract syntax tree into 6502 Microprocessor machine code that is executable in a 6502 Microprocessor Operating System. The OS I used to test my machine code was <a href="https://www.labouseur.com/commondocs/operating-systems/SvegOS/public_html/index.html">SvegOS</a>. In order to understand how to accomplish this conversion of AST to opcode it is imperative that one understands computer architecture concepts such as CPU cycling, opcode execution, and byte/bit memory manipulation. Fortunately, my previous Chip-8 Interpreter project allowed me to learn and experience the hardships of these concepts earlier on. From there we must understand the 6502 microprocessor's assembly and machine languages. Professor Labousuer provides a small segment of the language, just enough needed for our grammar <a href="https://www.labouseur.com/commondocs/6502alan-instruction-set.pdf">seen here</a>. From here creating these conversions is not too hard, each unique case is handled by its associated opcodes and register manipulations. There is one final check our high-level code must pass, however, and that is memory allocation. Checking for heap and stack overloads is crucial to prevent memory overwriting and code misfunctions. Finally, after the completion of this fourth sub-project, we will have 256 bytes of executable 6502 microprocessor machine code which we execute in our SvegOS Operating System. We are Done!
  </p>
  <br />
  <h4 align="center">Executing Machine Code in SvegOS</h4> 
  <p align="center">
  Upon successful code generation, the compiler will display the 256-byte memory representation of your high-level language code. This memory is divided into three segments of memory, the code, the stack, and the heap. In order to execute the 6502 compiled code, we will need a 6502 operating system that can properly interpret and execute the op-codes we have generated in our 256-byte memory. For the sake of clarity, we will stick to <a href="https://www.labouseur.com/commondocs/operating-systems/SvegOS/public_html/index.html">SvegOS</a>. Sveg OS is an operating system created in alignment with Professor Labouseur's Operating Systems class, however, just about any 6502 Operating Systems should theoretically be able to execute our generated code. 
  <br />
   <br />
   <strong>Step 1: Start the operating system</strong>
   <br />
   <p align="center">
  <img src="https://i.imgur.com/30aPL3R.png" alt="img1" width="700" height="450">
</p>
<br />
<p align = "center">
<strong>Step 2: Paste the compiled 256 bytes of code into the bottom right box</strong>
</p>
   <p align="center">
  <img src="https://i.imgur.com/pJjjHxR.png" alt="img2" width="700" height="450">
</p>
<br />
<p align = "center">
 <strong>Step 3: Execute "load" to load the 256 bytes of machine code into the operating system's memory, you will be provided with a number that is assigned to that operation</strong>
</p>
   <p align="center">
  <img src="https://i.imgur.com/7BFEtXN.png" alt="img3" width="700" height="450">
</p>
<br />
<p align = "center">
 <strong>Step 4: Execute "run" followed by the assigned operation number. This will execute the stored machine code. In this case, the machine code printed 12 and then DONE</strong>
</p>
   <p align="center">
  <img src="https://i.imgur.com/HpicYih.png" alt="img4" width="700" height="450">
</p>
<h4 align="center">Thanks For Reading</h4>
<h4 align="center">LaTeX Writeups, Labs, and Some Code Adjustments May be Required For Class Completion</h4>

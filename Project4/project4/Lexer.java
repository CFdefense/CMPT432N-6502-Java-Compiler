/*
    Lexer
    Takes an input and returns an ordered stream of tokens
    Perform lexical analysis on input programs and return an ordered stream of tokens
*/

package project4;

import java.io.File;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    // Private Object Instance Variables
    private ArrayList<String> fileLines; // array to store each file line
    private ArrayList<Token> myTokens;  // array to store a single program tokens
    private ArrayList<ArrayList<Token>> myTotalTokens; // 2D array to hold each programs tokens mainly used for debugging and program count
    private boolean inComment; // to determine if we are reading in a comment
    private boolean inQuotes; // to determin if we are reading in a quote
    private int errorCount; // running total of errors for a program
    private int warningCount; // running total of warnings for a program
    private boolean foundEnd; // boolean to determine if weve hit an EOP
    private boolean foundNew; // boolean to determine if were starting a new program
    private Parser myParser; // Parser instance to begin next step of compiling
    private int myProgramNumber; // Hold Program # being parsed
    
    //! Begin Lexer Construction and Manipulation

    public Lexer() {
        this.fileLines = new ArrayList<String>();
        this.myTokens = new ArrayList<Token>();
        this.myTotalTokens = new ArrayList<ArrayList<Token>>();
        this.inComment = false;
        this.inQuotes = false;
        this.errorCount = 0;
        this.warningCount = 0;
        this.foundEnd = false;
        this.foundNew = true;
        this.myParser = new Parser();
        this.myProgramNumber = 0;
    }
    
    // Method for reading in a file
    public boolean readFile(String fileName) {
        // Instance Variables
        boolean result; 

        // try to read in the file name
        try {
            File myFile = new File(fileName);
            String lineData;
            Scanner myScanner = new Scanner(myFile);

            // Read-in File and add to our Arraylist
            while(myScanner.hasNextLine()) {
                lineData = myScanner.nextLine();
                this.fileLines.add(lineData);
            }

            // Clean up Scanner Object
            myScanner.close();  

            // return a successful read
            result = true;

        // Catch errors and display a unsuccessful read
        } catch (FileNotFoundException error) {
            System.out.println("An error occured.");
            error.printStackTrace();
            result = false;
        }
        return result;
    }

    // Method to clear our lexer 
    public void clearLexer() {
        this.myTokens.clear();
        this.inComment = false;
        this.inQuotes = false;
        this.errorCount = 0;
        this.warningCount = 0;
        this.foundNew = true;
        this.foundEnd = false;
        this.myParser.reset();
        this.myProgramNumber = 0;
    }

    //! End of Lexer Construction and Manipulation

    //! Start Lexical Analysis (Scanner)

    public void lexicalAnalysis() {
        
        // Define our Grammer to be used by regex pattern detection
        String ids = "[a-z]"; // regex a through z 
        String digits = "[0-9]+"; // regex 0 through 9
        String chars = "[a-z]+"; // regex one or more a through z NO DIGITS
        String keywords = "(print|while|if|int|string|boolean|true|false)"; // regex or
        String symbols = "(\\$|\\{|\\}|\\(|\\)|\\==|\\!=|\\=|\\+)"; // regex or
        String whiteSpace = "\s"; // regex whitespace
        String comments = "(\\/\\*|\\*\\/)"; // regex or
        String quotes = "\""; // regex quote
        String badChars = "[A-Z!$@#%^&*;:<>?-_/~`|\\\\]"; // regex A through Z and Special Characters

        String tokenID = ""; // descriptive 'type' of token

        // Compile all Grammer and define our pattern
        String totalGrammer = keywords + "|" + ids + "|" + symbols + "|" + digits + "|" + chars + "|" + whiteSpace + "|" + comments + "|" + quotes + "|" + badChars;
        Pattern grammer = Pattern.compile(totalGrammer);

        // Determine token and Build in order [Keyword -> id -> symbol -> digit -> char]
        int lineNumber; // line being read
        String lastMatch = ""; // last match to be used to check for final EOP

        // for each line
        for(lineNumber = 0; lineNumber < fileLines.size(); lineNumber++) {
            // determine matches which exist in the current line
            Matcher match = grammer.matcher(fileLines.get(lineNumber));

            // Determine program # and display to user
            if(this.foundNew) {
                System.out.println("\nRUNNING LEXICAL ANALYSIS ON PROGRAM # " + (myTotalTokens.size() + 1) + "\n");
                this.myProgramNumber = myTotalTokens.size() + 1;
                this.foundNew = false;
            }

            // Continue while we have a match
            while(match.find()) { // finds next match                System.out.println(match.find());
                // Define the match and update our last match
                String myMatch = match.group();
                lastMatch = myMatch;

                // The Match is a KEYWORD
                if(match.group().matches(keywords) && !inComment && !inQuotes)  {
                    // Determine which keyword has been matched
                    switch (myMatch) {
                        case "print":
                            tokenID = "T_PRINT";
                            break;
                        case "while":
                            tokenID = "T_WHILE";
                            break;
                        case "if":
                            tokenID = "T_IF";
                            break;
                        case "int":
                            tokenID = "T_INT";
                            break;
                        case "string":
                            tokenID = "T_STRING";
                            break;
                        case "boolean":
                            tokenID = "T_BOOLEAN";
                            break;
                        case "true":
                            tokenID = "T_TRUE";
                            break;
                        case "false":
                            tokenID = "T_FALSE";
                            break;
                    }

                    // create token
                    Token newKeywordToken = new Token(tokenID, myMatch, lineNumber + 1);
                    myTokens.add(newKeywordToken);
                    System.out.println("LEXER --> | " + tokenID + " [ " + myMatch + " ] on line " + (lineNumber) + "...");
                }

                // The Match is an ID
                else if(match.group().matches(ids) && !inComment && !inQuotes) {
                    tokenID = "T_ID";

                    // create token
                    Token newIDToken = new Token(tokenID, myMatch, lineNumber + 1);
                    myTokens.add(newIDToken);
                    System.out.println("LEXER --> | " + tokenID + " [ " + myMatch + " ] on line " + (lineNumber) + "...");
                }

                // The Match is a SYMBOL
                else if(match.group().matches(symbols) && !inComment) {
                    switch(myMatch) {
                        case "$":
                            tokenID = "T_EOPS";

                            // Mark End of Program
                            this.foundEnd = true;

                            // check for more warnings due to comments or quotes
                            if(inComment) {
                                System.out.println("ERROR PROGRAM ENDED IN A COMMENTED - MISSING END COMMENT - '*/'");
                                System.out.println("NO EOP DETECTED");
                                this.warningCount++;
                            } else if(inQuotes) {
                                System.out.println("ERROR PROGRAM ENDED IN A QUOTE - MISSING END QUOTE");
                                System.out.println("NO EOP DETECTED");
                                this.errorCount++;
                            } else {
                                // Create Token if the $ does not exist in a comment or quote
                                Token newSymbolToken = new Token(tokenID, myMatch, lineNumber + 1);
                                myTokens.add(newSymbolToken);
                                System.out.println("LEXER --> | " + tokenID + " [ " + myMatch + " ] on line " + (lineNumber) + "...");
                            }
                            continue; // proceed to next line
                        case "{":
                            tokenID = "T_OPENING_BRACE";
                            break;
                        case "}":
                            tokenID = "T_CLOSING_BRACE";
                            break;
                        case "(":
                            tokenID = "T_OPENING_PARENTHESIS";
                            break;
                        case ")":
                            tokenID = "T_CLOSING_PARENTHESIS";
                            break;
                        case "==":
                            tokenID = "T_EQUALITY_OP";
                            break;
                        case "!=":
                            tokenID = "T_INEQUALITY_OP";
                            break;
                        case "+":
                            tokenID = "T_ADDITION_OP";
                            break;
                        case "=":
                            tokenID = "T_ASSIGN_OP";
                            break;
                    }

                    // Create token for SYMBOL if it is not EOP and does NOT appear in quotes
                    if(!this.foundEnd) {
                        if(inQuotes) {
                            System.out.println("WARNING UNAUTHORIZED CHARACTER [ " + myMatch + " ] in quotes - at line " + (lineNumber + 1) + "...");
                            this.warningCount++;
                        } else {
                            Token newSymbolToken = new Token(tokenID, myMatch, lineNumber + 1);
                            myTokens.add(newSymbolToken);
                            System.out.println("LEXER --> | " + tokenID + " [ " + myMatch + " ] on line " + (lineNumber) + "...");
                        }
                    }
                }

                // The Match is a DIGIT
                else if(match.group().matches(digits) && !inComment) {
                    tokenID = "T_DIGIT";
                    
                    // if the digit exists in quote throw an error to prevent parsing errors later
                    if(inQuotes) {
                        System.out.println("ERROR - UNAUTHORIZED DIGIT(S) IN QUOTES [ " + myMatch + " ] at line " + (lineNumber +1) + "..." );
                        this.errorCount++;
                    // if the digit is not in quotes add normally
                    } else {
                        // Create Token for digit
                        Token newDigitToken = new Token(tokenID, myMatch, lineNumber + 1);
                        myTokens.add(newDigitToken);
                        System.out.println("LEXER --> | " + tokenID + " [ " + myMatch + " ] on line " + (lineNumber) + "...");
                    }
                }

                // The Match is a CHAR
                else if(match.group().matches(chars) && inQuotes && !inComment) {
                    // A Char must be in quotes because we already checked IDs
                    tokenID = "T_CHAR";
                    
                    // create token for each letter -> good for "string etc"
                    for(int i = 0; i < myMatch.length(); i++) {
                        Token newCharToken = new Token(tokenID, String.valueOf(myMatch.charAt(i)), lineNumber + 1);
                        myTokens.add(newCharToken);
                        System.out.println("LEXER --> | " + tokenID + " [ " + String.valueOf(myMatch.charAt(i)) + " ] on line " + (lineNumber) + "...");
                    }
                    
                }

                // The Match is a WHITESPACE
                else if(match.group().matches(whiteSpace) && inQuotes && !inComment) {
                    // only create a token for a whitespace that exists in quotes
                    tokenID = "T_SPACE";
                    Token newCharToken = new Token(tokenID, myMatch, lineNumber + 1);
                    myTokens.add(newCharToken);
                    System.out.println("LEXER --> | " + tokenID + " [ " + myMatch + " ] on line " + (lineNumber) + "...");
                }

                // The Match is a QUOTE
                else if(match.group().matches(quotes) && !inComment) {
                    tokenID = "T_QUOTES";
                    
                    // toggle boolean
                    inQuotes = !inQuotes;

                    // create token
                    Token newQuoteToken = new Token(tokenID, myMatch, lineNumber + 1);
                    myTokens.add(newQuoteToken);
                    System.out.println("LEXER --> | " + tokenID + " [ " + myMatch + " ] on line " + (lineNumber) + "...");
                }
                
                // The Match is a COMMENT
                else if(match.group().matches(comments)) {
                    
                    // toggle boolean and conduct error and warning checking
                    if(inQuotes) {
                        // if a '/*' or '*/' exists in a quote it is an unauthorized character
                        System.out.println("ERROR UNAUTHORIZED CHARACTER [ " + myMatch + " ] in quotes - at line " + (lineNumber + 1) + "...");
                        this.errorCount++;
                    } else {
                        if(myMatch.equals("/*")) {
                            if(inComment == true) {
                                // check to see if were already within a comment and throw warning
                                System.out.println("WARNING NEW COMMENT STARTED WITHIN ANOTHER COMMENT - at line " + (lineNumber + 1) + "...");
                                this.warningCount++;
                            } else {
                                // if not already in a comment - toggle
                                inComment = true;
                            }
                        } else {
                            // check to see if were already out of a comment and throw warning
                            if(inComment == false) {
                                System.out.println("WARNING COMMENTED ENDED WITHOUT COMMENT START - at line " + (lineNumber + 1) + "...");
                                this.warningCount++;
                            } else {
                                // if not out of comment already - toggle
                                inComment = false;
                            }
                        }
                    }
                }

                //  The Match is a BAD CHAR
                else if(match.group().matches(badChars) && !inComment) {
                    // throw error
                    System.out.println("ERROR Illegal Character Detected: " + myMatch + " at line " + (lineNumber + 1) + "...");
                    this.errorCount++;
                }
            } 
            
        // line done parsing - check for warnings and output
        if(this.foundEnd) {
            // output final conclusion
            if(this.errorCount > 0) {
                System.out.println("Program " + this.myProgramNumber + " Lexical Analysis Failed - Reached End of Program with "+ this.warningCount + " Warning(s) and " + this.errorCount + " Error(s) \n");
                System.out.println("PARSER WILL NOT START DUE TO LEXER ERRORS");
                System.out.println("CST WILL NOT BE PRINTED");
                System.out.println("AST WILL NOT BE CREATED");
                System.out.println("SEMANTIC ANALYSIS WILL NOT START");
                System.out.println("CODE GENERATION WILL NOT START \n");
            } else {
                System.out.println("Program " + this.myProgramNumber + " Lexical Anaylsis Completed - Error Count: " + this.errorCount + " Warning Count: " + this.warningCount + "\n");
                System.out.println("RUNNING PARSE ON PROGRAM  # " + this.myProgramNumber + "\n");

                // Send tokens to PARSER
                myParser.tokenStream(myTokens);

                // Update Parser Program Number
                myParser.setProgramNumber(this.myProgramNumber);
                
                // Start Parsing
                myParser.parseProgram();
            }

           // Save tokens to total tokens list before resetting
        this.myTotalTokens.add(this.myTokens);

        // Reset Lexer for the Next Program
        this.clearLexer();

        // Check if there's another program to process
        if (!hasNextProgram(lineNumber)) {
            System.out.println("No more programs found.");
            return; // Exit the lexer if no next program is found
        } else {
            // Reset necessary flags for the next program
            this.foundNew = true;
            this.foundEnd = false;
        }
        } 
    }

    // Output error for no EOP detected
    if(lastMatch.compareTo("$") != 0) {
        // Program ended without reaching $
        System.out.println("ERROR NO EOP DETECTED AT END OF FILE '$' OR EXTRA CHARACTER(S) EXISTING AFTER EOP");
        this.errorCount++;

        // check for more warnings and output error count
        if(inComment) {
            System.out.println("ERROR PROGRAM ENDED IN A COMMENTED - MISSING END COMMENT - '*/'");
            this.errorCount++;
        } else if(inQuotes) {
            System.out.println("ERROR PROGRAM ENDED IN A QUOTE - MISSING END QUOTE");
            this.errorCount++;
        }

        // output final conclusion
        if(this.errorCount > 0) {
            System.out.println("Program " + this.myProgramNumber + " Lexical Analysis Failed - Reached End of Program with "+ this.warningCount + " Warning(s) and " + this.errorCount + " Error(s) \n");
            System.out.println("PARSER WILL NOT START DUE THESE ERRORS");
            System.out.println("CST WILL NOT BE PRINTED");
            System.out.println("AST WILL NOT BE CREATED");
            System.out.println("SEMANTIC ANALYSIS WILL NOT START");
            System.out.println("CODE GENERATION WILL NOT START \n");
        } else {
            System.out.println("Program " + this.myProgramNumber + "Lexical Analysis Completed - Error Count: " + this.errorCount + " Warning Count: " + this.warningCount + "\n");
            System.out.println("PARSING PROGRAM # " + this.myProgramNumber + "... \n");

            // Send Tokens to PARSER
            myParser.tokenStream(myTokens);

            // Update current Program Number
            myParser.setProgramNumber(this.myProgramNumber);

            // Start parsing
            myParser.parseProgram();
        }

        // Save tokens to total tokens list before resetting
        this.myTotalTokens.add(this.myTokens);

        // Reset Lexer for the Next Program
        this.clearLexer();

        // Check if there's another program to process
        if (!hasNextProgram(lineNumber)) {
            System.out.println("No more programs found.");
            return; // Exit the lexer if no next program is found
        } else {
            // Reset necessary flags for the next program
            this.foundNew = true;
            this.foundEnd = false;
        }
    }   
    }

    public boolean hasNextProgram(int currentLine) {
        // Start scanning from the current line number onwards
        for (int i = currentLine; i < fileLines.size(); i++) {
            String line = fileLines.get(i);
            // Check if this line contains an opening brace {
            if (line.contains("{")) {
                return true; // There's another program
            }
        }
        // If no more { are found, return false
        return false;
    }
}
//! End of Lexical Anaylsis (Scanner)

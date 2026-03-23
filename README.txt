****************
* Project 2 (Nondeterministic Finite Automata)
* CS 361
* 3/23/2026
* Lucas Coltrin, Leo Haener-Pope
**************** 


OVERVIEW:


 This program demonstrates the capabilities of a nondeterministic finite automata.
 It utilizes Java classes State, NFA, and NFAState to allow users to create their own
 NFAs and test if inputs are valid or not for the NFA, and how many different paths
 can be taken through each one.




INCLUDED FILES:


 * NFA.java - java file for appropriate NFA functions
 * NFAState.java - a specific state for NFA, contains some helper functions and stored variables
 * NFAInterface.java - source file
 * FAInterface.java - source file
 * NFATest.java - source file, also edited test file
 * README - this file




COMPILING AND RUNNING:


For testing:

On onyx, first compile with 
`javac -cp .:/usr/share/java/junit.jar ./test/nfa/NFATest.java`

Then run:
`java -cp .:/usr/share/java/junit.jar:/usr/share/java/hamcrest/core.jar org.junit.runner.JUnitCore test.nfa.NFATest`


PROGRAM DESIGN AND IMPORTANT CONCEPTS:


In this program, NFA.java does quite a bit of the heavy lifting for handling how an
NFA works. We utilized using LinkedHashSets for our states, sigma, and finalStates
for the 5-tuple of the NFA as they provide easy ordered access and putting abilities, which we
thought would be helpful for our program.

The NFAState.java program has some helper functions that are used to edit the NFAState that is being
handled (editing the 5-tuple). It utilizes a map with a character, set pair for storing the transitions
for each character for the current NFA state.

The NFA.java has the rest of the main functions that are required by the instructions. For going through an
NFA given a certain string, it utilizes a BFS using Java sets to store data as it goes through the possibilities
of the paths. It also handles e-closures specifically by using a DFS with a java stack object to store and pop e-closure
values. It returns NFAState set of all NFA states that are connected through e-closure. The max copies function
operates pretty similarly to how the accepts function runs, as specified by the instructions.

Overall, the two NFA and NFAState java classes work together, with NFAState having information about state transitions
and helper functions for each state, and NFA operating the broad functionality of NFAs along with helper functions.


TESTING:


This program utilizes the NFATest.java file to run tests with junit. 
Ultimately we utilized the given tests to run and test the program to make sure that it operates correctly.
Tests that were run included just basic asserting that functions and operations worked without error
and returned an expected result for how an NFA would actually work. Our program can handle bad input pretty well,
and is pretty idiot-proof, however if we wanted to fully ensure that our program handles 100% of bad input,
we would probably need to add even more end-to-end tests than we already have. We don't know of any
remaining bugs.




DISCUSSION:
 
 Issues that came up in this project included just first getting our heads wrapped around how
 we were going to make the accepts function to handle the e-closures utilizing the e-closure DFS and Then
 BFS to handle actually going through the NFA. Ultimately however the idea is not really that difficult and
 we just had to think about how we were going to go through with it.

 Ultimately, we understand the concepts of NFAs pretty well, and this project is already pretty similar to
 the DFA project, we just need to be able to handle e-closures along with the multiple paths a character can
 take for a state, so we did not run in to too much trouble for this project.
 
 
EXTRA CREDIT:


 N/A


SOURCES:


 N/A
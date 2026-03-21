package fa.nfa;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;

/**
 * Represents a Definite Finite Automaton, where each DFA state can have a transition pertaining to
 * each letter in the sigma, with no limit on how many transitions per character
 * 
 * @author Lucas Coltrin, Leo Haener-Pope
 */
public class NFA implements NFAInterface {

    //NFA 5-tuple
    private Set<NFAState> states;
    private Set<Character> sigma;
    private NFAState startState;
    private Set<NFAState> finalStates;

    /**
     * Constructs an empty NFA with no states, no alphabet, and no transitions.
     */
    public NFA() {
        states = new LinkedHashSet<>();
        sigma = new LinkedHashSet<>();
        finalStates = new LinkedHashSet<>();
        startState = null;
    }


    /**
     * Adds a new state with the given name to the NFA.
     *
     * @param name the label for the new state
     * @return true if the state was added, false if a state with that name already exists
     */
    @Override
    public boolean addState(String name) {
        // Reject duplicates
        if (getStateByName(name) != null) {
            return false;
        }
        states.add(new NFAState(name));
        return true;
    }

    /**
     * Marks an existing state as a final (accepting) state.
     *
     * @param name the label of the state to mark as final
     * @return true if successful, false if no state with that name exists
     */
    @Override
    public boolean setFinal(String name) {
        NFAState s = getStateByName(name);
        if (s == null) return false;
        s.setFinal();
        finalStates.add(s);
        return true;
    }

    /**
     * Sets an existing state as the NFA's start state.
     *
     * @param name the label of the state to set as start
     * @return true if successful, false if no state with that name exists
     */
    @Override
    public boolean setStart(String name) {
        NFAState s = getStateByName(name);
        if (s == null) return false;
        startState = s;
        return true;
    }

    /**
     * Adds a symbol to the NFA's alphabet (sigma).
     * The reserved character {@code 'e'} should never be added.
     *
     * @param symbol the character to add to the alphabet
     */
    @Override
    public void addSigma(char symbol) {
        sigma.add(symbol);
    }

    /**
     * Simulates the NFA on the given input string using BFS over the set of active states.
     *
     * @param s the input string to test
     * @return true if the NFA accepts the string, false otherwise
     */
    @Override
    public boolean accepts(String s) {
        if (startState == null) return false;

        // Start with the epsilon closure of the start state
        Set<NFAState> current = eClosure(startState);

        // BFS: process each character of the input string
        for (int i = 0; i < s.length(); i++) {
            char symbol = s.charAt(i);

            // Collect all states reachable on this symbol from every current state
            Set<NFAState> next = new HashSet<>();
            for (NFAState state : current) {
                next.addAll(state.toStates(symbol));
            }

            // Expand with epsilon closure of every newly reached state
            Set<NFAState> afterEps = new HashSet<>();
            for (NFAState state : next) {
                afterEps.addAll(eClosure(state));
            }

            current = afterEps;

            // If no copies remain alive, reject early
            if (current.isEmpty()) return false;
        }

        // Accept if any live state is a final state
        for (NFAState state : current) {
            if (state.isFinal()) return true;
        }
        return false;
    }

    /**
     * Returns the alphabet (sigma) of this NFA.
     *
     * @return the set of characters in the alphabet
     */
    @Override
    public Set<Character> getSigma() {
        return sigma;
    }

    /**
     * Returns the NFAState with the given name.
     *
     * @param name the label of the desired state
     * @return the matching {@link NFAState}, or null if not found
     */
    @Override
    public NFAState getState(String name) {
        return getStateByName(name);
    }

    /**
     * Determines whether the state with the given name is a final state.
     *
     * @param name the label of the state
     * @return true if the state exists and is final, false otherwise
     */
    @Override
    public boolean isFinal(String name) {
        NFAState s = getStateByName(name);
        return s != null && s.isFinal();
    }

    /**
     * Determines whether the state with the given name is the start state.
     *
     * @param name the label of the state
     * @return true if the state exists and is the start state, false otherwise
     */
    @Override
    public boolean isStart(String name) {
        return startState != null && startState.getName().equals(name);
    }

    /**
     * Returns the set of states reachable from {@code from} on input symbol {@code onSymb}.
     *
     * @param from   the source state
     * @param onSymb the input symbol (use 'e' for epsilon)
     * @return the set of reachable destination states (may be empty)
     */
    @Override
    public Set<NFAState> getToState(NFAState from, char onSymb) {
        return from.toStates(onSymb);
    }

    /**
     * Computes the epsilon closure of state {@code s} using an iterative DFS with a stack.
     *
     * @param s the starting state
     * @return the set of states in the epsilon closure of {@code s}
     */
    @Override
    public Set<NFAState> eClosure(NFAState s) {
        Set<NFAState> closure = new HashSet<>();
        // Use a Stack for the required iterative DFS
        Stack<NFAState> stack = new Stack<>();
        stack.push(s);

        while (!stack.isEmpty()) {
            NFAState current = stack.pop();

            // closure.add returns false if already present — skip already-visited states
            if (closure.add(current)) {
                // Push all epsilon-reachable neighbors that haven't been visited
                for (NFAState neighbor : current.toStates('e')) {
                    if (!closure.contains(neighbor)) {
                        stack.push(neighbor);
                    }
                }
            }
        }
        return closure;
    }

    /**
     * Simulates the NFA on the input string and returns the maximum number of
     * simultaneous NFA copies (active states) observed at any point during the trace.
     *
     * @param s the input string
     * @return the maximum number of NFA copies alive at any step (minimum 1 if start exists)
     */
    @Override
    public int maxCopies(String s) {
        if (startState == null) return 0;

        // Begin with epsilon closure of the start state
        Set<NFAState> current = eClosure(startState);
        int max = current.size();

        for (int i = 0; i < s.length(); i++) {
            char symbol = s.charAt(i);

            // Transition all current states on the symbol
            Set<NFAState> next = new HashSet<>();
            for (NFAState state : current) {
                next.addAll(state.toStates(symbol));
            }

            // Apply epsilon closure to every reached state
            Set<NFAState> afterEps = new HashSet<>();
            for (NFAState state : next) {
                afterEps.addAll(eClosure(state));
            }

            current = afterEps;

            // Track the maximum number of copies seen
            if (current.size() > max) {
                max = current.size();
            }

            // If no copies remain, stop early but keep current max
            if (current.isEmpty()) break;
        }

        return max;
    }

    /**
     * Adds a transition to the NFA's delta function.
     *
     * @param fromState the label of the source state
     * @param toStates  the set of labels of the destination states
     * @param onSymb    the input symbol ('e' for epsilon)
     * @return true if the transition was added successfully, false otherwise
     */
    @Override
    public boolean addTransition(String fromState, Set<String> toStates, char onSymb) {
        // Validate source state exists
        NFAState from = getStateByName(fromState);
        if (from == null) return false;

        // Symbol must be in sigma OR be the epsilon character 'e'
        if (onSymb != 'e' && !sigma.contains(onSymb)) return false;

        // Validate every destination state exists before adding any transition
        for (String toName : toStates) {
            if (getStateByName(toName) == null) return false;
        }

        // All checks passed — wire up the transitions
        for (String toName : toStates) {
            from.addTransition(onSymb, getStateByName(toName));
        }
        return true;
    }

    /**
     * Determines whether this NFA is also a valid DFA.
     *
     * @return true if this NFA satisfies all DFA constraints, false otherwise
     */
    @Override
    public boolean isDFA() {
        for (NFAState state : states) {
            // Any epsilon transition disqualifies the NFA from being a DFA
            if (!state.toStates('e').isEmpty()) return false;

            // Each symbol must map to exactly one state (no missing or ambiguous transitions)
            for (char c : sigma) {
                if (state.toStates(c).size() != 1) return false;
            }
        }
        return true;
    }

    /**
     * Looks up a state by name in the NFA's state set.
     *
     * @param name the label to search for
     * @return the matching {@link NFAState}, or null if not found
     */
    private NFAState getStateByName(String name) {
        for (NFAState s : states) {
            if (s.getName().equals(name)) return s;
        }
        return null;
    }
}
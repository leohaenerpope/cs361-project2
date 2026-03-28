package fa.nfa;

import fa.State;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents a single state in a Nondeterministic Finite Automaton (NFA).
 * 
 * @author Lucas Coltrin, Leo Haener-Pope
 */
public class NFAState extends State {

    /**
     * Transition function for this state.
     */
    private Map<Character, Set<NFAState>> transitions;

    /** Whether this state is a final/accepting state. */
    private boolean isFinal;

    /**
     * Constructs a new NFAState with the given name.
     * Initializes the transition map and sets the state as non-final.
     *
     * @param name the unique label for this state
     */
    public NFAState(String name) {
        super(name);
        this.transitions = new HashMap<>();
        this.isFinal = false;
    }

    /**
     * Marks this state as a final (accepting) state.
     */
    public void setFinal() {
        this.isFinal = true;
    }

    /**
     * Returns whether this state is a final (accepting) state.
     *
     * @return true if this is a final state, false otherwise
     */
    public boolean isFinal() {
        return isFinal;
    }

    /**
     * Adds a transition from this state on the given symbol to the given destination state.
     *
     * @param onSymb  the input symbol triggering the transition ('e' for epsilon)
     * @param toState the destination NFAState
     */
    public void addTransition(char onSymb, NFAState toState) {
        // Get or create the set of destination states for this symbol
        if (!transitions.containsKey(onSymb)) {
            transitions.put(onSymb, new HashSet<>());
        }
        transitions.get(onSymb).add(toState);
    }

    /**
     * Returns the set of states reachable from this state on the given symbol, or empty if no transition
     *
     * @param onSymb the input symbol to look up ('e' for epsilon)
     * @return a set of reachable NFAStates (may be empty, never null)
     */
    public Set<NFAState> getTransitions(char onSymb) {
        if (transitions.containsKey(onSymb)) {
            return transitions.get(onSymb);
        }
        return new HashSet<>();
    }

    /**
     * Returns the full transition map for this state.
     *
     * @return the map of symbol to set of destination states
     */
    public Map<Character, Set<NFAState>> getAllTransitions() {
        return transitions;
    }
}
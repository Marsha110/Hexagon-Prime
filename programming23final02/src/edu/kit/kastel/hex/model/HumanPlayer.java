package edu.kit.kastel.hex.model;

/**
 * A human-player model.
 *
 * @author uqeoc
 * @author Programmieren-Team
 */
public class HumanPlayer extends Player {

    private static final String EMPTY_STRING = "";

    /**
     * Create a new person-player.
     * @param humanName the name of human.
     */
    public HumanPlayer(String humanName) {
        super(humanName, false);
    }

    @Override
    public String nextAction(Hex hex) {
        return EMPTY_STRING;
    }
}

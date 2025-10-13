package edu.kit.kastel.hex.model;

/**
 * A player model.
 *
 * @author uqeoc
 * @author Programmieren-Team
 */
public abstract class Player {
    protected boolean isAI;
    private final String playerName;


    /**
     * Create a new player.
     *
     * @param playerName the name of the player.
     * @param isAI       true if the player is AI. Otherwise, it's false.
     */
    Player(String playerName, boolean isAI) {
        this.playerName = playerName;
        this.isAI = isAI;
    }

    /**
     * Get the name of player.
     *
     * @return the name of player.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Determines the next action of the given player.
     *
     * @param hex a hex.
     * @return the index of the cell
     */
    public abstract String nextAction(Hex hex);

    /**
     * Get if the player is an AI.
     *
     * @return true if the player is an AI. Otherwise, return false.
     */
    public boolean isAI() {
        return isAI;
    }
}

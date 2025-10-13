package edu.kit.kastel.hex.model;

/**
 * Represents entries of the {@link GameBoard}.
 *
 * @author uqeoc
 * @author Programmieren-Team
 */
public enum Entry {

    /**
     * Token for empty tile.
     */
    EMPTY('.'),
    /**
     * Token for red-player's tile.
     */
    X('X'),
    /**
     * Token for blue-player's tile.
     */
    O('O'),

    /**
     * Token for win tile.
     */
    WIN('*');

    private final char token;

    /**
     * Get the token.
     * @param token token for tile
     */
    Entry(char token) {
        this.token = token;
    }

    /**
     * Get the token.
     * @return token.
     */
    public char getToken() {
        return token;
    }
}

package edu.kit.kastel.hex.model;

/**
 * Exception for signaling that a game piece can not be placed on top of another game piece.
 *
 * @author uqeoc
 * @author Programmieren-Team
 */
public class PlaceOnTopException extends Exception {

    private static final String PIECE_PLACE_ILLEGAL_F = "Tile already placed";
    
    /**
     * Constructs a new PlaceOnTopException.
     *
     */
    public PlaceOnTopException() {
        super(PIECE_PLACE_ILLEGAL_F);
    }
}

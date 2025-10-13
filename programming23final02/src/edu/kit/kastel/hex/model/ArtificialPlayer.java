package edu.kit.kastel.hex.model;


/**
 * A artificial-player model.
 *
 * @author uqeoc
 */
public abstract class ArtificialPlayer extends Player {

    /**
     * Create a new artificial-player.
     * @param aiName the name of AI.
     */
    protected ArtificialPlayer(String aiName) {
        super(aiName, true);
    }
}

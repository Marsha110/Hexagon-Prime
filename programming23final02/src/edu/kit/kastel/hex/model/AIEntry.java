package edu.kit.kastel.hex.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents entries of the {@link ArtificialPlayer}.
 *
 * @author uqeoc
 * @author Programmieren-Team
 */

public class AIEntry {

    protected static final String PLACE_INFO = "place %d %d";
    protected static final String SWAP_INFO = "swap";
    private final Map<String, ArtificialPlayer> artificialPlayers;

    /**
     * Create a new AIEntry.
     */
    public AIEntry() {
        artificialPlayers = new HashMap<>();
        initArtificialPlayers();
    }

    private void initArtificialPlayers() {
        addArtificialPlayer(new BogoAIPlayer());
        addArtificialPlayer(new HeroAIPlayer());
    }

    private void addArtificialPlayer(ArtificialPlayer ai) {
        this.artificialPlayers.put(ai.getPlayerName(), ai);
    }

    /**
     * Get a copy of all AIs.
     * @return a copy of all AIs.
     */
    public Map<String, ArtificialPlayer> getAIs() {
        return Map.copyOf(artificialPlayers);
    }
}

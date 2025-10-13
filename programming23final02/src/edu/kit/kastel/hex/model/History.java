package edu.kit.kastel.hex.model;

import java.util.StringJoiner;

/**
 * A history model.
 *
 * @author uqeoc
 * @author Programmieren-Team
 */
public class History {
    private static final String LINE_JOINER = " ";
    private static final String COLON = ":";
    private Player player;
    private final BoardCoordinate coordinate;

    /**
     * Create a history.
     *
     * @param player     a player.
     * @param coordinate the placed-coordinate of the history.
     */
    History(Player player, BoardCoordinate coordinate) {
        this.player = player;
        this.coordinate = coordinate;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(LINE_JOINER);
        String playerName = player.getPlayerName() + COLON;
        joiner.add(playerName);
        joiner.add(Integer.toString(coordinate.getColumn()));
        joiner.add(Integer.toString(coordinate.getRow()));
        return joiner.toString();
    }

    /**
     * Get the placed-coordinate of the history.
     *
     * @return the placed-coordinate of the history.
     */
    public BoardCoordinate getCoordinate() {
        return coordinate;
    }

    /**
     * Set player.
     *
     * @param newPlayer a new player.
     */
    public void setPlayer(Player newPlayer) {
        this.player = newPlayer;
    }

    /**
     * Set player.
     * @return the player.
     */
    public Player getPlayer() {
        return this.player;
    }
}

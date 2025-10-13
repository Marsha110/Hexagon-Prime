package edu.kit.kastel.hex.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * A board-coordinate model.
 *
 * @author uqeoc
 * @author Programmieren-Team
 */
public class BoardCoordinate {

    private final int row;
    private final int column;
    private final List<BoardCoordinate> prev;
    private int searchedLevel;
    private char token;

    /**
     * Constructs a new BoardCoordinate.
     *
     * @param row    the row of the coordinate
     * @param column the column of the coordinate
     * @param entry  entry of token
     */
    BoardCoordinate(int row, int column, Entry entry) {
        this.row = row;
        this.column = column;
        this.prev = new LinkedList<>();
        this.searchedLevel = Integer.MAX_VALUE;
        this.token = entry.getToken();
    }

    /**
     * Returns the row of the coordinate.
     *
     * @return the row of the coordinate
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column of the coordinate.
     *
     * @return the column of the coordinate
     */
    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof BoardCoordinate otherCoordinate && row == otherCoordinate.row
                && column == otherCoordinate.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    /**
     * Returns a coordinate from the given row and column as strings.
     *
     * @param rowInput    the row of the coordinate
     * @param columnInput the column of the coordinate
     * @param entry entry of token.
     * @return the coordinate or null if the input is invalid
     */
    public static BoardCoordinate getFromStrings(String rowInput, String columnInput, Entry entry) {
        int row;
        int column;
        try {
            row = Integer.parseInt(rowInput);
            column = Integer.parseInt(columnInput);
        } catch (NumberFormatException e) {
            return null;
        }
        if (!GameBoard.validateCoordinate(row, column)) {
            return null;
        }
        return new BoardCoordinate(row, column, entry);
    }

    /**
     * Add a pre-board-coordinate.
     *
     * @param prev pre-board-coordinate.
     */
    public void addAPrev(BoardCoordinate prev) {
        this.prev.add(prev);
    }

    /**
     * Get pre-board-coordinate.
     *
     * @return pre-board-coordinate.
     */
    public List<BoardCoordinate> getPrev() {
        return List.copyOf(prev);
    }

    /**
     * Set the searched-level in BFS.
     *
     * @param newLevel a new searched-level.
     */
    public void setSearchedLevel(int newLevel) {
        this.searchedLevel = newLevel;
    }

    /**
     * Get the searched-level in BFS.
     *
     * @return searched-level in BFS.
     */
    public int getSearchedLevel() {
        return searchedLevel;
    }

    /**
     * Reset prev to original.
     */
    public void resetPrev() {
        this.prev.clear();
    }

    /**
     * reset searched-level to original.
     */
    public void resetLevel() {
        this.searchedLevel = Integer.MAX_VALUE;
    }

    /**
     * Get the token of this coordinate.
     *
     * @return the token of this coordinate.
     */
    public char getToken() {
        return token;
    }

    /**
     * Set the token of this coordinate.
     *
     * @param entry token.
     */
    public void setToken(Entry entry) {
        this.token = entry.getToken();
    }
}

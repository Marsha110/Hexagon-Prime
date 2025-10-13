package edu.kit.kastel.hex.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.StringJoiner;

/**
 * A game-board model.
 *
 * @author uqeoc
 * @author Programmieren-Team
 */
public class GameBoard {
    private static final String CELL_DELIMITER = " ";
    private static final String PADDING = "";
    private static int sideLength;
    private final List<BoardCoordinate> board;
    private final List<BoardCoordinate> winBoard;
    private final Set<BoardCoordinate> tileSearchedOnEastSide;
    private boolean northSideReached;
    private boolean southSideReached;
    private boolean westSideReached;
    private boolean eastSideReached;

    /**
     * Create a new game-board.
     *
     * @param sideLength the length of game-board.
     */
    public GameBoard(int sideLength) {

        GameBoard.sideLength = sideLength;
        winBoard = new ArrayList<>();
        board = new ArrayList<>();

        for (int i = 0; i < sideLength; i++) {
            for (int j = 0; j < sideLength; j++) {
                board.add(new BoardCoordinate(i, j, Entry.EMPTY));
            }
        }
        tileSearchedOnEastSide = new HashSet<>();
        northSideReached = false;
        southSideReached = false;
        westSideReached = false;
        eastSideReached = false;
    }

    /**
     * Print the board.
     *
     * @return a string contains the board-info.
     */
    public String printBoard() {
        return printABoard(board);
    }

    /**
     * Print the win-board.
     *
     * @return a string contains the win-board-info.
     */
    public String printWinBoard() {
        return printABoard(winBoard);
    }

    private String printABoard(List<BoardCoordinate> inputBoard) {
        StringJoiner totalOutput = new StringJoiner(System.lineSeparator());

        for (int i = 0; i < sideLength; i++) {
            StringJoiner line = new StringJoiner(CELL_DELIMITER);
            for (int indentation = i; indentation > 0; indentation--) {
                line.add(PADDING);
            }
            for (int j = 0; j < sideLength; j++) {
                line.add(Character.toString(inputBoard.get(i * sideLength + j).getToken()));
            }
            totalOutput.add(line.toString());
        }
        return totalOutput.toString();
    }

    /**
     * Print an empty board.
     * @return the empty board.
     */
    public String printAEmptyBoard() {
        StringJoiner totalOutput = new StringJoiner(System.lineSeparator());

        for (int i = 0; i < sideLength; i++) {
            StringJoiner line = new StringJoiner(CELL_DELIMITER);
            for (int indentation = i; indentation > 0; indentation--) {
                line.add(PADDING);
            }
            for (int j = 0; j < sideLength; j++) {
                line.add(Character.toString(Entry.EMPTY.getToken()));
            }
            totalOutput.add(line.toString());
        }
        return totalOutput.toString();
    }

    /**
     * Checks if a coordinate is considered valid.
     *
     * @param row    the row of the coordinate
     * @param column the column of the coordinate
     * @return {@code true} if the coordinate is valid, {@code false} otherwise
     */
    public static boolean validateCoordinate(int row, int column) {
        return row >= 0 && row < sideLength && column >= 0 && column < sideLength;
    }

    /**
     * place a piece.
     *
     * @param entry      entry of token.
     * @param coordinate board-coordinate to be placed.
     * @throws PlaceOnTopException if the tile is occupied.
     */
    public void place(Entry entry, BoardCoordinate coordinate) throws PlaceOnTopException {
        if (getTile(coordinate.getRow(), coordinate.getColumn()).getToken() == Entry.EMPTY.getToken()) {
            setTileToken(coordinate.getRow(), coordinate.getColumn(), entry);
        } else {
            throw new PlaceOnTopException();
        }
    }

    /**
     * Reset all the side reached infos and all the prev and search-level infos of the tiles.
     */
    public void reset() {
        northSideReached = false;
        southSideReached = false;
        westSideReached = false;
        eastSideReached = false;
        tileSearchedOnEastSide.clear();
        for (BoardCoordinate tile : board) {
            tile.resetPrev();
            tile.resetLevel();
        }
    }

    /**
     * Mark the win-route in the win-board with star.
     */
    public void updateWinBoard() {

        for (int i = 0; i < board.size(); i++) {
            if (board.get(i).getSearchedLevel() != Integer.MAX_VALUE) {
                winBoard.add(new BoardCoordinate(getRowNumber(i), getColumnNumber(i), Entry.WIN));
            } else {
                winBoard.add(board.get(i));
            }
        }
    }

    private int getRowNumber(int index) {
        return index / sideLength;
    }

    private int getColumnNumber(int index) {
        return index % sideLength;
    }

    /**
     * Add all neighbors of a coordinate to a queue.
     *
     * @param token            token of this current-player.
     * @param extraToken       extra-token to be considered as a neighbor
     * @param startCoordinate  the coordinate to start.
     * @param neighbors        to store all the new-found neighbors.
     * @param extraFreeHexagon the coordinates of the hexagon which can be taken as free.
     */
    public void addNeighborsToQueue(char token, char extraToken,
                                    BoardCoordinate startCoordinate, Queue<BoardCoordinate> neighbors,
                                    List<BoardCoordinate> extraFreeHexagon) {

        addOneNeighbor(token, extraToken, startCoordinate, 0, -1, neighbors, extraFreeHexagon);
        addOneNeighbor(token, extraToken, startCoordinate, 0, 1, neighbors, extraFreeHexagon);
        addOneNeighbor(token, extraToken, startCoordinate, -1, 0, neighbors, extraFreeHexagon);
        addOneNeighbor(token, extraToken, startCoordinate, -1, 1, neighbors, extraFreeHexagon);
        addOneNeighbor(token, extraToken, startCoordinate, 1, -1, neighbors, extraFreeHexagon);
        addOneNeighbor(token, extraToken, startCoordinate, 1, 0, neighbors, extraFreeHexagon);
    }

    /**
     * Add One neighbor to the Queue.
     *
     * @param token            token on the route to be added.
     * @param extraToken       extra-token on the route to be added. The range of it is within extraFreeHexagonForOToken
     *                         or extraFreeHexagonForXToken; Character.MIN_VALUE means this extra-token will be invalid.
     * @param startCoordinate  the coordinate which this number found.
     * @param rowShift         the shift of the row
     * @param columnShift      the shift of the column
     * @param neighbors        stored all the tiles that the BFS needs.
     * @param extraFreeHexagon the coordinates of the hexagon which can be taken as free.
     */
    private void addOneNeighbor(char token, char extraToken, BoardCoordinate startCoordinate, int rowShift,
                                int columnShift, Queue<BoardCoordinate> neighbors,
                                List<BoardCoordinate> extraFreeHexagon) {

        setTileReachedSide(startCoordinate);
        BoardCoordinate neighbor = getTile(startCoordinate.getRow() + rowShift,
                startCoordinate.getColumn() + columnShift);
        List<Character> tokens = new LinkedList<>();
        tokens.add(token);
        if (extraToken != Character.MIN_VALUE) {
            tokens.add(extraToken);
        }
        if (neighbor == null || neighbor.getSearchedLevel() <= startCoordinate.getSearchedLevel()
                || !tokens.contains(getTile(neighbor.getRow(), neighbor.getColumn()).getToken())) {
            return;
        }

        if ((getTile(neighbor.getRow(), neighbor.getColumn()).getToken() == token
                || extraFreeHexagon != null && extraFreeHexagon.contains(neighbor))
                && neighbor.getSearchedLevel() > startCoordinate.getSearchedLevel()) {
            if (neighbor.getSearchedLevel() == Integer.MAX_VALUE) {
                neighbors.add(neighbor);
                setTileReachedSide(neighbor);
            }
            //update neighbor
            neighbor.addAPrev(startCoordinate);
            neighbor.setSearchedLevel(startCoordinate.getSearchedLevel() + 1);
        }
    }

    private void setTileReachedSide(BoardCoordinate tile) {
        if (tile.getRow() == 0) {
            northSideReached = true;
        } else if (tile.getRow() == sideLength - 1) {
            southSideReached = true;
        }
        if (tile.getColumn() == 0) {
            westSideReached = true;
        } else if (tile.getColumn() == sideLength - 1) {
            eastSideReached = true;
            tileSearchedOnEastSide.add(tile);
        }
    }

    /**
     * If there is no own game piece on the board yet, place the piece on the free hexagon with the
     * lowest x-coordinate (and if ambiguous, with the lowest y-coordinate).
     *
     * @return the tile at the first available position along the rows of hexagons.
     */
    public BoardCoordinate getTheMinimumAvailableCoordinateForHero() {
        for (int column = 0; column < sideLength; column++) {
            for (int row = 0; row < sideLength; row++) {
                if (getTile(row, column).getToken() == Entry.EMPTY.getToken()) {
                    return getTile(row, column);
                }
            }
        }
        return null;
    }

    /**
     * Get the tile at the first available position along the rows of hexagons.
     *
     * @return the tile at the first available position along the rows of hexagons.
     */
    public BoardCoordinate getTheMinimumAvailableCoordinateForBogo() {
        for (int row = 0; row < sideLength; row++) {
            for (int column = 0; column < sideLength; column++) {
                if (getTile(row, column).getToken() == Entry.EMPTY.getToken()) {
                    return getTile(row, column);
                }
            }
        }
        return null;
    }

    /**
     * Get the side-length of this board.
     *
     * @return the side-length of this board.
     */
    public int getSideLength() {
        return sideLength;
    }

    /**
     * Set a token on the board.
     *
     * @param row    row of this token
     * @param column column of this token
     * @param entry  a entry.
     */
    public void setTileToken(int row, int column, Entry entry) {
        if (getTile(row, column) == null) {
            return;
        }
        getTile(row, column).setToken(entry);
    }

    /**
     * Get a tile.
     *
     * @param row    row of this tile.
     * @param column column of this tile.
     * @return a tile.
     */
    public BoardCoordinate getTile(int row, int column) {
        if (row >= sideLength || column >= sideLength || row < 0 || column < 0) {
            return null;
        }
        return board.get(sideLength * row + column);
    }

    /**
     * Get if east-side is reached.
     *
     * @return true if it's reached. Otherwise, return false.
     */
    public boolean isEastSideReached() {
        return eastSideReached;
    }

    /**
     * Get if west-side is reached.
     *
     * @return true if it's reached. Otherwise, return false.
     */
    public boolean isWestSideReached() {
        return westSideReached;
    }

    /**
     * Get if north-side is reached.
     *
     * @return true if it's reached. Otherwise, return false.
     */
    public boolean isNorthSideReached() {
        return northSideReached;
    }

    /**
     * Get if south-side is reached.
     *
     * @return true if it's reached. Otherwise, return false.
     */
    public boolean isSouthSideReached() {
        return southSideReached;
    }

    /**
     * Get all the tiles which are in the route on the east-side.
     *
     * @return all the tiles which are in the route on the east-side.
     */
    public Set<BoardCoordinate> getTileSearchedOnEastSide() {
        return Set.copyOf(tileSearchedOnEastSide);
    }
}

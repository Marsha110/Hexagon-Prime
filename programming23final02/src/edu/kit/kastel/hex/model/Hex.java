package edu.kit.kastel.hex.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringJoiner;

/**
 * A Hex Model.
 *
 * @author uqeoc
 */
public class Hex {
    private Player redPlayer;
    private Player bluePlayer;
    private final Player originalRedPlayer;
    private final Player originalBluePlayer;
    private final boolean isAutoPrint;
    private final int sideLength;
    private Player currentPlayer;
    private final GameBoard board;
    private final String hexName;
    private final List<History> histories;
    private int turnCount;
    private Entry currentEntry;
    private boolean isSwapped;
    private boolean hasWon = false;

    /**
     * Instantiates a new Hex.
     *
     * @param redPlayer  red-player.
     * @param bluePlayer blue-player.
     * @param printBoard true if game-board print automatically. Otherwise, false.
     * @param sideLength the length of game-board.
     * @param hexName    the name of hex.
     */
    public Hex(Player redPlayer, Player bluePlayer, boolean printBoard, int sideLength, String hexName) {

        this.originalRedPlayer = redPlayer;
        this.originalBluePlayer = bluePlayer;
        this.redPlayer = redPlayer;
        this.bluePlayer = bluePlayer;
        this.isAutoPrint = printBoard;
        this.sideLength = sideLength;
        this.currentPlayer = redPlayer;
        this.board = new GameBoard(sideLength);
        this.hexName = hexName;
        this.histories = new LinkedList<>();
        this.turnCount = 0;
        this.currentEntry = Entry.X;
        this.isSwapped = false;

    }

    /**
     * Get the current-player.
     *
     * @return the current-player.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Get the board and convert it to a string.
     *
     * @return a string contains the game-board.
     */
    public String printBoard() {
        return board.printBoard();
    }

    /**
     * Get the win-board and convert it to a string.
     *
     * @return a string contains the game-win-board.
     */
    public String printWinBoard() {
        return board.printWinBoard();
    }

    /**
     * Place a piece.
     *
     * @param coordinate the coordinate of the tile to be placed.
     * @throws PlaceOnTopException if the tile is occupied.
     */
    public void place(BoardCoordinate coordinate) throws PlaceOnTopException {
        Player currentPlayerTemp = currentPlayer;
        try {
            board.place(currentEntry, coordinate);
        } catch (PlaceOnTopException e) {
            throw new PlaceOnTopException();
        }
        histories.add(new History(currentPlayerTemp, coordinate));
        updateCurrentPlayer();
        updateCurrentEntry();
        turnCount++;
    }

    private void updateCurrentPlayer() {
        currentPlayer = currentPlayer == redPlayer ? bluePlayer : redPlayer;
    }

    private void updateCurrentEntry() {
        currentEntry = currentEntry.getToken() == Entry.X.getToken() ? Entry.O : Entry.X;
    }

    /**
     * Get if the board is print automatically.
     *
     * @return true if the board is print automatically. Otherwise, return false.
     */
    public boolean isAutoPrint() {
        return this.isAutoPrint;
    }

    /**
     * Swap the players.
     */
    public void swap() {
        Player playerTemp = this.redPlayer;
        this.redPlayer = this.bluePlayer;
        this.bluePlayer = playerTemp;
        updateCurrentPlayer();
        swapHistories();
        isSwapped = true;
    }

    private void swapHistories() {
        for (History history : histories) {
            history.setPlayer(history.getPlayer() == bluePlayer ? redPlayer : bluePlayer);
        }
    }

    /**
     * Get the last "turns" history-infos.
     *
     * @param turns the number of the last history-infos.
     * @return the last "turns" history-infos.
     */
    public String history(int turns) {
        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        if (turns > histories.size() || turns <= 0) {
            return joiner.toString();
        }

        for (int i = 0; i < turns && histories.size() - 1 - i >= 0; i++) {
            joiner.add(histories.get(histories.size() - 1 - i).toString());
        }
        return joiner.toString();
    }

    /**
     * Get the total number of histories.
     *
     * @return the total number of histories.
     */
    public int getHistoryTotalNumber() {
        return histories.size();
    }

    /**
     * Get the name of hex.
     *
     * @return the name of hex.
     */
    public String getHexName() {
        return hexName;
    }

    /**
     * Deep copy the current game with a new gaem-name.
     *
     * @param newName a new game-name.
     * @return the new created hex.
     */
    public Hex deepCopyWithNewGameName(String newName) {
        return new Hex(originalRedPlayer, originalBluePlayer, isAutoPrint, sideLength, newName);
    }

    /**
     * Get the turn-number.
     *
     * @return the turn-number.
     */
    public int getTurnCount() {
        return turnCount;
    }

    /**
     * Get the game-board.
     *
     * @return the game-board.
     */
    public GameBoard getBoard() {
        return board;
    }

    /**
     * Get the tile with which the player can win.
     *
     * @param entry entry of token.
     * @return the tile if it exists. Otherwise, return null.
     */
    public BoardCoordinate getWinCoordinate(Entry entry) {
        int boardSideLength = board.getSideLength();
        for (int column = 0; column < boardSideLength; column++) {
            for (int row = 0; row < boardSideLength; row++) {
                if (board.getTile(row, column).getToken() == Entry.X.getToken()
                        || board.getTile(row, column).getToken() == Entry.O.getToken()) {
                    continue;
                }
                board.setTileToken(row, column, entry);
                BoardCoordinate startCoordinates = board.getTile(row, column);
                if (startCoordinates != null && hasWon(entry, startCoordinates, false)) {
                    board.setTileToken(row, column, Entry.EMPTY);
                    return startCoordinates;
                }
                board.setTileToken(row, column, Entry.EMPTY);
            }
        }
        return null;
    }

    /**
     * Get current entry.
     *
     * @return current entry.
     */
    public Entry getCurrentEntry() {
        return currentEntry;
    }

    /**
     * Check if it's swapped.
     *
     * @return true if it's swapped. Otherwise, return false;
     */
    public boolean isSwapped() {
        return isSwapped;
    }

    /**
     * Get a list of histories.
     *
     * @return a list of histories.
     */
    public List<History> getHistories() {
        return List.copyOf(histories);
    }

    /**
     * Check if a player has won.
     *
     * @param entry                entry of token.
     * @param startCoordinate      the start-coordinate to be checked.
     * @param markWinRouteWithStar if it needs to mark the win-route after won.
     * @return true if the player has won. Otherwise, return false.
     */
    public boolean hasWon(Entry entry, BoardCoordinate startCoordinate, boolean markWinRouteWithStar) {
        updateSideReachedInfo(entry.getToken(), startCoordinate);
        boolean hasWon = false;
        if (entry.getToken() == Entry.X.getToken() && board.isNorthSideReached() && board.isSouthSideReached()) {
            hasWon = true;
        } else if (entry.getToken() == Entry.O.getToken() && board.isWestSideReached() && board.isEastSideReached()) {
            hasWon = true;
        }
        if (hasWon && markWinRouteWithStar) {
            board.updateWinBoard();
        }
        //reset all side-reached infos.
        board.reset();
        return hasWon;
    }

    private void updateSideReachedInfo(char token, BoardCoordinate startCoordinate) {
        Queue<BoardCoordinate> neighbors = new LinkedList<>();
        BoardCoordinate startCoordinateOnTheBoard
                = board.getTile(startCoordinate.getRow(), startCoordinate.getColumn());
        if (startCoordinateOnTheBoard == null) {
            return;
        }

        neighbors.add(startCoordinateOnTheBoard);
        startCoordinateOnTheBoard.setSearchedLevel(0);
        while (!neighbors.isEmpty()) {
            BoardCoordinate startCoordinateTemp = neighbors.poll();
            board.addNeighborsToQueue(token, Character.MIN_VALUE, startCoordinateTemp, neighbors, null);
        }
    }

    /**
     * Get if a player has won.
     *
     * @return if a player has won.
     */
    public boolean hasWon() {
        return hasWon;
    }

    /**
     * Set if it has won.
     *
     * @param hasWon if a player has won.
     */
    public void setHasWon(boolean hasWon) {
        this.hasWon = hasWon;
    }

    /**
     * Get the name of original red-player.
     *
     * @return the name of original red-player.
     */
    public String getOriginalRedPlayerName() {
        return originalRedPlayer.getPlayerName();
    }
}

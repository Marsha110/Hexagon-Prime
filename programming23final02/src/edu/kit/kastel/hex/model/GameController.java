package edu.kit.kastel.hex.model;

import edu.kit.kastel.hex.ui.ResultType;

import java.util.LinkedList;
import java.util.List;

/**
 * A controller of all games.
 *
 * @author uqeoc
 * @author Programmieren-Team
 */
public class GameController {

    private static final int ARGS_COUNT_MAX = 4;
    private static final int ARGS_COUNT_MIN = 3;
    private static final int SIDE_LENGTH_INDEX = 0;
    private static final int RED_PLAYER_NAME_INDEX = 1;
    private static final int BLUE_PLAYER_NAME_INDEX = 2;
    private static final int AUTO_PRINT_INDEX = 3;
    private static final int SIDE_LENGTH_MIN = 4;
    private static final int SIDE_LENGTH_MAX = 12345;
    private static final String AUTO_PRINT = "auto-print";
    private static final String ERROR_ARGUMENT_NUMBER = "The number of arguments shall be three or four.";
    private static final String ERROR_ARGUMENT_PLAYER_NAME = "The name of the two players shall be different.";
    private static final String ERROR_FOURTH_ARGUMENT = "The fourth argument shall be auto-print.";
    private static final String ERROR_SIDE_LENGTH
            = "The first parameter shall be a positive odd number greater than 4 and less than 12345.";
    private static final String HEX_NAME_PRIME = "Prime";
    private static final String ERROR_RED_PLAYER_SHALL_NOT_AI = "The first player muss be a person.";
    private final AIEntry aiEntry = new AIEntry();
    private final List<Hex> games;
    private Hex currentHex;
    private boolean useAI;

    /**
     * Create a game-controller.
     *
     * @param args arguments.
     *
     */
    public GameController(String[] args) {
        this.useAI = false;
        this.games = new LinkedList<>();
        currentHex = null;
        buildGame(args);
    }

    private void buildGame(String[] args) {
        if (args.length < ARGS_COUNT_MIN || args.length > ARGS_COUNT_MAX) {
            ResultType.FAILURE.printResult(ERROR_ARGUMENT_NUMBER);
            return;
        }

        if (args[RED_PLAYER_NAME_INDEX].equals(args[BLUE_PLAYER_NAME_INDEX])) {
            ResultType.FAILURE.printResult(ERROR_ARGUMENT_PLAYER_NAME);
            return;
        }

        boolean autoPrint = args.length == ARGS_COUNT_MAX && args[AUTO_PRINT_INDEX].equals(AUTO_PRINT);
        if (args.length == ARGS_COUNT_MAX && !autoPrint) {
            ResultType.FAILURE.printResult(ERROR_FOURTH_ARGUMENT);
            return;
        }

        boolean isRedPlayerAI = aiEntry.getAIs().containsKey(args[RED_PLAYER_NAME_INDEX]);
        if (isRedPlayerAI) {
            ResultType.FAILURE.printResult(ERROR_RED_PLAYER_SHALL_NOT_AI);
            return;
        }

        int sideLength;
        try {
            sideLength = Integer.parseInt(args[SIDE_LENGTH_INDEX]);
        } catch (NumberFormatException ignored) {
            ResultType.FAILURE.printResult(ERROR_SIDE_LENGTH);
            return;
        }

        Player redPlayer = new HumanPlayer(args[RED_PLAYER_NAME_INDEX]);
        Player bluePlayer;
        if (aiEntry.getAIs().containsKey(args[BLUE_PLAYER_NAME_INDEX])) {
            bluePlayer = aiEntry.getAIs().get(args[BLUE_PLAYER_NAME_INDEX]);
            useAI = true;
        } else {
            bluePlayer = new HumanPlayer(args[BLUE_PLAYER_NAME_INDEX]);
        }

        if (sideLength < SIDE_LENGTH_MIN || sideLength > SIDE_LENGTH_MAX || sideLength % 2 != 1) {
            ResultType.FAILURE.printResult(ERROR_SIDE_LENGTH);
            return;
        }
        currentHex = new Hex(redPlayer, bluePlayer, autoPrint, sideLength, HEX_NAME_PRIME);

        this.games.add(this.currentHex);
    }

    /**
     * Build a game.
     *
     * @param gameName the name of the game.
     */
    public void buildGame(String gameName) {
        currentHex = currentHex.deepCopyWithNewGameName(gameName);
        this.games.add(this.currentHex);
    }

    /**
     * Get all the games.
     *
     * @return a copy of all the games.
     */
    public List<Hex> getGames() {
        return List.copyOf(games);
    }

    /**
     * Get current-hex.
     *
     * @return current-hex.
     */
    public Hex getCurrentHex() {
        return currentHex;
    }

    /**
     * Switch to a new game.
     *
     * @param hexName the name of the new game.
     * @return true if the new game exists. Otherwise, return false.
     */
    public boolean switchGame(String hexName) {
        if (currentHex.getHexName().equals(hexName)) {
            return false;
        }
        for (Hex hex : games) {
            if (hex.getHexName().equals(hexName)) {
                currentHex = hex;
                return true;
            }
        }
        return false;
    }

    /**
     * Get if AI is used.
     *
     * @return true if AI is used. Otherwise, return false.
     */
    public boolean useAI() {
        return useAI;
    }
}

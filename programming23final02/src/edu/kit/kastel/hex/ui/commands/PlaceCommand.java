package edu.kit.kastel.hex.ui.commands;

import edu.kit.kastel.hex.model.BoardCoordinate;
import edu.kit.kastel.hex.model.Entry;
import edu.kit.kastel.hex.model.Hex;
import edu.kit.kastel.hex.model.PlaceOnTopException;
import edu.kit.kastel.hex.model.Player;
import edu.kit.kastel.hex.ui.CommandHandler;
import edu.kit.kastel.hex.ui.HexCommand;
import edu.kit.kastel.hex.ui.Result;
import edu.kit.kastel.hex.ui.ResultType;

/**
 * This command place a piece on the game-board.
 *
 * @author uqeoc
 * @author Programmieren-Team
 */
public class PlaceCommand extends HexCommand {

    private static final String COORDINATE_INVALID_FORMAT = "the coordinate (%s %s) is not valid.";
    private static final String COMMAND_DESCRIPTION = "Place a piece";
    private static final String COMMAND_NAME = "place";
    private static final String STRING_INIT = "";
    private static final String AI_PLACE_INFO = "%s places at %d %d";
    private static final String WON = "%s wins!";
    private static final String GAME_OVER = "The game is over, you can no longer continue playing chess.";
    private static final int EXPECTED_ARGUMENTS = 2;
    private static final int ROW_INDEX = 0;
    private static final int COLUMN_INDEX = 1;

    /**
     * Constructs a new PlaceCommand.
     *
     * @param commandHandler the command handler
     * @param hex            the hex instance
     */
    public PlaceCommand(CommandHandler commandHandler, Hex hex) {
        super(COMMAND_NAME, COMMAND_DESCRIPTION, commandHandler, hex, EXPECTED_ARGUMENTS, EXPECTED_ARGUMENTS);
    }

    @Override
    protected Result executeHexCommand(String[] commandArguments) {
        if (hex.hasWon()) {
            return new Result(ResultType.FAILURE, GAME_OVER);
        }

        BoardCoordinate coordinateTemp = BoardCoordinate.getFromStrings(commandArguments[COLUMN_INDEX],
                commandArguments[ROW_INDEX], Entry.EMPTY);

        if (coordinateTemp == null) {
            return new Result(ResultType.FAILURE, COORDINATE_INVALID_FORMAT
                    .formatted(commandArguments[ROW_INDEX], commandArguments[COLUMN_INDEX]));
        }

        BoardCoordinate coordinate = hex.getBoard().getTile(coordinateTemp.getRow(), coordinateTemp.getColumn());
        //place a piece
        Player currentPlayer = hex.getCurrentPlayer();
        Entry currentEntry = hex.getCurrentEntry();
        try {
            hex.place(coordinate);
        } catch (PlaceOnTopException e) {
            return new Result(ResultType.FAILURE, e.getMessage());
        }

        //generate the action from AI, if AI is used.
        String aiPlaceInfo = STRING_INIT;
        if (currentPlayer.isAI()) {
            aiPlaceInfo = String.format(AI_PLACE_INFO, currentPlayer.getPlayerName(),
                    coordinate.getColumn(), coordinate.getRow()) + System.lineSeparator();
        }

        //generate board
        String boardInfo = STRING_INIT;
        if (hex.isAutoPrint()) {
            boardInfo = hex.printBoard() + System.lineSeparator();
        }

        //generate other success- and failed-infos.
        if (hex.hasWon(currentEntry, coordinate, true)) {
            hex.setHasWon(true);
            return new Result(ResultType.SUCCESS, aiPlaceInfo
                    + String.format(WON, currentPlayer.getPlayerName()) + System.lineSeparator() + hex.printWinBoard());
        } else {
            return new Result(ResultType.SUCCESS, aiPlaceInfo + boardInfo
                    + String.format(SUCCESS, hex.getCurrentPlayer().getPlayerName()));
        }
    }
}


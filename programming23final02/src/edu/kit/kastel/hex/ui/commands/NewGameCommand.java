package edu.kit.kastel.hex.ui.commands;

import edu.kit.kastel.hex.model.GameController;
import edu.kit.kastel.hex.model.Hex;
import edu.kit.kastel.hex.ui.CommandHandler;
import edu.kit.kastel.hex.ui.HexCommand;
import edu.kit.kastel.hex.ui.Result;
import edu.kit.kastel.hex.ui.ResultType;

/**
 * This command creates a new game.
 *
 * @author uqeoc
 * @author Programmieren-Team
 */
public class NewGameCommand extends HexCommand {

    private static final String COMMAND_NAME = "new-game";
    private static final String COMMAND_DESCRIPTION = "Create a new game";
    private static final String EMPTY_RESULT = "";
    private static final String ERROR_GAME_NAME_DUPLICATED = "The new game-name is duplicated.";
    private static final int EXPECTED_ARGUMENTS_MIN = 1;
    private static final int EXPECTED_ARGUMENTS_MAX = 1;
    GameController gameController;

    /**
     * Constructs a new PrintCommand.
     *
     * @param commandHandler    the command handler
     * @param gameController    the game-controller instance
     */
    public NewGameCommand(CommandHandler commandHandler, GameController gameController) {
        super(COMMAND_NAME, COMMAND_DESCRIPTION, commandHandler, gameController.getCurrentHex(),
                EXPECTED_ARGUMENTS_MIN, EXPECTED_ARGUMENTS_MAX);
        this.gameController = gameController;
    }

    @Override
    protected Result executeHexCommand(String[] commandArguments) {
        for (Hex hex : gameController.getGames()) {
            if (hex.getHexName().equals(commandArguments[0])) {
                return new Result(ResultType.FAILURE, ERROR_GAME_NAME_DUPLICATED);
            }
        }
        commandHandler.printStartInfo(commandArguments[0], hex.getOriginalRedPlayerName());
        gameController.buildGame(commandArguments[0]);
        commandHandler.updateCurrentHex();
        commandHandler.initCommands();
        return new Result(ResultType.SUCCESS, EMPTY_RESULT);
    }
}


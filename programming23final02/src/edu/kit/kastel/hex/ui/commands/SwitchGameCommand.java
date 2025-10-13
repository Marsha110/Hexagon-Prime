package edu.kit.kastel.hex.ui.commands;

import edu.kit.kastel.hex.model.GameController;
import edu.kit.kastel.hex.ui.CommandHandler;
import edu.kit.kastel.hex.ui.HexCommand;
import edu.kit.kastel.hex.ui.Result;
import edu.kit.kastel.hex.ui.ResultType;

/**
 * This command switches the game.
 *
 * @author uqeoc
 * @author Programmieren-Team
 */
public class SwitchGameCommand extends HexCommand {

    private static final String COMMAND_NAME = "switch-game";
    private static final String COMMAND_DESCRIPTION = "Switch to new game";
    private static final String SWITCH_SUCCESS_INFO = "Switched to %s";
    private static final String SWITCH_FAILED_INFO_NOT_EXISTS = "This hex-name doesn't exist.";
    private static final String SWITCH_FAILED_INFO_SAME_GAME = "This hex-name is same with the current game.";
    private static final int EXPECTED_ARGUMENTS_MIN = 1;
    private static final int EXPECTED_ARGUMENTS_MAX = 1;
    GameController gameController;

    /**
     * Constructs a new SwitchGameCommand.
     *
     * @param commandHandler the command handler
     * @param gameController the game-controller instance
     */
    public SwitchGameCommand(CommandHandler commandHandler, GameController gameController) {
        super(COMMAND_NAME, COMMAND_DESCRIPTION, commandHandler, gameController.getCurrentHex(),
                EXPECTED_ARGUMENTS_MIN, EXPECTED_ARGUMENTS_MAX);
        this.gameController = gameController;
    }

    @Override
    protected Result executeHexCommand(String[] commandArguments) {
        if (gameController.getCurrentHex().getHexName().equals(commandArguments[0])) {
            return new Result(ResultType.FAILURE, SWITCH_FAILED_INFO_SAME_GAME);
        }

        if (gameController.switchGame(commandArguments[0])) {
            commandHandler.updateCurrentHex();
            commandHandler.initCommands();
            return new Result(ResultType.SUCCESS, String.format(SWITCH_SUCCESS_INFO,
                    gameController.getCurrentHex().getHexName()));
        } else {
            return new Result(ResultType.FAILURE, SWITCH_FAILED_INFO_NOT_EXISTS);
        }
    }
}


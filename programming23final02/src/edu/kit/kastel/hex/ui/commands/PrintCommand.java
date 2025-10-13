package edu.kit.kastel.hex.ui.commands;

import edu.kit.kastel.hex.model.Hex;
import edu.kit.kastel.hex.ui.CommandHandler;
import edu.kit.kastel.hex.ui.HexCommand;
import edu.kit.kastel.hex.ui.Result;
import edu.kit.kastel.hex.ui.ResultType;

/**
 * This command prints the content of the board.
 *
 * @author uqeoc
 * @author Programmieren-Team
 */
public class PrintCommand extends HexCommand {

    private static final String COMMAND_NAME = "print";
    private static final String COMMAND_DESCRIPTION = "Prints the game-board";
    private static final int EXPECTED_ARGUMENTS = 0;

    /**
     * Constructs a new PrintCommand.
     *
     * @param commandHandler    the command handler
     * @param hex         the hex instance
     */
    public PrintCommand(CommandHandler commandHandler, Hex hex) {
        super(COMMAND_NAME, COMMAND_DESCRIPTION, commandHandler, hex, EXPECTED_ARGUMENTS, EXPECTED_ARGUMENTS);
    }

    @Override
    protected Result executeHexCommand(String[] commandArguments) {
        return new Result(ResultType.SUCCESS, hex.printBoard());
    }
}


package edu.kit.kastel.hex.ui.commands;

import edu.kit.kastel.hex.model.Hex;
import edu.kit.kastel.hex.model.Player;
import edu.kit.kastel.hex.ui.CommandHandler;
import edu.kit.kastel.hex.ui.HexCommand;
import edu.kit.kastel.hex.ui.Result;
import edu.kit.kastel.hex.ui.ResultType;

/**
 * This command swaps the players.
 *
 * @author uqeoc
 * @author Programmieren-Team
 */
public class SwapCommand extends HexCommand {

    private static final String COMMAND_NAME = "swap";
    private static final String SWAP_INFO = "%s swaps";
    private static final String COMMAND_DESCRIPTION = "Swap two players";
    private static final String SWAP_FAILED = "It's only allowed the blue-player in the first turn to swap.";
    private static final String STRING_INIT = "";
    private static final int EXPECTED_ARGUMENTS = 0;

    /**
     * Constructs a new SwapCommand.
     *
     * @param commandHandler the command handler
     * @param hex            the hex instance
     */
    public SwapCommand(CommandHandler commandHandler, Hex hex) {
        super(COMMAND_NAME, COMMAND_DESCRIPTION, commandHandler, hex, EXPECTED_ARGUMENTS, EXPECTED_ARGUMENTS);
    }

    @Override
    protected Result executeHexCommand(String[] commandArguments) {

        if (hex.getTurnCount() != 1 || hex.isSwapped()) {
            return new Result(ResultType.FAILURE, SWAP_FAILED);
        }

        Player player = hex.getCurrentPlayer();
        hex.swap();

        //generate board
        String boardInfo = STRING_INIT;
        if (hex.isAutoPrint()) {
            boardInfo = hex.printBoard() + System.lineSeparator();
        }

        return new Result(ResultType.SUCCESS, String.format(SWAP_INFO, player.getPlayerName())
                + System.lineSeparator() + boardInfo + String.format(SUCCESS, hex.getCurrentPlayer().getPlayerName()));
    }
}


package edu.kit.kastel.hex.ui.commands;

import edu.kit.kastel.hex.model.Hex;
import edu.kit.kastel.hex.ui.CommandHandler;
import edu.kit.kastel.hex.ui.HexCommand;
import edu.kit.kastel.hex.ui.Result;
import edu.kit.kastel.hex.ui.ResultType;

/**
 * Command to show history-info.
 *
 * @author uqeoc
 * @author Programmieren-Team
 */
public class HistoryCommand extends HexCommand {

    private static final String COMMAND_NAME = "history";
    private static final String COMMAND_DESCRIPTION = "Prints the turn history";
    private static final String HISTORY_FAILED_INFO_FORMAT
            = "The second parameter shall be a positive integer.";
    private static final String HISTORY_FAILED_INFO_NUMBER_EXCEEDS
            = "The total number of histories is %d. The input shall not exceed %d.";
    private static final String HISTORY_FAILED_INFO_NO_HISTORY = "There is no histories.";
    private static final int EXPECTED_ARGUMENTS_MIN = 0;
    private static final int EXPECTED_ARGUMENTS_MAX = 1;

    /**
     * Constructs a new HistoryCommand.
     *
     * @param commandHandler the command handler
     * @param hex            the hex instance
     */
    public HistoryCommand(CommandHandler commandHandler, Hex hex) {
        super(COMMAND_NAME, COMMAND_DESCRIPTION, commandHandler, hex, EXPECTED_ARGUMENTS_MIN, EXPECTED_ARGUMENTS_MAX);
    }

    @Override
    protected Result executeHexCommand(String[] commandArguments) {
        if (hex.getHistoryTotalNumber() == 0) {
            return new Result(ResultType.FAILURE, HISTORY_FAILED_INFO_NO_HISTORY);
        }

        int turns;
        if (commandArguments.length != 0) {
            try {
                turns = Integer.parseInt(commandArguments[0]);
            } catch (NumberFormatException e) {
                return new Result(ResultType.FAILURE, HISTORY_FAILED_INFO_FORMAT);
            }

            //check the range of the parameter
            if (turns > hex.getHistoryTotalNumber()) {
                return new Result(ResultType.FAILURE, String.format(HISTORY_FAILED_INFO_NUMBER_EXCEEDS,
                        hex.getHistoryTotalNumber(), hex.getHistoryTotalNumber()));
            } else if (turns <= 0) {
                return new Result(ResultType.FAILURE, HISTORY_FAILED_INFO_FORMAT);
            }

            return new Result(ResultType.SUCCESS, hex.history(turns));

        } else {
            return new Result(ResultType.SUCCESS, hex.history(1));
        }
    }
}


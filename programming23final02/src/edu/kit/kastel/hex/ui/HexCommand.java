package edu.kit.kastel.hex.ui;

import edu.kit.kastel.hex.model.Hex;

import java.util.Objects;

/**
 * A command used for handling {@link Hex} regarding interactions.
 *
 * @author uqeoc
 * @author Programmieren-Team
 * @version 1.0
 */
public abstract class HexCommand extends Command {

    /**
     * Success info.
     */
    protected static final String SUCCESS = "%s's turn";

    /**
     * A COLON
     */
    protected static final String COLON = ":";
    private static final String NOT_EXPECTED_ARGS_LENGTH_ERROR_WITH_RANGE
            = "Expected [%d, %d] arguments but got %d";
    private static final String NOT_EXPECTED_ARGS_LENGTH_ERROR = "Expected %d arguments but got %d";
    protected final Hex hex;
    private final int expectedNumberOfArgumentsMin;
    private final int expectedNumberOfArgumentsMax;

    /**
     * Creates a new hex command
     *
     * @param commandName                  The name of the command.
     * @param commandDescription           The description of this command
     * @param commandHandler               The command handler.
     * @param hex                          a hex play
     * @param expectedNumberOfArgumentsMin The expected min. number of arguments
     * @param expectedNumberOfArgumentsMax The expected max. number of arguments
     */
    protected HexCommand(String commandName, String commandDescription, CommandHandler commandHandler,
                         Hex hex, int expectedNumberOfArgumentsMin, int expectedNumberOfArgumentsMax) {
        super(commandName, commandDescription, commandHandler);
        this.hex = Objects.requireNonNull(hex);
        this.expectedNumberOfArgumentsMin = expectedNumberOfArgumentsMin;
        this.expectedNumberOfArgumentsMax = expectedNumberOfArgumentsMax;
    }

    @Override
    public final void execute(String[] commandArguments) {
        //check the length of args.
        if (commandArguments.length < expectedNumberOfArgumentsMin
                || commandArguments.length > expectedNumberOfArgumentsMax) {
            if (expectedNumberOfArgumentsMax == expectedNumberOfArgumentsMin) {
                ResultType.FAILURE.printResult(String.format(NOT_EXPECTED_ARGS_LENGTH_ERROR,
                        expectedNumberOfArgumentsMin, commandArguments.length));
            } else {
                ResultType.FAILURE.printResult(String.format(NOT_EXPECTED_ARGS_LENGTH_ERROR_WITH_RANGE,
                                expectedNumberOfArgumentsMin, expectedNumberOfArgumentsMax,
                                commandArguments.length));
            }
            return;
        }

        Result result = executeHexCommand(commandArguments);
        if (result != null) {
            result.getType().printResult(result.getMessage());
        }
    }

    /**
     * Executes the command on the Hex.
     *
     * @param commandArguments The command arguments.
     * @return the result of this execute.
     */
    protected abstract Result executeHexCommand(String[] commandArguments);
}

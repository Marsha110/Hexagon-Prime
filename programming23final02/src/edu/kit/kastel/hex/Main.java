package edu.kit.kastel.hex;

import edu.kit.kastel.hex.ui.CommandHandler;

/**
 * Main class of the program.
 *
 * @author uqeoc
 * @author Programmieren-Team
 */
public final class Main {
    private static final String ERROR_UTILITY_CLASS_INSTANTIATION = "Utility class cannot be instantiated.";

    private Main() {
        throw new UnsupportedOperationException(ERROR_UTILITY_CLASS_INSTANTIATION);
    }

    /**
     * Main entry point of the program.
     *
     * @param args basis-parameter for the start of game.
     */
    public static void main(String[] args) {
        CommandHandler commandHandler = new CommandHandler(args);
        commandHandler.handleUserInput();
    }
}

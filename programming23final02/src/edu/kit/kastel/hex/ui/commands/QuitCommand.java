package edu.kit.kastel.hex.ui.commands;

import edu.kit.kastel.hex.ui.Command;
import edu.kit.kastel.hex.ui.CommandHandler;

/**
 * Command to quit the program.
 *
 * @author uqeoc
 * @author Programmieren-Team
 */
public class QuitCommand extends Command {

    private static final String COMMAND_NAME = "quit";
    private static final String COMMAND_DESCRIPTION = "Quit all games and end program";
    private static final String QUIT_WITH_ARGUMENTS_ERROR = "Error: quit does not allow args.";

    /**
     * Instantiates a new quit command.
     *
     * @param commandHandler the command handler
     */
    public QuitCommand(CommandHandler commandHandler) {
        super(COMMAND_NAME, COMMAND_DESCRIPTION, commandHandler);
    }

    @Override
    public void execute(String[] commandArguments) {
        if (commandArguments.length != 0) {
            System.err.println(QUIT_WITH_ARGUMENTS_ERROR);
            return;
        }
        commandHandler.quit();
    }
}

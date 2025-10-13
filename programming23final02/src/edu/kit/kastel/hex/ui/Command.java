package edu.kit.kastel.hex.ui;

import java.util.Objects;

/**
 * A command that can be executed by the user.
 *
 * @author uqeoc
 * @author Programmieren-Team
 */
public abstract class Command {

    /**
     * The command handler for this command
     */
    protected final CommandHandler commandHandler;
    private final String commandName;
    private final String commandDescription;

    /**
     * Instantiates a new command.
     *
     * @param commandName        the command name
     * @param commandDescription the description of this command
     * @param commandHandler     the command handler for this command
     */
    protected Command(String commandName, String commandDescription, CommandHandler commandHandler) {
        this.commandName = Objects.requireNonNull(commandName);
        this.commandHandler = Objects.requireNonNull(commandHandler);
        this.commandDescription = commandDescription;
    }

    /**
     * Gets the command name.
     *
     * @return the command name
     */
    public final String getCommandName() {
        return commandName;
    }

    /**
     * Executes the command with the given arguments.
     *
     * @param commandArguments the command arguments
     */
    public abstract void execute(String[] commandArguments);

    /**
     * Get the description of a command.
     *
     * @return the description of a command.
     */
    public String getCommandDescription() {
        return commandDescription;
    }
}


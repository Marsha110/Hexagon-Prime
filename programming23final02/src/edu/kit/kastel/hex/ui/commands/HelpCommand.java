package edu.kit.kastel.hex.ui.commands;

import edu.kit.kastel.hex.model.Hex;
import edu.kit.kastel.hex.ui.Command;
import edu.kit.kastel.hex.ui.CommandHandler;
import edu.kit.kastel.hex.ui.HexCommand;
import edu.kit.kastel.hex.ui.Result;
import edu.kit.kastel.hex.ui.ResultType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;

/**
 * This command show all the descriptions of commands.
 *
 * @author uqeoc
 * @author Programmieren-Team
 */
public class HelpCommand extends HexCommand {

    private static final String COMMAND_NAME = "help";
    private static final String COMMAND_DESCRIPTION = "Prints this help message";
    private static final String JOINER_FOR_LINE = " ";
    private static final String STAR = "*";
    private static final int EXPECTED_ARGUMENTS = 0;

    /**
     * Constructs a new HelpCommand.
     *
     * @param commandHandler the command handler
     * @param hex            the hex instance
     */
    public HelpCommand(CommandHandler commandHandler, Hex hex) {
        super(COMMAND_NAME, COMMAND_DESCRIPTION, commandHandler, hex, EXPECTED_ARGUMENTS, EXPECTED_ARGUMENTS);
    }

    @Override
    protected Result executeHexCommand(String[] commandArguments) {
        return new Result(ResultType.SUCCESS, getHelpInfo());
    }

    private String getHelpInfo() {
        List<Command> commands = new ArrayList<>(commandHandler.getCommands().values());
        commands.sort(Comparator.comparing(Command::getCommandName));
        StringJoiner joinerForRow = new StringJoiner(System.lineSeparator());

        for (Command command : commands) {
            StringJoiner line = new StringJoiner(JOINER_FOR_LINE);
            line.add(STAR);
            String commandName = command.getCommandName() + COLON;
            line.add(commandName);
            line.add(command.getCommandDescription());
            joinerForRow.add(line.toString());
        }
        return joinerForRow.toString();
    }
}


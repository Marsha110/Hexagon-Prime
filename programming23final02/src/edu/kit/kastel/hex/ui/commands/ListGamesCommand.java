package edu.kit.kastel.hex.ui.commands;

import edu.kit.kastel.hex.model.Hex;
import edu.kit.kastel.hex.ui.CommandHandler;
import edu.kit.kastel.hex.ui.HexCommand;
import edu.kit.kastel.hex.ui.Result;
import edu.kit.kastel.hex.ui.ResultType;

import java.util.List;
import java.util.StringJoiner;

/**
 * This command list all games.
 *
 * @author uqeoc
 * @author Programmieren-Team
 */
public class ListGamesCommand extends HexCommand {

    private static final String COMMAND_NAME = "list-games";
    private static final String COMMAND_DESCRIPTION = "List all games";
    private static final String LINE_SEPARATOR = " ";
    private static final int EXPECTED_ARGUMENTS_MIN = 0;
    private static final int EXPECTED_ARGUMENTS_MAX = 0;

    /**
     * Constructs a new ListGamesCommand.
     *
     * @param commandHandler the command handler.
     * @param hex            the hex instance
     */
    public ListGamesCommand(CommandHandler commandHandler, Hex hex) {
        super(COMMAND_NAME, COMMAND_DESCRIPTION, commandHandler, hex, EXPECTED_ARGUMENTS_MIN, EXPECTED_ARGUMENTS_MAX);
    }

    @Override
    protected Result executeHexCommand(String[] commandArguments) {
        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        List<Hex> games = commandHandler.getGameController().getGames();
        for (Hex hex : games) {
            if (hex.hasWon()) {
                continue;
            }
            StringJoiner line = new StringJoiner(LINE_SEPARATOR);
            String hexName = hex.getHexName() + COLON;
            line.add(hexName);
            line.add(Integer.toString(hex.getTurnCount()));
            joiner.add(line.toString());
        }
        return new Result(ResultType.SUCCESS, joiner.toString());
    }
}


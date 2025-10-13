package edu.kit.kastel.hex.ui;

import edu.kit.kastel.hex.model.GameController;
import edu.kit.kastel.hex.model.Hex;
import edu.kit.kastel.hex.ui.commands.HelpCommand;
import edu.kit.kastel.hex.ui.commands.HistoryCommand;
import edu.kit.kastel.hex.ui.commands.ListGamesCommand;
import edu.kit.kastel.hex.ui.commands.NewGameCommand;
import edu.kit.kastel.hex.ui.commands.PlaceCommand;
import edu.kit.kastel.hex.ui.commands.PrintCommand;
import edu.kit.kastel.hex.ui.commands.QuitCommand;
import edu.kit.kastel.hex.ui.commands.SwapCommand;
import edu.kit.kastel.hex.ui.commands.SwitchGameCommand;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * The command handler responsible for maintaining all necessary commands and user interaction.
 *
 * @author uqeoc
 * @author Programmieren-Team
 */
public class CommandHandler {

    /**
     * SUCCESS INFO.
     */
    protected static final String SUCCESS = "%s's turn";
    private static final String COMMAND_SEPARATOR_REGEX = "\\s+";
    private static final String COMMAND_NOT_FOUND = "Command '%s' not found";
    private static final String START_WORDS = "Welcome to %s";
    private final Map<String, Command> commands;
    private final GameController gameController;
    private Hex currentHex;
    private boolean running = false;


    /**
     * Instantiates a new command handler.
     *
     * @param args input-arguments
     */
    public CommandHandler(String[] args) {
        this.commands = new HashMap<>();
        gameController = new GameController(args);
        currentHex = gameController.getCurrentHex();
        if (currentHex != null) {
            this.initCommands();
        }
    }

    /**
     * Handles the user input.
     */
    public void handleUserInput() {
        if (currentHex == null) {
            return;
        }
        printStartInfo();
        int remainderForPerson;
        int remainderForAI;
        //start the game
        this.running = true;
        try (Scanner scanner = new Scanner(System.in)) {
            while (running) {
                //System.out.println(start);
                if (!gameController.useAI() && scanner.hasNextLine()) {
                    executeCommand(scanner.nextLine());
                    continue;
                }

                if (currentHex.isSwapped()) {
                    remainderForPerson = 1;
                    remainderForAI = 0;
                } else {
                    remainderForPerson = 0;
                    remainderForAI = 1;
                }
                if ((currentHex.hasWon() || currentHex.getTurnCount() % 2 == remainderForPerson) && scanner.hasNextLine()) {
                    executeCommand(scanner.nextLine());
                } else if (!currentHex.hasWon() && currentHex.getTurnCount() % 2 == remainderForAI) {
                    executeCommand(gameController.getCurrentHex().getCurrentPlayer().nextAction(currentHex));
                }
            }
        }
    }

    /**
     * Print game start infos.
     */
    public void printStartInfo() {
        //print start infos
        Result startInfo = new Result(ResultType.SUCCESS, String.format(START_WORDS, currentHex.getHexName()));
        startInfo.getType().printResult(startInfo.getMessage());

        //print game-board if the mode is auto-print
        if (currentHex.isAutoPrint()) {
            System.out.println(currentHex.printBoard());
        }
        Result turnInfo = new Result(ResultType.SUCCESS,
                String.format(SUCCESS, currentHex.getCurrentPlayer().getPlayerName()));
        turnInfo.getType().printResult(turnInfo.getMessage());
    }

    /**
     * Print game start infos.
     *
     * @param hexName name of the hex
     * @param redPlayerName the name of the red-player.
     */
    public void printStartInfo(String hexName, String redPlayerName) {
        //print start infos
        Result startInfo = new Result(ResultType.SUCCESS, String.format(START_WORDS, hexName));
        startInfo.getType().printResult(startInfo.getMessage());

        //print game-board if the mode is auto-print
        if (currentHex.isAutoPrint()) {
            System.out.println(currentHex.getBoard().printAEmptyBoard());
        }
        Result turnInfo = new Result(ResultType.SUCCESS,
                String.format(SUCCESS, redPlayerName));
        turnInfo.getType().printResult(turnInfo.getMessage());
    }

    /**
     * Quits the user input handling.
     */
    public void quit() {
        this.running = false;
    }

    private void executeCommand(String commandWithArguments) {
        String[] splittedCommand = commandWithArguments.trim().split(COMMAND_SEPARATOR_REGEX);
        String commandName = splittedCommand[0];
        String[] commandArguments = Arrays.copyOfRange(splittedCommand, 1, splittedCommand.length);

        if (!commands.containsKey(commandName)) {
            ResultType.FAILURE.printResult(COMMAND_NOT_FOUND, commandName);
            return;
        }
        commands.get(commandName).execute(commandArguments);
    }

    /**
     * Initiate all the commands.
     */
    public void initCommands() {
        commands.clear();
        this.addCommand(new QuitCommand(this));
        this.addCommand(new PrintCommand(this, currentHex));
        this.addCommand(new PlaceCommand(this, currentHex));
        this.addCommand(new SwapCommand(this, currentHex));
        this.addCommand(new HistoryCommand(this, currentHex));
        this.addCommand(new HelpCommand(this, currentHex));
        this.addCommand(new NewGameCommand(this, gameController));
        this.addCommand(new ListGamesCommand(this, currentHex));
        this.addCommand(new SwitchGameCommand(this, gameController));
    }


    private void addCommand(Command command) {
        this.commands.put(command.getCommandName(), command);
    }

    /**
     * Get all commands.
     *
     * @return A copy of commands.
     */
    public Map<String, Command> getCommands() {
        return Map.copyOf(commands);
    }

    /**
     * Get the game-controller.
     *
     * @return game-controller.
     */
    public GameController getGameController() {
        return gameController;
    }

    /**
     * Update the current-hex from game-controller.
     */
    public void updateCurrentHex() {
        currentHex = gameController.getCurrentHex();
    }
}

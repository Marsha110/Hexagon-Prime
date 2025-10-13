package edu.kit.kastel.hex.ui;

/**
 * The type of Result of an execution.
 *
 * @author uqeoc
 * @author Programmieren-Team
 */
public enum ResultType {
    /**
     * The execution did not end with success.
     */
    FAILURE() {
        @SafeVarargs
        @Override
        public final <T> void printResult(String formattedMessage, T... args) {
            System.err.printf("Error: " + formattedMessage + System.lineSeparator(), (Object) args);
        }
    },
    /**
     * The execution did end with success.
     */
    SUCCESS() {
        @SafeVarargs
        @Override
        public final <T> void printResult(String formattedMessage, T... args) {
            if (!formattedMessage.isEmpty()) {
                System.out.printf(formattedMessage + System.lineSeparator(), (Object) args);
            }
        }
    };


    /**
     * Prints the result of the execution.
     *
     * @param formattedMessage the formatted message
     * @param args             the arguments
     * @param <T>              the type of the arguments
     */
    public abstract <T> void printResult(String formattedMessage, T... args);
}

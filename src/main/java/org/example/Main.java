package org.example;

import java.util.Scanner;
public class Main {

    /**
     * Program entry point.
     *
     * <p>Reads a multi-line text from the user (terminated by an empty line),
     * then reads a single target character. Invokes
     * {@link TextProcessor#sortWordsByCharOccurrence(char)} and prints the result.
     * All checked and unchecked exceptions are caught and reported to {@code stderr}.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the text (press Enter twice to finish):");
        StringBuilder inputBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                break;
            }
            if (!inputBuilder.isEmpty()) {
                inputBuilder.append(" ");
            }
            inputBuilder.append(line);
        }
        String inputText = inputBuilder.toString();

        char targetSymbol = '\0';
        while (targetSymbol == '\0') {
            System.out.print("Enter the character to sort by: ");
            String symbolInput = scanner.nextLine().trim();
            if (symbolInput.length() == 1 && symbolInput.charAt(0) != ' ') {
                targetSymbol = symbolInput.charAt(0);
            } else {
                System.err.println("Please enter exactly one non-space character.");
            }
        }

        System.out.println();
        System.out.println("Input text : " + inputText);
        System.out.println("Sort character: '" + targetSymbol + "'");
        System.out.println();

        try {
            TextProcessor processor = new TextProcessor(inputText);

            StringBuilder result = processor.sortWordsByCharOccurrence(targetSymbol);

            System.out.println("Words sorted by occurrence count of '" + targetSymbol + "':");
            System.out.println(result);

        } catch (IllegalArgumentException e) {
            System.err.println("Invalid input: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println("Processing error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
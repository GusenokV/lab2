package org.example;

public class TextProcessor {
    private StringBuilder text;

    /**
     * Constructs a {@code TextProcessor} with the given input text.
     *
     * @param text the source text to process
     * @throws IllegalArgumentException if {@code text} is {@code null} or blank
     */
    public TextProcessor(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text must not be null or empty.");
        }
        this.text = new StringBuilder(text);
    }

    /**
     * Extracts all words from the current text.
     *
     * <p>Words are delimited by whitespace and punctuation characters
     * ({@code .,!?;:"'()-}). Empty tokens produced by the split are discarded.
     *
     * @return an array of words as {@link StringBuilder} instances
     */
    private StringBuilder[] extractWords() {
        String[] raw = this.text.toString().split("[\\s.,!?;:\"'()\\-]+");
        int count = 0;
        for (String w : raw) {
            if (!w.isEmpty()) {
                count++;
            }
        }
        StringBuilder[] words = new StringBuilder[count];
        int idx = 0;
        for (String w : raw) {
            if (!w.isEmpty()) {
                words[idx++] = new StringBuilder(w);
            }
        }
        return words;
    }

    /**
     * Counts how many times {@code symbol} appears in {@code word}.
     *
     * @param word   the word to inspect
     * @param symbol the character to count
     * @return the number of occurrences of {@code symbol} in {@code word}
     */
    private int countOccurrences(StringBuilder word, char symbol) {
        int count = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == symbol) {
                count++;
            }
        }
        return count;
    }

    /**
     * Sorts all words in the text by the number of occurrences of {@code symbol}
     * in ascending order using the insertion sort algorithm.
     *
     * <p>Words with equal occurrence counts preserve their original relative order
     * (stable sort).
     *
     * @param symbol the character whose occurrence count determines the sort key;
     *               must not be a space character
     * @return a {@link StringBuilder} containing the sorted words separated by
     *         single spaces
     * @throws IllegalArgumentException if {@code symbol} is a space character
     * @throws IllegalStateException    if no words could be extracted from the text
     */
    public StringBuilder sortWordsByCharOccurrence(char symbol) {
        if (symbol == ' ') {
            throw new IllegalArgumentException("Search symbol must not be a space character.");
        }

        StringBuilder[] words = extractWords();

        if (words.length == 0) {
            throw new IllegalStateException("No words found in the provided text.");
        }

        // Insertion sort;
        for (int i = 1; i < words.length; i++) {
            StringBuilder key = words[i];
            int keyCount = countOccurrences(key, symbol);
            int j = i - 1;
            while (j >= 0 && countOccurrences(words[j], symbol) > keyCount) {
                words[j + 1] = words[j];
                j--;
            }
            words[j + 1] = key;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            result.append(words[i]);
            if (i < words.length - 1) {
                result.append(" ");
            }
        }
        return result;
    }

    /**
     * Returns a copy of the current text.
     *
     * @return the stored text as a new {@link StringBuilder}
     */
    public StringBuilder getText() {
        return new StringBuilder(this.text);
    }
}
package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link TextProcessor}.
 *
 * <p>Test naming convention: {@code methodName_scenario_expectedBehaviour}.
 * All tests use JUnit 5 ({@code org.junit.jupiter}).
 */
@DisplayName("TextProcessor")
class TextProcessorTest {

    @Test
    @DisplayName("constructor: valid text — object created successfully")
    void constructor_validText_createsInstance() {
        assertDoesNotThrow(() -> new TextProcessor("Hello world."));
    }

    @Test
    @DisplayName("constructor: null text — throws IllegalArgumentException")
    void constructor_nullText_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new TextProcessor(null));
    }

    @Test
    @DisplayName("constructor: empty string — throws IllegalArgumentException")
    void constructor_emptyText_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new TextProcessor(""));
    }

    @Test
    @DisplayName("constructor: blank string (only spaces) — throws IllegalArgumentException")
    void constructor_blankText_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new TextProcessor("   "));
    }

    @Test
    @DisplayName("getText: returns stored text unchanged")
    void getText_returnsOriginalText() {
        String input = "Hello world.";
        TextProcessor processor = new TextProcessor(input);
        assertEquals(input, processor.getText().toString());
    }

    @Test
    @DisplayName("getText: returns a defensive copy, not the internal reference")
    void getText_returnsDefensiveCopy() {
        TextProcessor processor = new TextProcessor("Hello world.");
        StringBuilder copy = processor.getText();
        copy.append(" modified");
        // Internal text must not be affected
        assertEquals("Hello world.", processor.getText().toString());
    }

    @Test
    @DisplayName("sortWordsByCharOccurrence: space symbol — throws IllegalArgumentException")
    void sortWords_spaceSymbol_throwsIllegalArgumentException() {
        TextProcessor processor = new TextProcessor("Hello world.");
        assertThrows(IllegalArgumentException.class,
                () -> processor.sortWordsByCharOccurrence(' '));
    }

    @Test
    @DisplayName("sortWordsByCharOccurrence: text made of punctuation only — throws IllegalStateException")
    void sortWords_onlyPunctuation_throwsIllegalStateException() {
        TextProcessor processor = new TextProcessor("... , ! ?");
        assertThrows(IllegalStateException.class,
                () -> processor.sortWordsByCharOccurrence('a'));
    }

    @Test
    @DisplayName("sortWordsByCharOccurrence: basic case — words sorted by char count ascending")
    void sortWords_basicText_correctAscendingOrder() {
        // 'a' occurrences: "cat"=1, "banana"=3, "hat"=1, "panda"=2
        // Expected stable order by count: cat hat panda banana  (cat before hat — original order)
        TextProcessor processor = new TextProcessor("cat banana hat panda");
        String result = processor.sortWordsByCharOccurrence('a').toString();
        String[] words = result.split(" ");

        assertEquals("cat",    words[0]);
        assertEquals("hat",    words[1]);
        assertEquals("panda",  words[2]);
        assertEquals("banana", words[3]);
    }

    @Test
    @DisplayName("sortWordsByCharOccurrence: single word — returned unchanged")
    void sortWords_singleWord_returnedUnchanged() {
        TextProcessor processor = new TextProcessor("banana");
        assertEquals("banana", processor.sortWordsByCharOccurrence('a').toString());
    }

    @Test
    @DisplayName("sortWordsByCharOccurrence: character absent in all words — original order preserved")
    void sortWords_charAbsentEverywhere_originalOrderPreserved() {
        TextProcessor processor = new TextProcessor("one two three");
        // 'z' appears 0 times in every word — stable sort must keep original order
        assertEquals("one two three", processor.sortWordsByCharOccurrence('z').toString());
    }

    @Test
    @DisplayName("sortWordsByCharOccurrence: all words have equal char count — original order preserved")
    void sortWords_allEqualCount_originalOrderPreserved() {
        // 'o' appears once in each word
        TextProcessor processor = new TextProcessor("fox box dog cod");
        assertEquals("fox box dog cod", processor.sortWordsByCharOccurrence('o').toString());
    }

    @Test
    @DisplayName("sortWordsByCharOccurrence: text with punctuation — punctuation stripped, words sorted")
    void sortWords_textWithPunctuation_punctuationStripped() {
        // "fast." → "fast" (1×'a'), "a" (1×'a'), "banana," → "banana" (3×'a'), "cat!" → "cat" (1×'a')
        TextProcessor processor = new TextProcessor("fast. a banana, cat!");
        String result = processor.sortWordsByCharOccurrence('a').toString();
        // All 3 with count=1 come before banana (count=3)
        assertTrue(result.endsWith("banana"),
                "Word with most occurrences should be last: " + result);
        assertFalse(result.contains(".") || result.contains(",") || result.contains("!"),
                "Result must not contain punctuation: " + result);
    }

    @Test
    @DisplayName("sortWordsByCharOccurrence: case-sensitive — uppercase and lowercase treated differently")
    void sortWords_caseSensitive_upperAndLowerDistinct() {
        // 'A' (uppercase) — "Apple" has 0 lowercase 'a', "apple" has 1
        TextProcessor processor = new TextProcessor("Apple banana apple");
        String result = processor.sortWordsByCharOccurrence('A').toString();
        // "Apple" has 1 'A', others have 0 — Apple must come last
        String[] words = result.split(" ");
        assertEquals("Apple", words[words.length - 1]);
    }

}
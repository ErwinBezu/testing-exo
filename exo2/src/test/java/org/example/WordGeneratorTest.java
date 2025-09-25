package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WordGeneratorTest {

    @Test
    @DisplayName("getWord(index) renvoie le mot attendu et gère les bornes")
    void getWordByIndex() {
        WordGenerator wg = new WordGenerator(List.of("a", "b", "c"), new Random(42));
        assertThat(wg.getWord(0)).isEqualTo("a");
        assertThat(wg.getWord(2)).isEqualTo("c");
        assertThrows(IndexOutOfBoundsException.class, () -> wg.getWord(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> wg.getWord(3));
    }

    @Test
    @DisplayName("getRandomWord() dépend de Random et de la taille de la liste")
    void getRandomWordDeterministic() {
        Random fixed = new Random(123456);
        WordGenerator wg = new WordGenerator(List.of("java", "maven", "mockito"), fixed);

        String w1 = wg.getRandomWord();
        String w2 = wg.getRandomWord();
        String w3 = wg.getRandomWord();

        assertThat(List.of(w1, w2, w3)).hasSize(3)
                .allMatch(List.of("java", "maven", "mockito")::contains);
    }

    @Test
    @DisplayName("Constructeurs valident les entrées")
    void constructorsValidate() {
        assertThrows(IllegalArgumentException.class, () -> new WordGenerator(List.of(), new Random()));
    }
}

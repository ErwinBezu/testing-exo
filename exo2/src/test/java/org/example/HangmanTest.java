package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HangmanTest {

    @Test
    @DisplayName("Construction: refuse un nombre d'essais invalide ou un mot vide")
    void constructorValidation() {
        WordGenerator wg = Mockito.mock(WordGenerator.class);
        Mockito.when(wg.getRandomWord()).thenReturn("java");

        assertThrows(IllegalArgumentException.class, () -> new Hangman(wg, 0));

        Mockito.when(wg.getRandomWord()).thenReturn("   ");
        assertThrows(IllegalArgumentException.class, () -> new Hangman(wg, 5));
    }

    @Test
    @DisplayName("Deviner des lettres correctement révèle le mot")
    void guessRevealsMaskedWord() {
        WordGenerator wg = Mockito.mock(WordGenerator.class);
        Mockito.when(wg.getRandomWord()).thenReturn("java");

        Hangman game = new Hangman(wg, 5);
        assertThat(game.getMaskedWord()).isEqualTo("____");

        boolean hitJ = game.guess('j');
        assertThat(hitJ).isTrue();
        assertThat(game.getMaskedWord()).isEqualTo("j___");
        assertThat(game.getRemainingAttempts()).isEqualTo(5);

        boolean hitA = game.guess('A');
        assertThat(hitA).isTrue();
        assertThat(game.getMaskedWord()).isEqualTo("ja_a");
        assertThat(game.getRemainingAttempts()).isEqualTo(5);

        boolean miss = game.guess('z');
        assertThat(miss).isFalse();
        assertThat(game.getMaskedWord()).isEqualTo("ja_a");
        assertThat(game.getRemainingAttempts()).isEqualTo(4);
    }

    @Test
    @DisplayName("Ne pas décrémenter deux fois pour la même mauvaise lettre déjà tentée")
    void doNotDoubleDecrementOnSameMiss() {
        WordGenerator wg = Mockito.mock(WordGenerator.class);
        Mockito.when(wg.getRandomWord()).thenReturn("maven");

        Hangman game = new Hangman(wg, 2);
        assertThat(game.guess('x')).isFalse();
        assertThat(game.getRemainingAttempts()).isEqualTo(1);

        assertThat(game.guess('x')).isFalse();
        assertThat(game.getRemainingAttempts()).isEqualTo(1);
    }

    @Test
    @DisplayName("Partie gagnée quand toutes les lettres découvertes")
    void gameWon() {
        WordGenerator wg = Mockito.mock(WordGenerator.class);
        Mockito.when(wg.getRandomWord()).thenReturn("junit");

        Hangman game = new Hangman(wg, 3);
        for (char c : "junit".toCharArray()) {
            game.guess(c);
        }
        assertThat(game.isGameWon()).isTrue();
        assertThat(game.isGameOver()).isTrue();
        assertThat(game.getMaskedWord()).isEqualTo("junit");
        assertThat(game.getRemainingAttempts()).isEqualTo(3);
    }

    @Test
    @DisplayName("Partie perdue quand plus d'essais")
    void gameLost() {
        WordGenerator wg = Mockito.mock(WordGenerator.class);
        Mockito.when(wg.getRandomWord()).thenReturn("java");

        Hangman game = new Hangman(wg, 2);
        game.guess('x');
        game.guess('y');

        assertThat(game.isGameWon()).isFalse();
        assertThat(game.isGameOver()).isTrue();
        assertThat(game.getRemainingAttempts()).isEqualTo(0);

        game.guess('j');
        assertThat(game.isGameOver()).isTrue();
    }
}

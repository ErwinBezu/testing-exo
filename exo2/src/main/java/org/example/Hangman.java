package org.example;


import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public class Hangman {
    private final String wordToGuess;
    private final Set<Character> guessedLetters;
    private int remainingAttempts;

    public Hangman(WordGenerator wordGenerator, int maxAttempts) {
        Objects.requireNonNull(wordGenerator, "wordGenerator");
        if (maxAttempts < 1) {
            throw new IllegalArgumentException("maxAttempts must be >= 1");
        }
        String word = wordGenerator.getRandomWord();
        if (word == null || word.isBlank()) {
            throw new IllegalArgumentException("Generated word must be non-empty");
        }
        this.wordToGuess = normalize(word);
        this.guessedLetters = new HashSet<>();
        this.remainingAttempts = maxAttempts;
    }

    public boolean guess(char letter) {
        char normalized = normalize(letter);

        if (guessedLetters.contains(normalized)) {
            return wordToGuess.indexOf(normalized) >= 0;
        }

        guessedLetters.add(normalized);

        boolean isHit = wordToGuess.indexOf(normalized) >= 0;
        if (!isHit) {
            remainingAttempts = Math.max(0, remainingAttempts - 1);
        }
        return isHit;
    }

    public String getMaskedWord() {
        StringBuilder sb = new StringBuilder(wordToGuess.length());
        for (char c : wordToGuess.toCharArray()) {
            if (guessedLetters.contains(c)) {
                sb.append(c);
            } else {
                sb.append('_');
            }
        }
        return sb.toString();
    }

    public boolean isGameWon() {
        for (char c : wordToGuess.toCharArray()) {
            if (!guessedLetters.contains(c)) {
                return false;
            }
        }
        return true;
    }

    public boolean isGameOver() {
        return isGameWon() || remainingAttempts == 0;
    }

    public int getRemainingAttempts() {
        return remainingAttempts;
    }


    private static String normalize(String s) {
        return s.toLowerCase(Locale.ROOT);
    }

    private static char normalize(char c) {
        return Character.toLowerCase(c);
    }

    String getWordToGuess() {
        return wordToGuess;
    }
}
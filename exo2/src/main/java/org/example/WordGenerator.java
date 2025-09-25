package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class WordGenerator {
    private final List<String> words;
    private final Random random;

    public WordGenerator() {
        this(defaultWords(), new Random());
    }

    WordGenerator(List<String> words, Random random) {
        this.words = new ArrayList<>(Objects.requireNonNull(words, "words"));
        if (this.words.isEmpty()) {
            throw new IllegalArgumentException("words must not be empty");
        }
        this.random = Objects.requireNonNull(random, "random");
    }

    public String getRandomWord() {
        int idx = random.nextInt(words.size());
        return words.get(idx);
    }

    public String getWord(int index) {
        if (index < 0 || index >= words.size()) {
            throw new IndexOutOfBoundsException("index " + index + " out of 0.." + (words.size() - 1));
        }
        return words.get(index);
    }

    public int size() {
        return words.size();
    }

    private static List<String> defaultWords() {
        return List.of("java", "maven", "mockito", "junit", "hangman");
    }
}

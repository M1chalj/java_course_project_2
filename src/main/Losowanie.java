package main;

import java.util.Random;

public abstract class Losowanie {
    private static final Random generator = new Random();

    public static int losuj(int dolna, int górna) {
        return generator.nextInt(dolna, górna + 1);
    }

    public static Random generator() {
        return generator;
    }
}
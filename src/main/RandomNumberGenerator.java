package main;

import java.util.Random;

public abstract class RandomNumberGenerator {
    private static final Random generator = new Random();

    public static int randInt(int begin, int end) {
        return generator.nextInt(begin, end + 1);
    }

    public static Random generator() {
        return generator;
    }
}
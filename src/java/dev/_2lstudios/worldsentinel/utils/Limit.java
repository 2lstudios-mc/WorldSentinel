package dev._2lstudios.worldsentinel.utils;

public class Limit {
    private final int max;
    private int current;

    public Limit(final int max) {
        this.max = max;
    }

    public void add(final int addition) {
        this.current += addition;
    }

    public boolean reached() {
        return this.current >= this.max;
    }
}

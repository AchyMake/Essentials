package org.achymake.essentials.handlers;

import java.util.Random;

public class RandomHandler {
    private Random getRandom() {
        return new Random();
    }
    private double getRandomDouble() {
        return nextDouble(0, 1);
    }
    private double getRandomInt() {
        return getRandom().nextInt(0, 100);
    }
    /**
     * is true
     * @param chance double
     * @return true if chance is >= than random
     * @since many moons ago
     */
    public boolean isTrue(double chance) {
        return chance >= getRandomDouble();
    }
    /**
     * is true
     * @param chance integer
     * @return true if chance is >= than random
     * @since many moons ago
     */
    public boolean isTrue(int chance) {
        return chance >= getRandomInt();
    }
    /**
     * nextDouble
     * @param origin double
     * @param bound double
     * @return double
     * @since many moons ago
     */
    public double nextDouble(double origin, double bound) {
        return getRandom().nextDouble(origin, bound);
    }
    /**
     * nextInt
     * @param origin integer
     * @param bound integer
     * @return integer
     * @since many moons ago
     */
    public int nextInt(int origin, int bound) {
        return getRandom().nextInt(origin, bound);
    }
}
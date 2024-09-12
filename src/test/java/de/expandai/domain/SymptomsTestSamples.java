package de.expandai.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class SymptomsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Symptoms getSymptomsSample1() {
        return new Symptoms().id(1L);
    }

    public static Symptoms getSymptomsSample2() {
        return new Symptoms().id(2L);
    }

    public static Symptoms getSymptomsRandomSampleGenerator() {
        return new Symptoms().id(longCount.incrementAndGet());
    }
}

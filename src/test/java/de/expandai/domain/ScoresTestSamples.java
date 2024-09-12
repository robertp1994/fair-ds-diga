package de.expandai.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ScoresTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Scores getScoresSample1() {
        return new Scores().id(1L).questionnaire("questionnaire1").score(1L);
    }

    public static Scores getScoresSample2() {
        return new Scores().id(2L).questionnaire("questionnaire2").score(2L);
    }

    public static Scores getScoresRandomSampleGenerator() {
        return new Scores().id(longCount.incrementAndGet()).questionnaire(UUID.randomUUID().toString()).score(longCount.incrementAndGet());
    }
}

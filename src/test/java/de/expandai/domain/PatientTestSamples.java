package de.expandai.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PatientTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Patient getPatientSample1() {
        return new Patient().id("id1").gender("gender1").yearOfDiagnosis(1L);
    }

    public static Patient getPatientSample2() {
        return new Patient().id("id2").gender("gender2").yearOfDiagnosis(2L);
    }

    public static Patient getPatientRandomSampleGenerator() {
        return new Patient()
            .id(UUID.randomUUID().toString())
            .gender(UUID.randomUUID().toString())
            .yearOfDiagnosis(longCount.incrementAndGet());
    }
}

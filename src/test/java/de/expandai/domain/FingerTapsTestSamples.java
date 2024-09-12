package de.expandai.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FingerTapsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FingerTaps getFingerTapsSample1() {
        return new FingerTaps().id(1L).patientId("patientId1").thumbX("thumbX1").thumbY("thumbY1").digitX("digitX1").digitY("digitY1");
    }

    public static FingerTaps getFingerTapsSample2() {
        return new FingerTaps().id(2L).patientId("patientId2").thumbX("thumbX2").thumbY("thumbY2").digitX("digitX2").digitY("digitY2");
    }

    public static FingerTaps getFingerTapsRandomSampleGenerator() {
        return new FingerTaps()
            .id(longCount.incrementAndGet())
            .patientId(UUID.randomUUID().toString())
            .thumbX(UUID.randomUUID().toString())
            .thumbY(UUID.randomUUID().toString())
            .digitX(UUID.randomUUID().toString())
            .digitY(UUID.randomUUID().toString());
    }
}

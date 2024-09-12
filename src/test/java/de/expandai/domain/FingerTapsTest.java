package de.expandai.domain;

import static de.expandai.domain.FingerTapsTestSamples.*;
import static de.expandai.domain.PatientTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import de.expandai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FingerTapsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FingerTaps.class);
        FingerTaps fingerTaps1 = getFingerTapsSample1();
        FingerTaps fingerTaps2 = new FingerTaps();
        assertThat(fingerTaps1).isNotEqualTo(fingerTaps2);

        fingerTaps2.setId(fingerTaps1.getId());
        assertThat(fingerTaps1).isEqualTo(fingerTaps2);

        fingerTaps2 = getFingerTapsSample2();
        assertThat(fingerTaps1).isNotEqualTo(fingerTaps2);
    }

    @Test
    void patientTest() {
        FingerTaps fingerTaps = getFingerTapsRandomSampleGenerator();
        Patient patientBack = getPatientRandomSampleGenerator();

        fingerTaps.setPatient(patientBack);
        assertThat(fingerTaps.getPatient()).isEqualTo(patientBack);

        fingerTaps.patient(null);
        assertThat(fingerTaps.getPatient()).isNull();
    }
}

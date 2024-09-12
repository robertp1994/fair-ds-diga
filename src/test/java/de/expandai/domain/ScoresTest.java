package de.expandai.domain;

import static de.expandai.domain.PatientTestSamples.*;
import static de.expandai.domain.ScoresTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import de.expandai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScoresTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Scores.class);
        Scores scores1 = getScoresSample1();
        Scores scores2 = new Scores();
        assertThat(scores1).isNotEqualTo(scores2);

        scores2.setId(scores1.getId());
        assertThat(scores1).isEqualTo(scores2);

        scores2 = getScoresSample2();
        assertThat(scores1).isNotEqualTo(scores2);
    }

    @Test
    void patientTest() {
        Scores scores = getScoresRandomSampleGenerator();
        Patient patientBack = getPatientRandomSampleGenerator();

        scores.setPatient(patientBack);
        assertThat(scores.getPatient()).isEqualTo(patientBack);

        scores.patient(null);
        assertThat(scores.getPatient()).isNull();
    }
}

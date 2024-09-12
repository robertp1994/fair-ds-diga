package de.expandai.domain;

import static de.expandai.domain.FingerTapsTestSamples.*;
import static de.expandai.domain.PatientTestSamples.*;
import static de.expandai.domain.ScoresTestSamples.*;
import static de.expandai.domain.SymptomsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import de.expandai.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PatientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Patient.class);
        Patient patient1 = getPatientSample1();
        Patient patient2 = new Patient();
        assertThat(patient1).isNotEqualTo(patient2);

        patient2.setId(patient1.getId());
        assertThat(patient1).isEqualTo(patient2);

        patient2 = getPatientSample2();
        assertThat(patient1).isNotEqualTo(patient2);
    }

    @Test
    void fingerTapsTest() {
        Patient patient = getPatientRandomSampleGenerator();
        FingerTaps fingerTapsBack = getFingerTapsRandomSampleGenerator();

        patient.addFingerTaps(fingerTapsBack);
        assertThat(patient.getFingerTaps()).containsOnly(fingerTapsBack);
        assertThat(fingerTapsBack.getPatient()).isEqualTo(patient);

        patient.removeFingerTaps(fingerTapsBack);
        assertThat(patient.getFingerTaps()).doesNotContain(fingerTapsBack);
        assertThat(fingerTapsBack.getPatient()).isNull();

        patient.fingerTaps(new HashSet<>(Set.of(fingerTapsBack)));
        assertThat(patient.getFingerTaps()).containsOnly(fingerTapsBack);
        assertThat(fingerTapsBack.getPatient()).isEqualTo(patient);

        patient.setFingerTaps(new HashSet<>());
        assertThat(patient.getFingerTaps()).doesNotContain(fingerTapsBack);
        assertThat(fingerTapsBack.getPatient()).isNull();
    }

    @Test
    void scoresTest() {
        Patient patient = getPatientRandomSampleGenerator();
        Scores scoresBack = getScoresRandomSampleGenerator();

        patient.addScores(scoresBack);
        assertThat(patient.getScores()).containsOnly(scoresBack);
        assertThat(scoresBack.getPatient()).isEqualTo(patient);

        patient.removeScores(scoresBack);
        assertThat(patient.getScores()).doesNotContain(scoresBack);
        assertThat(scoresBack.getPatient()).isNull();

        patient.scores(new HashSet<>(Set.of(scoresBack)));
        assertThat(patient.getScores()).containsOnly(scoresBack);
        assertThat(scoresBack.getPatient()).isEqualTo(patient);

        patient.setScores(new HashSet<>());
        assertThat(patient.getScores()).doesNotContain(scoresBack);
        assertThat(scoresBack.getPatient()).isNull();
    }

    @Test
    void symptomsTest() {
        Patient patient = getPatientRandomSampleGenerator();
        Symptoms symptomsBack = getSymptomsRandomSampleGenerator();

        patient.addSymptoms(symptomsBack);
        assertThat(patient.getSymptoms()).containsOnly(symptomsBack);
        assertThat(symptomsBack.getPatient()).isEqualTo(patient);

        patient.removeSymptoms(symptomsBack);
        assertThat(patient.getSymptoms()).doesNotContain(symptomsBack);
        assertThat(symptomsBack.getPatient()).isNull();

        patient.symptoms(new HashSet<>(Set.of(symptomsBack)));
        assertThat(patient.getSymptoms()).containsOnly(symptomsBack);
        assertThat(symptomsBack.getPatient()).isEqualTo(patient);

        patient.setSymptoms(new HashSet<>());
        assertThat(patient.getSymptoms()).doesNotContain(symptomsBack);
        assertThat(symptomsBack.getPatient()).isNull();
    }
}

package de.expandai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Scores.
 */
@Entity
@Table(name = "scores")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Scores implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "time")
    private Instant time;

    @Column(name = "questionnaire")
    private String questionnaire;

    @Column(name = "score")
    private Long score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "fingerTaps", "scores", "symptoms" }, allowSetters = true)
    private Patient patient;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Scores id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTime() {
        return this.time;
    }

    public Scores time(Instant time) {
        this.setTime(time);
        return this;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getQuestionnaire() {
        return this.questionnaire;
    }

    public Scores questionnaire(String questionnaire) {
        this.setQuestionnaire(questionnaire);
        return this;
    }

    public void setQuestionnaire(String questionnaire) {
        this.questionnaire = questionnaire;
    }

    public Long getScore() {
        return this.score;
    }

    public Scores score(Long score) {
        this.setScore(score);
        return this;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Scores patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Scores)) {
            return false;
        }
        return getId() != null && getId().equals(((Scores) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Scores{" +
            "id=" + getId() +
            ", time='" + getTime() + "'" +
            ", questionnaire='" + getQuestionnaire() + "'" +
            ", score=" + getScore() +
            "}";
    }
}

package de.expandai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Patient.
 */
@Entity
@Table(name = "patient")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @org.hibernate.annotations.GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "gender")
    private String gender;

    @Column(name = "birth_date")
    private Instant birthDate;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "year_of_diagnosis")
    private Long yearOfDiagnosis;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    @JsonIgnoreProperties(value = { "patient" }, allowSetters = true)
    private Set<FingerTaps> fingerTaps = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    @JsonIgnoreProperties(value = { "patient" }, allowSetters = true)
    private Set<Scores> scores = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    @JsonIgnoreProperties(value = { "patient" }, allowSetters = true)
    private Set<Symptoms> symptoms = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Patient id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGender() {
        return this.gender;
    }

    public Patient gender(String gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Instant getBirthDate() {
        return this.birthDate;
    }

    public Patient birthDate(Instant birthDate) {
        this.setBirthDate(birthDate);
        return this;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Patient createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Long getYearOfDiagnosis() {
        return this.yearOfDiagnosis;
    }

    public Patient yearOfDiagnosis(Long yearOfDiagnosis) {
        this.setYearOfDiagnosis(yearOfDiagnosis);
        return this;
    }

    public void setYearOfDiagnosis(Long yearOfDiagnosis) {
        this.yearOfDiagnosis = yearOfDiagnosis;
    }

    public Set<FingerTaps> getFingerTaps() {
        return this.fingerTaps;
    }

    public void setFingerTaps(Set<FingerTaps> fingerTaps) {
        if (this.fingerTaps != null) {
            this.fingerTaps.forEach(i -> i.setPatient(null));
        }
        if (fingerTaps != null) {
            fingerTaps.forEach(i -> i.setPatient(this));
        }
        this.fingerTaps = fingerTaps;
    }

    public Patient fingerTaps(Set<FingerTaps> fingerTaps) {
        this.setFingerTaps(fingerTaps);
        return this;
    }

    public Patient addFingerTaps(FingerTaps fingerTaps) {
        this.fingerTaps.add(fingerTaps);
        fingerTaps.setPatient(this);
        return this;
    }

    public Patient removeFingerTaps(FingerTaps fingerTaps) {
        this.fingerTaps.remove(fingerTaps);
        fingerTaps.setPatient(null);
        return this;
    }

    public Set<Scores> getScores() {
        return this.scores;
    }

    public void setScores(Set<Scores> scores) {
        if (this.scores != null) {
            this.scores.forEach(i -> i.setPatient(null));
        }
        if (scores != null) {
            scores.forEach(i -> i.setPatient(this));
        }
        this.scores = scores;
    }

    public Patient scores(Set<Scores> scores) {
        this.setScores(scores);
        return this;
    }

    public Patient addScores(Scores scores) {
        this.scores.add(scores);
        scores.setPatient(this);
        return this;
    }

    public Patient removeScores(Scores scores) {
        this.scores.remove(scores);
        scores.setPatient(null);
        return this;
    }

    public Set<Symptoms> getSymptoms() {
        return this.symptoms;
    }

    public void setSymptoms(Set<Symptoms> symptoms) {
        if (this.symptoms != null) {
            this.symptoms.forEach(i -> i.setPatient(null));
        }
        if (symptoms != null) {
            symptoms.forEach(i -> i.setPatient(this));
        }
        this.symptoms = symptoms;
    }

    public Patient symptoms(Set<Symptoms> symptoms) {
        this.setSymptoms(symptoms);
        return this;
    }

    public Patient addSymptoms(Symptoms symptoms) {
        this.symptoms.add(symptoms);
        symptoms.setPatient(this);
        return this;
    }

    public Patient removeSymptoms(Symptoms symptoms) {
        this.symptoms.remove(symptoms);
        symptoms.setPatient(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Patient)) {
            return false;
        }
        return getId() != null && getId().equals(((Patient) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Patient{" +
            "id=" + getId() +
            ", gender='" + getGender() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", yearOfDiagnosis=" + getYearOfDiagnosis() +
            "}";
    }
}

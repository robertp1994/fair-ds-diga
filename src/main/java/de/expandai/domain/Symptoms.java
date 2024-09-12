package de.expandai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.expandai.domain.enumeration.STATUS;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Symptoms.
 */
@Entity
@Table(name = "symptoms")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Symptoms implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "time")
    private Instant time;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private STATUS status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "fingerTaps", "scores", "symptoms" }, allowSetters = true)
    private Patient patient;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Symptoms id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTime() {
        return this.time;
    }

    public Symptoms time(Instant time) {
        this.setTime(time);
        return this;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public STATUS getStatus() {
        return this.status;
    }

    public Symptoms status(STATUS status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Symptoms patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Symptoms)) {
            return false;
        }
        return getId() != null && getId().equals(((Symptoms) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Symptoms{" +
            "id=" + getId() +
            ", time='" + getTime() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}

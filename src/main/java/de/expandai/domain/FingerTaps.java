package de.expandai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.expandai.domain.enumeration.SIDE;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A FingerTaps.
 */
@Entity
@Table(name = "finger_taps")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FingerTaps implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "patient_id", insertable = false, updatable = false)
    private String patientId;

    @Column(name = "date")
    private Instant date;

    @Enumerated(EnumType.STRING)
    @Column(name = "side")
    private SIDE side;

    @Column(name = "thumb_x")
    private String thumbX;

    @Column(name = "thumb_y")
    private String thumbY;

    @Column(name = "digit_x")
    private String digitX;

    @Column(name = "digit_y")
    private String digitY;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "fingerTaps", "scores", "symptoms" }, allowSetters = true)
    private Patient patient;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FingerTaps id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientId() {
        return this.patientId;
    }

    public FingerTaps patientId(String patientId) {
        this.setPatientId(patientId);
        return this;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Instant getDate() {
        return this.date;
    }

    public FingerTaps date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public SIDE getSide() {
        return this.side;
    }

    public FingerTaps side(SIDE side) {
        this.setSide(side);
        return this;
    }

    public void setSide(SIDE side) {
        this.side = side;
    }

    public String getThumbX() {
        return this.thumbX;
    }

    public FingerTaps thumbX(String thumbX) {
        this.setThumbX(thumbX);
        return this;
    }

    public void setThumbX(String thumbX) {
        this.thumbX = thumbX;
    }

    public String getThumbY() {
        return this.thumbY;
    }

    public FingerTaps thumbY(String thumbY) {
        this.setThumbY(thumbY);
        return this;
    }

    public void setThumbY(String thumbY) {
        this.thumbY = thumbY;
    }

    public String getDigitX() {
        return this.digitX;
    }

    public FingerTaps digitX(String digitX) {
        this.setDigitX(digitX);
        return this;
    }

    public void setDigitX(String digitX) {
        this.digitX = digitX;
    }

    public String getDigitY() {
        return this.digitY;
    }

    public FingerTaps digitY(String digitY) {
        this.setDigitY(digitY);
        return this;
    }

    public void setDigitY(String digitY) {
        this.digitY = digitY;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public FingerTaps patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FingerTaps)) {
            return false;
        }
        return getId() != null && getId().equals(((FingerTaps) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FingerTaps{" +
            "id=" + getId() +
            ", patientId='" + getPatientId() + "'" +
            ", date='" + getDate() + "'" +
            ", side='" + getSide() + "'" +
            ", thumbX='" + getThumbX() + "'" +
            ", thumbY='" + getThumbY() + "'" +
            ", digitX='" + getDigitX() + "'" +
            ", digitY='" + getDigitY() + "'" +
            "}";
    }
}

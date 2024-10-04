import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPatient } from 'app/shared/model/patient.model';
import { getEntities as getPatients } from 'app/entities/patient/patient.reducer';
import { IFingerTaps } from 'app/shared/model/finger-taps.model';
import { SIDE } from 'app/shared/model/enumerations/side.model';
import { getEntity, updateEntity, createEntity, reset } from './finger-taps.reducer';

export const FingerTapsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const patients = useAppSelector(state => state.patient.entities);
  const fingerTapsEntity = useAppSelector(state => state.fingerTaps.entity);
  const loading = useAppSelector(state => state.fingerTaps.loading);
  const updating = useAppSelector(state => state.fingerTaps.updating);
  const updateSuccess = useAppSelector(state => state.fingerTaps.updateSuccess);
  const sIDEValues = Object.keys(SIDE);

  const handleClose = () => {
    navigate('/finger-taps');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPatients({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.date = convertDateTimeToServer(values.date);

    const entity = {
      ...fingerTapsEntity,
      ...values,
      patient: patients.find(it => it.id.toString() === values.patient?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          date: displayDefaultDateTime(),
        }
      : {
          side: 'LEFT',
          ...fingerTapsEntity,
          date: convertDateTimeFromServer(fingerTapsEntity.date),
          patient: fingerTapsEntity?.patient?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="fitApp.fingerTaps.home.createOrEditLabel" data-cy="FingerTapsCreateUpdateHeading">
            Create or edit a Finger Taps
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="finger-taps-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Patient Id" id="finger-taps-patientId" name="patientId" data-cy="patientId" type="text" />
              <ValidatedField
                label="Date"
                id="finger-taps-date"
                name="date"
                data-cy="date"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Side" id="finger-taps-side" name="side" data-cy="side" type="select">
                {sIDEValues.map(sIDE => (
                  <option value={sIDE} key={sIDE}>
                    {sIDE}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label="Thumb X" id="finger-taps-thumbX" name="thumbX" data-cy="thumbX" type="text" />
              <ValidatedField label="Thumb Y" id="finger-taps-thumbY" name="thumbY" data-cy="thumbY" type="text" />
              <ValidatedField label="Digit X" id="finger-taps-digitX" name="digitX" data-cy="digitX" type="text" />
              <ValidatedField label="Digit Y" id="finger-taps-digitY" name="digitY" data-cy="digitY" type="text" />
              <ValidatedField id="finger-taps-patient" name="patient" data-cy="patient" label="Patient" type="select">
                <option value="" key="0" />
                {patients
                  ? patients.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/finger-taps" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default FingerTapsUpdate;

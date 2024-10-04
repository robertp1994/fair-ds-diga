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
import { ISymptoms } from 'app/shared/model/symptoms.model';
import { STATUS } from 'app/shared/model/enumerations/status.model';
import { getEntity, updateEntity, createEntity, reset } from './symptoms.reducer';

export const SymptomsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const patients = useAppSelector(state => state.patient.entities);
  const symptomsEntity = useAppSelector(state => state.symptoms.entity);
  const loading = useAppSelector(state => state.symptoms.loading);
  const updating = useAppSelector(state => state.symptoms.updating);
  const updateSuccess = useAppSelector(state => state.symptoms.updateSuccess);
  const sTATUSValues = Object.keys(STATUS);

  const handleClose = () => {
    navigate('/symptoms');
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
    values.time = convertDateTimeToServer(values.time);

    const entity = {
      ...symptomsEntity,
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
          time: displayDefaultDateTime(),
        }
      : {
          status: 'RED',
          ...symptomsEntity,
          time: convertDateTimeFromServer(symptomsEntity.time),
          patient: symptomsEntity?.patient?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="fitApp.symptoms.home.createOrEditLabel" data-cy="SymptomsCreateUpdateHeading">
            Create or edit a Symptoms
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="symptoms-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Time"
                id="symptoms-time"
                name="time"
                data-cy="time"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Status" id="symptoms-status" name="status" data-cy="status" type="select">
                {sTATUSValues.map(sTATUS => (
                  <option value={sTATUS} key={sTATUS}>
                    {sTATUS}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="symptoms-patient" name="patient" data-cy="patient" label="Patient" type="select">
                <option value="" key="0" />
                {patients
                  ? patients.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/symptoms" replace color="info">
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

export default SymptomsUpdate;

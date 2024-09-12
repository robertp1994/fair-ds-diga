import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './symptoms.reducer';

export const SymptomsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const symptomsEntity = useAppSelector(state => state.symptoms.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="symptomsDetailsHeading">Symptoms</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{symptomsEntity.id}</dd>
          <dt>
            <span id="time">Time</span>
          </dt>
          <dd>{symptomsEntity.time ? <TextFormat value={symptomsEntity.time} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{symptomsEntity.status}</dd>
          <dt>Patient</dt>
          <dd>{symptomsEntity.patient ? symptomsEntity.patient.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/symptoms" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Wstecz</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/symptoms/${symptomsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edytuj</span>
        </Button>
      </Col>
    </Row>
  );
};

export default SymptomsDetail;

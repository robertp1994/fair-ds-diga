import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './finger-taps.reducer';

export const FingerTapsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const fingerTapsEntity = useAppSelector(state => state.fingerTaps.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="fingerTapsDetailsHeading">Finger Taps</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{fingerTapsEntity.id}</dd>
          <dt>
            <span id="patientId">Patient Id</span>
          </dt>
          <dd>{fingerTapsEntity.patientId}</dd>
          <dt>
            <span id="date">Date</span>
          </dt>
          <dd>{fingerTapsEntity.date ? <TextFormat value={fingerTapsEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="side">Side</span>
          </dt>
          <dd>{fingerTapsEntity.side}</dd>
          <dt>
            <span id="thumbX">Thumb X</span>
          </dt>
          <dd>{fingerTapsEntity.thumbX}</dd>
          <dt>
            <span id="thumbY">Thumb Y</span>
          </dt>
          <dd>{fingerTapsEntity.thumbY}</dd>
          <dt>
            <span id="digitX">Digit X</span>
          </dt>
          <dd>{fingerTapsEntity.digitX}</dd>
          <dt>
            <span id="digitY">Digit Y</span>
          </dt>
          <dd>{fingerTapsEntity.digitY}</dd>
          <dt>Patient</dt>
          <dd>{fingerTapsEntity.patient ? fingerTapsEntity.patient.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/finger-taps" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Wstecz</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/finger-taps/${fingerTapsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edytuj</span>
        </Button>
      </Col>
    </Row>
  );
};

export default FingerTapsDetail;

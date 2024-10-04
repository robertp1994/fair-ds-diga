import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './scores.reducer';

export const ScoresDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const scoresEntity = useAppSelector(state => state.scores.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="scoresDetailsHeading">Scores</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{scoresEntity.id}</dd>
          <dt>
            <span id="time">Time</span>
          </dt>
          <dd>{scoresEntity.time ? <TextFormat value={scoresEntity.time} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="questionnaire">Questionnaire</span>
          </dt>
          <dd>{scoresEntity.questionnaire}</dd>
          <dt>
            <span id="score">Score</span>
          </dt>
          <dd>{scoresEntity.score}</dd>
          <dt>Patient</dt>
          <dd>{scoresEntity.patient ? scoresEntity.patient.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/scores" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/scores/${scoresEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ScoresDetail;

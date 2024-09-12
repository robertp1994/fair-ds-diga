import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './finger-taps.reducer';

export const FingerTaps = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const fingerTapsList = useAppSelector(state => state.fingerTaps.entities);
  const loading = useAppSelector(state => state.fingerTaps.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="finger-taps-heading" data-cy="FingerTapsHeading">
        Finger Taps
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/finger-taps/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Dodaj Finger Taps
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {fingerTapsList && fingerTapsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('patientId')}>
                  Patient Id <FontAwesomeIcon icon={getSortIconByFieldName('patientId')} />
                </th>
                <th className="hand" onClick={sort('date')}>
                  Date <FontAwesomeIcon icon={getSortIconByFieldName('date')} />
                </th>
                <th className="hand" onClick={sort('side')}>
                  Side <FontAwesomeIcon icon={getSortIconByFieldName('side')} />
                </th>
                <th className="hand" onClick={sort('thumbX')}>
                  Thumb X <FontAwesomeIcon icon={getSortIconByFieldName('thumbX')} />
                </th>
                <th className="hand" onClick={sort('thumbY')}>
                  Thumb Y <FontAwesomeIcon icon={getSortIconByFieldName('thumbY')} />
                </th>
                <th className="hand" onClick={sort('digitX')}>
                  Digit X <FontAwesomeIcon icon={getSortIconByFieldName('digitX')} />
                </th>
                <th className="hand" onClick={sort('digitY')}>
                  Digit Y <FontAwesomeIcon icon={getSortIconByFieldName('digitY')} />
                </th>
                <th>
                  Patient <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {fingerTapsList.map((fingerTaps, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/finger-taps/${fingerTaps.id}`} color="link" size="sm">
                      {fingerTaps.id}
                    </Button>
                  </td>
                  <td>{fingerTaps.patientId}</td>
                  <td>{fingerTaps.date ? <TextFormat type="date" value={fingerTaps.date} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{fingerTaps.side}</td>
                  <td>{fingerTaps.thumbX}</td>
                  <td>{fingerTaps.thumbY}</td>
                  <td>{fingerTaps.digitX}</td>
                  <td>{fingerTaps.digitY}</td>
                  <td>{fingerTaps.patient ? <Link to={`/patient/${fingerTaps.patient.id}`}>{fingerTaps.patient.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/finger-taps/${fingerTaps.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">Widok</span>
                      </Button>
                      <Button tag={Link} to={`/finger-taps/${fingerTaps.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edytuj</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/finger-taps/${fingerTaps.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Usuń</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Finger Taps found</div>
        )}
      </div>
    </div>
  );
};

export default FingerTaps;

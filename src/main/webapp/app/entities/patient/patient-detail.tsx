// import React, { useEffect } from 'react';
// import { Link, useParams } from 'react-router-dom';
// import { Button, Row, Col } from 'reactstrap';
// import { TextFormat } from 'react-jhipster';
// import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
//
// import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
// import { useAppDispatch, useAppSelector } from 'app/config/store';
//
// import { getEntity } from './patient.reducer';
//
// export const PatientDetail = () => {
//   const dispatch = useAppDispatch();
//
//   const { id } = useParams<'id'>();
//
//   useEffect(() => {
//     dispatch(getEntity(id));
//   }, []);
//
//   const patientEntity = useAppSelector(state => state.patient.entity);
//   return (
//     <Row>
//       <Col md="8">
//         <h2 data-cy="patientDetailsHeading">Patient</h2>
//         <dl className="jh-entity-details">
//           <dt>
//             <span id="id">Id</span>
//           </dt>
//           <dd>{patientEntity.id}</dd>
//           <dt>
//             <span id="gender">Gender</span>
//           </dt>
//           <dd>{patientEntity.gender}</dd>
//           <dt>
//             <span id="birthDate">Birth Date</span>
//           </dt>
//           <dd>{patientEntity.birthDate ? <TextFormat value={patientEntity.birthDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
//           <dt>
//             <span id="createdAt">Created At</span>
//           </dt>
//           <dd>{patientEntity.createdAt ? <TextFormat value={patientEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
//           <dt>
//             <span id="yearOfDiagnosis">Year Of Diagnosis</span>
//           </dt>
//           <dd>{patientEntity.yearOfDiagnosis}</dd>
//         </dl>
//         <Button tag={Link} to="/patient" replace color="info" data-cy="entityDetailsBackButton">
//           <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Wstecz</span>
//         </Button>
//         &nbsp;
//         <Button tag={Link} to={`/patient/${patientEntity.id}/edit`} replace color="primary">
//           <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edytuj</span>
//         </Button>
//       </Col>
//     </Row>
//   );
// };
//
// export default PatientDetail;

import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import FingertapsChart from 'app/entities/finger-taps/FingertapsChart';

const PatientDetail = () => {
  const { id } = useParams(); // Fetch the patient ID from the route parameters
  const [fingertapsData, setFingertapsData] = useState([]);

  useEffect(() => {
    // Fetch fingertaps data for the selected patient
    axios
      .get(`/api/finger-taps/patient/${id}`)
      .then(response => {
        setFingertapsData(response.data);
      })
      .catch(error => {
        console.error('Error fetching fingertaps data', error);
      });
  }, [id]);

  // Transforming data if necessary (e.g., parsing the coordinates)
  const transformedData = fingertapsData.map(fingertap => ({
    date: fingertap.date,
    thumbX: JSON.parse(fingertap.thumbX), // Assuming the coordinates are stored as JSON arrays
    thumbY: JSON.parse(fingertap.thumbY),
    digitX: JSON.parse(fingertap.digitX),
    digitY: JSON.parse(fingertap.digitY),
  }));

  return (
    <div>
      <h2>Patient {id}'s Fingertaps</h2>
      <FingertapsChart data={transformedData} />
    </div>
  );
};

export default PatientDetail;

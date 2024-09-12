import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Patient from './patient';
import FingerTaps from './finger-taps';
import Scores from './scores';
import Symptoms from './symptoms';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="patient/*" element={<Patient />} />
        <Route path="finger-taps/*" element={<FingerTaps />} />
        <Route path="scores/*" element={<Scores />} />
        <Route path="symptoms/*" element={<Symptoms />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};

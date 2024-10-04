import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

// import FingerTaps from './finger-taps';
import FingerTapsDetail from './finger-taps-detail';
import FingerTapsUpdate from './finger-taps-update';
import FingerTapsDeleteDialog from './finger-taps-delete-dialog';
import FingerTapsForPatient from 'app/entities/finger-taps/finger-taps-for-patient';

const FingerTapsRoutes = () => (
  <ErrorBoundaryRoutes>
    {/*<Route index element={<FingerTaps />} />*/}
    <Route index element={<FingerTapsForPatient />} />
    <Route path="new" element={<FingerTapsUpdate />} />
    <Route path=":id">
      <Route index element={<FingerTapsDetail />} />
      <Route path="edit" element={<FingerTapsUpdate />} />
      <Route path="delete" element={<FingerTapsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FingerTapsRoutes;

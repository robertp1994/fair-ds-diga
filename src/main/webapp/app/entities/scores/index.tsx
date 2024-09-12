import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Scores from './scores';
import ScoresDetail from './scores-detail';
import ScoresUpdate from './scores-update';
import ScoresDeleteDialog from './scores-delete-dialog';

const ScoresRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Scores />} />
    <Route path="new" element={<ScoresUpdate />} />
    <Route path=":id">
      <Route index element={<ScoresDetail />} />
      <Route path="edit" element={<ScoresUpdate />} />
      <Route path="delete" element={<ScoresDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ScoresRoutes;

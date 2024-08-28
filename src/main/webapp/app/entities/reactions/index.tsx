import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Reactions from './reactions';
import ReactionsDetail from './reactions-detail';
import ReactionsUpdate from './reactions-update';
import ReactionsDeleteDialog from './reactions-delete-dialog';

const ReactionsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Reactions />} />
    <Route path="new" element={<ReactionsUpdate />} />
    <Route path=":id">
      <Route index element={<ReactionsDetail />} />
      <Route path="edit" element={<ReactionsUpdate />} />
      <Route path="delete" element={<ReactionsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ReactionsRoutes;

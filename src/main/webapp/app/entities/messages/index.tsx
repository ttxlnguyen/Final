import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Messages from './messages';
import MessagesDetail from './messages-detail';
import MessagesUpdate from './messages-update';
import MessagesDeleteDialog from './messages-delete-dialog';

const MessagesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Messages />} />
    <Route path="new" element={<MessagesUpdate />} />
    <Route path=":id">
      <Route index element={<MessagesDetail />} />
      <Route path="edit" element={<MessagesUpdate />} />
      <Route path="delete" element={<MessagesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MessagesRoutes;

import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Channels from './channels';
import ChannelsDetail from './channels-detail';
import ChannelsUpdate from './channels-update';
import ChannelsDeleteDialog from './channels-delete-dialog';

const ChannelsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Channels />} />
    <Route path="new" element={<ChannelsUpdate />} />
    <Route path=":id">
      <Route index element={<ChannelsDetail />} />
      <Route path="edit" element={<ChannelsUpdate />} />
      <Route path="delete" element={<ChannelsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ChannelsRoutes;

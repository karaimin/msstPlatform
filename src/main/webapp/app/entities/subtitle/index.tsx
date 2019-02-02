import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Subtitle from './subtitle';
import SubtitleDetail from './subtitle-detail';
import SubtitleUpdate from './subtitle-update';
import SubtitleDeleteDialog from './subtitle-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SubtitleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SubtitleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SubtitleDetail} />
      <ErrorBoundaryRoute path={match.url} component={Subtitle} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={SubtitleDeleteDialog} />
  </>
);

export default Routes;

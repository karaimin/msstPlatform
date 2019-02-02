import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SubtitleLine from './subtitle-line';
import SubtitleLineDetail from './subtitle-line-detail';
import SubtitleLineUpdate from './subtitle-line-update';
import SubtitleLineDeleteDialog from './subtitle-line-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SubtitleLineUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SubtitleLineUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SubtitleLineDetail} />
      <ErrorBoundaryRoute path={match.url} component={SubtitleLine} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={SubtitleLineDeleteDialog} />
  </>
);

export default Routes;

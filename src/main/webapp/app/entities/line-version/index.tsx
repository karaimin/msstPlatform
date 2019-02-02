import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import LineVersion from './line-version';
import LineVersionDetail from './line-version-detail';
import LineVersionUpdate from './line-version-update';
import LineVersionDeleteDialog from './line-version-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LineVersionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LineVersionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LineVersionDetail} />
      <ErrorBoundaryRoute path={match.url} component={LineVersion} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={LineVersionDeleteDialog} />
  </>
);

export default Routes;

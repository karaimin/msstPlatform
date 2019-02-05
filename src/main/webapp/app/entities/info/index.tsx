import React from 'react';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import MovieInfo from './movie-info';
import MovieInfoDisplay from './movie-info-display';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MovieInfoDisplay} />
      <ErrorBoundaryRoute path={match.url} component={MovieInfo} />
    </Switch>
  </>
);

export default Routes;

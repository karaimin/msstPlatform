import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import MovieInfo from './movie-info';

const Routes = ({ match }) => (
  <div>
    <Switch>
      <ErrorBoundaryRoute path={match.url} component={MovieInfo} />
    </Switch>
  </div>
);

export default Routes;

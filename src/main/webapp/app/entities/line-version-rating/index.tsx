import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import LineVersionRating from './line-version-rating';
import LineVersionRatingDetail from './line-version-rating-detail';
import LineVersionRatingUpdate from './line-version-rating-update';
import LineVersionRatingDeleteDialog from './line-version-rating-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LineVersionRatingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LineVersionRatingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LineVersionRatingDetail} />
      <ErrorBoundaryRoute path={match.url} component={LineVersionRating} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={LineVersionRatingDeleteDialog} />
  </>
);

export default Routes;

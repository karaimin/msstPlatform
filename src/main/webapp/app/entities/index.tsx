import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Movie from './movie';
import Subtitle from './subtitle';
import SubtitleLine from './subtitle-line';
import LineVersion from './line-version';
import LineVersionRating from './line-version-rating';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/movie`} component={Movie} />
      <ErrorBoundaryRoute path={`${match.url}/subtitle`} component={Subtitle} />
      <ErrorBoundaryRoute path={`${match.url}/subtitle-line`} component={SubtitleLine} />
      <ErrorBoundaryRoute path={`${match.url}/line-version`} component={LineVersion} />
      <ErrorBoundaryRoute path={`${match.url}/line-version-rating`} component={LineVersionRating} />
      {/* jhipster-needle-add-route-path - JHipster will routes here */}
    </Switch>
  </div>
);

export default Routes;

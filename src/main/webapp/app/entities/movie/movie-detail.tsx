import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './movie.reducer';
import { IMovie } from 'app/shared/model/movie.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMovieDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class MovieDetail extends React.Component<IMovieDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { movieEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Movie [<b>{movieEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{movieEntity.name}</dd>
            <dt>
              <span id="duration">Duration</span>
            </dt>
            <dd>
              <TextFormat value={movieEntity.duration} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="description">Description</span>
            </dt>
            <dd>{movieEntity.description}</dd>
            <dt>Subtitle</dt>
            <dd>{movieEntity.subtitle ? movieEntity.subtitle.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/movie" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/movie/${movieEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ movie }: IRootState) => ({
  movieEntity: movie.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MovieDetail);

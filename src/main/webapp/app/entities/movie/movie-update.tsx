import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ISubtitle } from 'app/shared/model/subtitle.model';
import { getEntities as getSubtitles } from 'app/entities/subtitle/subtitle.reducer';
import { getEntity, updateEntity, createEntity, reset } from './movie.reducer';
import { IMovie } from 'app/shared/model/movie.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IMovieUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IMovieUpdateState {
  isNew: boolean;
  subtitleId: string;
}

export class MovieUpdate extends React.Component<IMovieUpdateProps, IMovieUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      subtitleId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getSubtitles();
  }

  saveEntity = (event, errors, values) => {
    values.duration = convertDateTimeToServer(values.duration);

    if (errors.length === 0) {
      const { movieEntity } = this.props;
      const entity = {
        ...movieEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/movie');
  };

  render() {
    const { movieEntity, subtitles, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="msstPlatformApp.movie.home.createOrEditLabel">Create or edit a Movie</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : movieEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="movie-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="name">
                    Name
                  </Label>
                  <AvField id="movie-name" type="text" name="name" />
                </AvGroup>
                <AvGroup>
                  <Label id="durationLabel" for="duration">
                    Duration
                  </Label>
                  <AvInput
                    id="movie-duration"
                    type="datetime-local"
                    className="form-control"
                    name="duration"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.movieEntity.duration)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="description">
                    Description
                  </Label>
                  <AvField id="movie-description" type="text" name="description" />
                </AvGroup>
                <AvGroup>
                  <Label for="subtitle.id">Subtitle</Label>
                  <AvInput id="movie-subtitle" type="select" className="form-control" name="subtitle.id">
                    <option value="" key="0" />
                    {subtitles
                      ? subtitles.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/movie" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  subtitles: storeState.subtitle.entities,
  movieEntity: storeState.movie.entity,
  loading: storeState.movie.loading,
  updating: storeState.movie.updating,
  updateSuccess: storeState.movie.updateSuccess
});

const mapDispatchToProps = {
  getSubtitles,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MovieUpdate);

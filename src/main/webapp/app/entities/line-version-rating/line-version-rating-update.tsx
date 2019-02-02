import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ILineVersion } from 'app/shared/model/line-version.model';
import { getEntities as getLineVersions } from 'app/entities/line-version/line-version.reducer';
import { getEntity, updateEntity, createEntity, reset } from './line-version-rating.reducer';
import { ILineVersionRating } from 'app/shared/model/line-version-rating.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ILineVersionRatingUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ILineVersionRatingUpdateState {
  isNew: boolean;
  lineVersionId: string;
}

export class LineVersionRatingUpdate extends React.Component<ILineVersionRatingUpdateProps, ILineVersionRatingUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      lineVersionId: '0',
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

    this.props.getLineVersions();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { lineVersionRatingEntity } = this.props;
      const entity = {
        ...lineVersionRatingEntity,
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
    this.props.history.push('/entity/line-version-rating');
  };

  render() {
    const { lineVersionRatingEntity, lineVersions, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="msstPlatformApp.lineVersionRating.home.createOrEditLabel">Create or edit a LineVersionRating</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : lineVersionRatingEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="line-version-rating-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="ratingLabel" for="rating">
                    Rating
                  </Label>
                  <AvField id="line-version-rating-rating" type="string" className="form-control" name="rating" />
                </AvGroup>
                <AvGroup>
                  <Label id="commentLabel" for="comment">
                    Comment
                  </Label>
                  <AvField id="line-version-rating-comment" type="text" name="comment" />
                </AvGroup>
                <AvGroup>
                  <Label for="lineVersion.id">Line Version</Label>
                  <AvInput id="line-version-rating-lineVersion" type="select" className="form-control" name="lineVersion.id">
                    <option value="" key="0" />
                    {lineVersions
                      ? lineVersions.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/line-version-rating" replace color="info">
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
  lineVersions: storeState.lineVersion.entities,
  lineVersionRatingEntity: storeState.lineVersionRating.entity,
  loading: storeState.lineVersionRating.loading,
  updating: storeState.lineVersionRating.updating,
  updateSuccess: storeState.lineVersionRating.updateSuccess
});

const mapDispatchToProps = {
  getLineVersions,
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
)(LineVersionRatingUpdate);

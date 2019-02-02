import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ILineVersionRating } from 'app/shared/model/line-version-rating.model';
import { getEntities as getLineVersionRatings } from 'app/entities/line-version-rating/line-version-rating.reducer';
import { getEntity, updateEntity, createEntity, reset } from './line-version.reducer';
import { ILineVersion } from 'app/shared/model/line-version.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ILineVersionUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ILineVersionUpdateState {
  isNew: boolean;
  lineVersionRatingId: string;
}

export class LineVersionUpdate extends React.Component<ILineVersionUpdateProps, ILineVersionUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      lineVersionRatingId: '0',
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

    this.props.getLineVersionRatings();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { lineVersionEntity } = this.props;
      const entity = {
        ...lineVersionEntity,
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
    this.props.history.push('/entity/line-version');
  };

  render() {
    const { lineVersionEntity, lineVersionRatings, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="msstPlatformApp.lineVersion.home.createOrEditLabel">Create or edit a LineVersion</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : lineVersionEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="line-version-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="versionLabel" for="version">
                    Version
                  </Label>
                  <AvField id="line-version-version" type="text" name="version" />
                </AvGroup>
                <AvGroup>
                  <Label id="textLabel" for="text">
                    Text
                  </Label>
                  <AvField id="line-version-text" type="text" name="text" />
                </AvGroup>
                <AvGroup>
                  <Label for="lineVersionRating.id">Line Version Rating</Label>
                  <AvInput id="line-version-lineVersionRating" type="select" className="form-control" name="lineVersionRating.id">
                    <option value="" key="0" />
                    {lineVersionRatings
                      ? lineVersionRatings.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/line-version" replace color="info">
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
  lineVersionRatings: storeState.lineVersionRating.entities,
  lineVersionEntity: storeState.lineVersion.entity,
  loading: storeState.lineVersion.loading,
  updating: storeState.lineVersion.updating,
  updateSuccess: storeState.lineVersion.updateSuccess
});

const mapDispatchToProps = {
  getLineVersionRatings,
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
)(LineVersionUpdate);

import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ISubtitle } from 'app/shared/model/subtitle.model';
import { getEntities as getSubtitles } from 'app/entities/subtitle/subtitle.reducer';
import { getEntity, updateEntity, createEntity, reset } from './subtitle-line.reducer';
import { ISubtitleLine } from 'app/shared/model/subtitle-line.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISubtitleLineUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ISubtitleLineUpdateState {
  isNew: boolean;
  subtitleId: string;
}

export class SubtitleLineUpdate extends React.Component<ISubtitleLineUpdateProps, ISubtitleLineUpdateState> {
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
    values.startTime = convertDateTimeToServer(values.startTime);
    values.endTime = convertDateTimeToServer(values.endTime);

    if (errors.length === 0) {
      const { subtitleLineEntity } = this.props;
      const entity = {
        ...subtitleLineEntity,
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
    this.props.history.push('/entity/subtitle-line');
  };

  render() {
    const { subtitleLineEntity, subtitles, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="msstPlatformApp.subtitleLine.home.createOrEditLabel">Create or edit a SubtitleLine</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : subtitleLineEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="subtitle-line-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="startTimeLabel" for="startTime">
                    Start Time
                  </Label>
                  <AvInput
                    id="subtitle-line-startTime"
                    type="datetime-local"
                    className="form-control"
                    name="startTime"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.subtitleLineEntity.startTime)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="endTimeLabel" for="endTime">
                    End Time
                  </Label>
                  <AvInput
                    id="subtitle-line-endTime"
                    type="datetime-local"
                    className="form-control"
                    name="endTime"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.subtitleLineEntity.endTime)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="subtitle.id">Subtitle</Label>
                  <AvInput id="subtitle-line-subtitle" type="select" className="form-control" name="subtitle.id">
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
                <Button tag={Link} id="cancel-save" to="/entity/subtitle-line" replace color="info">
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
  subtitleLineEntity: storeState.subtitleLine.entity,
  loading: storeState.subtitleLine.loading,
  updating: storeState.subtitleLine.updating,
  updateSuccess: storeState.subtitleLine.updateSuccess
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
)(SubtitleLineUpdate);

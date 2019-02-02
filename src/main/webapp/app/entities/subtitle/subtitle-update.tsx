import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntities as getSubtitles } from 'app/entities/subtitle/subtitle.reducer';
import { ISubtitleLine } from 'app/shared/model/subtitle-line.model';
import { getEntities as getSubtitleLines } from 'app/entities/subtitle-line/subtitle-line.reducer';
import { getEntity, updateEntity, createEntity, reset } from './subtitle.reducer';
import { ISubtitle } from 'app/shared/model/subtitle.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISubtitleUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ISubtitleUpdateState {
  isNew: boolean;
  subtitleId: string;
  sourceId: string;
  subtitleLineId: string;
}

export class SubtitleUpdate extends React.Component<ISubtitleUpdateProps, ISubtitleUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      subtitleId: '0',
      sourceId: '0',
      subtitleLineId: '0',
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
    this.props.getSubtitleLines();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { subtitleEntity } = this.props;
      const entity = {
        ...subtitleEntity,
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
    this.props.history.push('/entity/subtitle');
  };

  render() {
    const { subtitleEntity, subtitles, subtitleLines, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="msstPlatformApp.subtitle.home.createOrEditLabel">Create or edit a Subtitle</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : subtitleEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="subtitle-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="versionLabel" for="version">
                    Version
                  </Label>
                  <AvField id="subtitle-version" type="text" name="version" />
                </AvGroup>
                <AvGroup>
                  <Label for="subtitle.id">Subtitle</Label>
                  <AvInput id="subtitle-subtitle" type="select" className="form-control" name="subtitle.id">
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
                <AvGroup>
                  <Label for="subtitleLine.id">Subtitle Line</Label>
                  <AvInput id="subtitle-subtitleLine" type="select" className="form-control" name="subtitleLine.id">
                    <option value="" key="0" />
                    {subtitleLines
                      ? subtitleLines.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/subtitle" replace color="info">
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
  subtitleLines: storeState.subtitleLine.entities,
  subtitleEntity: storeState.subtitle.entity,
  loading: storeState.subtitle.loading,
  updating: storeState.subtitle.updating,
  updateSuccess: storeState.subtitle.updateSuccess
});

const mapDispatchToProps = {
  getSubtitles,
  getSubtitleLines,
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
)(SubtitleUpdate);

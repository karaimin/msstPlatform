import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './subtitle.reducer';
import { ISubtitle } from 'app/shared/model/subtitle.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISubtitleDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class SubtitleDetail extends React.Component<ISubtitleDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { subtitleEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Subtitle [<b>{subtitleEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="version">Version</span>
            </dt>
            <dd>{subtitleEntity.version}</dd>
            <dt>Subtitle</dt>
            <dd>{subtitleEntity.subtitle ? subtitleEntity.subtitle.id : ''}</dd>
            <dt>Subtitle Line</dt>
            <dd>{subtitleEntity.subtitleLine ? subtitleEntity.subtitleLine.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/subtitle" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/subtitle/${subtitleEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ subtitle }: IRootState) => ({
  subtitleEntity: subtitle.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SubtitleDetail);

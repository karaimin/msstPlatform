import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './subtitle-line.reducer';
import { ISubtitleLine } from 'app/shared/model/subtitle-line.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISubtitleLineDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class SubtitleLineDetail extends React.Component<ISubtitleLineDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { subtitleLineEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            SubtitleLine [<b>{subtitleLineEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="startTime">Start Time</span>
            </dt>
            <dd>
              <TextFormat value={subtitleLineEntity.startTime} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="endTime">End Time</span>
            </dt>
            <dd>
              <TextFormat value={subtitleLineEntity.endTime} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>Subtitle</dt>
            <dd>{subtitleLineEntity.subtitle ? subtitleLineEntity.subtitle.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/subtitle-line" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/subtitle-line/${subtitleLineEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ subtitleLine }: IRootState) => ({
  subtitleLineEntity: subtitleLine.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SubtitleLineDetail);

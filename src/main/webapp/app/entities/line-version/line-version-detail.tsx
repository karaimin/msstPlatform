import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './line-version.reducer';
import { ILineVersion } from 'app/shared/model/line-version.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ILineVersionDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class LineVersionDetail extends React.Component<ILineVersionDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { lineVersionEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            LineVersion [<b>{lineVersionEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="version">Version</span>
            </dt>
            <dd>{lineVersionEntity.version}</dd>
            <dt>
              <span id="text">Text</span>
            </dt>
            <dd>{lineVersionEntity.text}</dd>
            <dt>Subtitle Line</dt>
            <dd>{lineVersionEntity.subtitleLine ? lineVersionEntity.subtitleLine.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/line-version" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/line-version/${lineVersionEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ lineVersion }: IRootState) => ({
  lineVersionEntity: lineVersion.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(LineVersionDetail);

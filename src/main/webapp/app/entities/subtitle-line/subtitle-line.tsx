import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './subtitle-line.reducer';
import { ISubtitleLine } from 'app/shared/model/subtitle-line.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISubtitleLineProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class SubtitleLine extends React.Component<ISubtitleLineProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { subtitleLineList, match } = this.props;
    return (
      <div>
        <h2 id="subtitle-line-heading">
          Subtitle Lines
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Subtitle Line
          </Link>
        </h2>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Start Time</th>
                <th>End Time</th>
                <th>Subtitle</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {subtitleLineList.map((subtitleLine, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${subtitleLine.id}`} color="link" size="sm">
                      {subtitleLine.id}
                    </Button>
                  </td>
                  <td>
                    <TextFormat type="date" value={subtitleLine.startTime} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    <TextFormat type="date" value={subtitleLine.endTime} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    {subtitleLine.subtitle ? <Link to={`subtitle/${subtitleLine.subtitle.id}`}>{subtitleLine.subtitle.id}</Link> : ''}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${subtitleLine.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${subtitleLine.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${subtitleLine.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ subtitleLine }: IRootState) => ({
  subtitleLineList: subtitleLine.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SubtitleLine);

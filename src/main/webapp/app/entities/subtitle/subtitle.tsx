import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './subtitle.reducer';
import { ISubtitle } from 'app/shared/model/subtitle.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISubtitleProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Subtitle extends React.Component<ISubtitleProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { subtitleList, match } = this.props;
    return (
      <div>
        <h2 id="subtitle-heading">
          Subtitles
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Subtitle
          </Link>
        </h2>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Version</th>
                <th>Movie</th>
                <th>Subtitle</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {subtitleList.map((subtitle, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${subtitle.id}`} color="link" size="sm">
                      {subtitle.id}
                    </Button>
                  </td>
                  <td>{subtitle.version}</td>
                  <td>{subtitle.movie ? <Link to={`movie/${subtitle.movie.id}`}>{subtitle.movie.id}</Link> : ''}</td>
                  <td>{subtitle.subtitle ? <Link to={`subtitle/${subtitle.subtitle.id}`}>{subtitle.subtitle.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${subtitle.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${subtitle.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${subtitle.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ subtitle }: IRootState) => ({
  subtitleList: subtitle.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Subtitle);

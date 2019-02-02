import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './line-version-rating.reducer';
import { ILineVersionRating } from 'app/shared/model/line-version-rating.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ILineVersionRatingProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class LineVersionRating extends React.Component<ILineVersionRatingProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { lineVersionRatingList, match } = this.props;
    return (
      <div>
        <h2 id="line-version-rating-heading">
          Line Version Ratings
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Line Version Rating
          </Link>
        </h2>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Rating</th>
                <th>Comment</th>
                <th>Line Version</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {lineVersionRatingList.map((lineVersionRating, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${lineVersionRating.id}`} color="link" size="sm">
                      {lineVersionRating.id}
                    </Button>
                  </td>
                  <td>{lineVersionRating.rating}</td>
                  <td>{lineVersionRating.comment}</td>
                  <td>
                    {lineVersionRating.lineVersion ? (
                      <Link to={`line-version/${lineVersionRating.lineVersion.id}`}>{lineVersionRating.lineVersion.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${lineVersionRating.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${lineVersionRating.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${lineVersionRating.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ lineVersionRating }: IRootState) => ({
  lineVersionRatingList: lineVersionRating.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(LineVersionRating);

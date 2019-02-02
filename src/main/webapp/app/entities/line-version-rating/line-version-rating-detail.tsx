import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './line-version-rating.reducer';
import { ILineVersionRating } from 'app/shared/model/line-version-rating.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ILineVersionRatingDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class LineVersionRatingDetail extends React.Component<ILineVersionRatingDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { lineVersionRatingEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            LineVersionRating [<b>{lineVersionRatingEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="rating">Rating</span>
            </dt>
            <dd>{lineVersionRatingEntity.rating}</dd>
            <dt>
              <span id="comment">Comment</span>
            </dt>
            <dd>{lineVersionRatingEntity.comment}</dd>
            <dt>Line Version</dt>
            <dd>{lineVersionRatingEntity.lineVersion ? lineVersionRatingEntity.lineVersion.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/line-version-rating" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/line-version-rating/${lineVersionRatingEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ lineVersionRating }: IRootState) => ({
  lineVersionRatingEntity: lineVersionRating.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(LineVersionRatingDetail);

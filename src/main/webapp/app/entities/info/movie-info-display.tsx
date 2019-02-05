import './movie-info.scss';
import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Col, Row, Table, Container } from 'reactstrap';

import { IRootState } from 'app/shared/reducers';
import { getMovieInfo } from './movie-info.reducer';
import { IMovieInfo } from 'app/shared/model/movie-Info.model';

export interface IMovieInfoDisplayProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class MovieInfoDisplay extends React.Component<IMovieInfoDisplayProps> {
  componentDidMount() {
    this.props.getMovieInfo(this.props.match.params.id);
  }

  render() {
    const { movieEntity } = this.props;
    return (
      <Container>
        <Row>
          <Col lg="8">
            <h1 className="mt-4">{movieEntity.name}</h1>

            <hr />

            <hr />

            <img className="img-fluid rounded" src="http://placehold.it/900x300" alt="" />

            <hr />
            <p>{movieEntity.description}</p>
          </Col>
          <Col md="4">
            <div className="card my-4">
              <h5 className="card-header">Translated Subtitles</h5>
              <div className="card-body">
                <ul className="list-unstyled mb-0">
                  {movieEntity.translatedSubtitles
                    ? movieEntity.translatedSubtitles.map((tsub, i) => (
                        <li>
                          <Link to="#" className="alert-link">
                            {`${movieEntity.name}-${tsub.language}-${tsub.version}`}
                          </Link>
                        </li>
                      ))
                    : ' '}
                </ul>
              </div>
            </div>
            <div className="card my-4">
              <h5 className="card-header">Translation started subtitles</h5>
              <div className="card-body">
                <ul className="list-unstyled mb-0">
                  {movieEntity.pendingSubtitles
                    ? movieEntity.pendingSubtitles.map((psub, i) => (
                        <li>
                          <Link to="#" className="alert-link">
                            {`${movieEntity.name}-${psub.language}-${psub.version}`}
                          </Link>
                        </li>
                      ))
                    : ' '}
                </ul>
              </div>
            </div>
          </Col>
        </Row>
      </Container>
    );
  }
}

const mapStateToProps = ({ movieInfo }: IRootState) => ({
  movieEntity: movieInfo.entity
});

const mapDispatchToProps = { getMovieInfo };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MovieInfoDisplay);

import './movie-info.scss';
import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Card, CardBody, CardImg, CardSubtitle, CardText, CardTitle, Col, Container, Row } from 'reactstrap';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from '../movie/movie.reducer';

export interface IMovieProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class MovieInfo extends React.Component<IMovieProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { movieList, match } = this.props;
    return (
      <Container>
        {movieList.map((movie, i) => (
          <div className="movie-card">
            <Card>
              <CardImg top width="100%" src="content/images/movies/movie-logo.png" alt="Card image cap" />
              <CardBody>
                <CardTitle>{movie.name}</CardTitle>
                <CardSubtitle>Description</CardSubtitle>
                <CardText>{movie.description}</CardText>
                <Button tag={Link} to={`${match.url}/${movie.id}`}>
                  View
                </Button>
              </CardBody>
            </Card>
          </div>
        ))}
      </Container>
    );
  }
}

const mapStateToProps = ({ movie }: IRootState) => ({
  movieList: movie.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MovieInfo);

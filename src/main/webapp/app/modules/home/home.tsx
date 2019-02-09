import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';

import { connect } from 'react-redux';
import { Container, Row, Col, Alert } from 'reactstrap';

import { IRootState } from 'app/shared/reducers';
import { getSession } from 'app/shared/reducers/authentication';
import MovieCarousel from 'app/modules/home/carousel';

export interface IHomeProp extends StateProps, DispatchProps {}

export class Home extends React.Component<IHomeProp> {
  componentDidMount() {
    this.props.getSession();
  }

  render() {
    const { account } = this.props;
    return (
      <Container fluid>
        <Row>
          <MovieCarousel activeIndex={0} animating />
          <Col md="12">
            <h2>Welcome to Shared subititle translation platform!</h2>
            <p className="lead">This is your homepage</p>
            {account && account.login ? (
              <div>
                <Alert color="success">You are logged in as user {account.login}.</Alert>
              </div>
            ) : (
              <div>
                <Alert color="warning">
                  If you want to participate in subtitle translation process please
                  <Link to="/login" className="alert-link">
                    {' '}
                    sign in
                  </Link>
                </Alert>

                <Alert color="warning">
                  You do not have an account yet?&nbsp;
                  <Link to="/register" className="alert-link">
                    Register a new account
                  </Link>
                </Alert>
              </div>
            )}
          </Col>
        </Row>
      </Container>
    );
  }
}

const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated
});

const mapDispatchToProps = { getSession };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Home);

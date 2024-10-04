import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';

import { Row, Col, Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <Row>
      <Col md="3" className="pad">
        <span className="hipster rounded" />
      </Col>
      <Col md="9">
        <h1 className="display-4">Hello</h1>
        <p className="lead">This is starting page</p>
        {account?.login ? (
          <div>
            <Alert color="success">Jesteś zalogowany jako użytkownik &quot;{account.login}&quot;.</Alert>
          </div>
        ) : (
          <div>
            <Alert color="warning">
              If you want to
              <span>&nbsp;</span>
              <Link to="/login" className="alert-link">
                login
              </Link>
              with default credentials use:
              <br />- Admin (login=&quot;admin&quot; pass=&quot;admin&quot;) <br />- User (login=&quot;user&quot; i pass=&quot;user&quot;).
            </Alert>

            <Alert color="warning">
              You do not have account?&nbsp;
              <Link to="/account/register" className="alert-link">
                Sign up
              </Link>
            </Alert>
          </div>
        )}
      </Col>
    </Row>
  );
};

export default Home;

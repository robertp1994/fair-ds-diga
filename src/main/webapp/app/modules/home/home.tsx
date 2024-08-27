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
        <h1 className="display-4">Witamy, Java Hipsterze!</h1>
        <p className="lead">To jest twoja strona domowa</p>
        {account?.login ? (
          <div>
            <Alert color="success">Jesteś zalogowany jako użytkownik &quot;{account.login}&quot;.</Alert>
          </div>
        ) : (
          <div>
            <Alert color="warning">
              Jeśli chcesz się
              <span>&nbsp;</span>
              <Link to="/login" className="alert-link">
                zalogować
              </Link>
              , możesz spróbować któregoś z domyślnych kont:
              <br />- Administrator (login=&quot;admin&quot; i hasło=&quot;admin&quot;) <br />- Użytkownik (login=&quot;user&quot; i
              hasło=&quot;user&quot;).
            </Alert>

            <Alert color="warning">
              Nie masz jeszcze konta?&nbsp;
              <Link to="/account/register" className="alert-link">
                Zarejestruj się
              </Link>
            </Alert>
          </div>
        )}
        <p>Jeśli masz jakiekolwiek pytania na temat JHipster:</p>

        <ul>
          <li>
            <a href="https://www.jhipster.tech/" target="_blank" rel="noopener noreferrer">
              Strona domowa JHipster
            </a>
          </li>
          <li>
            <a href="https://stackoverflow.com/tags/jhipster/info" target="_blank" rel="noopener noreferrer">
              JHipster na Stack Overflow
            </a>
          </li>
          <li>
            <a href="https://github.com/jhipster/generator-jhipster/issues?state=open" target="_blank" rel="noopener noreferrer">
              JHipster bug tracker
            </a>
          </li>
          <li>
            <a href="https://gitter.im/jhipster/generator-jhipster" target="_blank" rel="noopener noreferrer">
              Publiczny chat room JHipster
            </a>
          </li>
          <li>
            <a href="https://twitter.com/jhipster" target="_blank" rel="noopener noreferrer">
              skontaktuj się z @jhipster na Twitterze
            </a>
          </li>
        </ul>

        <p>
          Jeśli lubisz JHipster, nie zapomnij dać nam gwiazdki na{' '}
          <a href="https://github.com/jhipster/generator-jhipster" target="_blank" rel="noopener noreferrer">
            GitHub
          </a>
          !
        </p>
      </Col>
    </Row>
  );
};

export default Home;

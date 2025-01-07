import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';

import { Row, Col, Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <Row>
      <h1 className="display-4">Hello</h1>
      <p className="lead">
        The goal of this demonstrator is a secure and legally compliant method to collect and manage data from various wearable sensor
        sources (e.g. smartphone and smartwatch), which can then be used to train AI algorithms. The prototype focuses on integrating
        AI-based DiGAs into the healthcare ecosystem by adhering to the FAIR (Findability, Accessibility, Interoperability, and Reusability)
        and Gaia-X principles. These guidelines ensure that data management is transparent, accessible, and fair. The ability to gather and
        analyze data about health conditions and influencing factors is crucial for developing new therapies and holistic care strategies.
      </p>
      <p className="lead">
        At the core of this project is a data integration center, which gathers and standardizes Parkinson patient data from a smartphone
        (iPhone 12 Pro Max) and smartwatch (Apple Watch Series 8). AI analytics within the center enable near real-time data insights,
        supporting Parkinson healthcare professionals and researchers in decision-making and advancing medical research. Our prototype
        proposes a framework for secure data exchange between scientific institutions and industry partners. This collaboration can empower
        healthcare providers to monitor Parkinson patient health using a simple traffic light system to indicate their condition: green for
        well, yellow for unwell but not critical, and red for emergencies. This system will be accessible via a web platform for doctors,
        providing valuable insights while minimizing data sharing. Moreover, limited access to these databases for companies developing
        DiGAs could enhance AI model training and product development, ultimately advancing personalized healthcare and improving patient
        outcomes.
      </p>

      <div className="embed-responsive embed-responsive-16by9">
        <iframe
          width={1600}
          height={900}
          className="embed-responsive-item center-iframe"
          src="https://www.youtube.com/embed/kcr1ColnTHM"
          allowFullScreen
          title="No Patients Found"
        ></iframe>
      </div>
      <span>&nbsp;</span>
      {account?.login ? (
        <div>
          <Alert color="success">You are logged in as &quot;{account.login}&quot;.</Alert>
        </div>
      ) : (
        <div>
          <Alert color="warning">
            If you want to
            <span>&nbsp;</span>
            <Link to="/login" className="alert-link">
              login
            </Link>
            <span>&nbsp;</span>
            with default credentials use:
            <br />- Admin (login=&quot;admin&quot; pass=&quot;admin&quot;) <br />- User (login=&quot;user&quot; i pass=&quot;user&quot;).
            You do not have account?&nbsp;
            <Link to="/account/register" className="alert-link">
              Sign up
            </Link>
          </Alert>
        </div>
      )}
    </Row>
  );
};

export default Home;

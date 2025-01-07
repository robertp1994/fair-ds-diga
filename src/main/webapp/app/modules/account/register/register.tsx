import React, { useState, useEffect } from 'react';
import { ValidatedField, ValidatedForm, isEmail } from 'react-jhipster';
import { Row, Col, Alert, Button } from 'reactstrap';
import { toast } from 'react-toastify';
import { Link } from 'react-router-dom';

import PasswordStrengthBar from 'app/shared/layout/password/password-strength-bar';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { handleRegister, reset } from './register.reducer';

export const RegisterPage = () => {
  const [password, setPassword] = useState('');
  const dispatch = useAppDispatch();

  useEffect(
    () => () => {
      dispatch(reset());
    },
    [],
  );

  const handleValidSubmit = ({ username, email, firstPassword }) => {
    dispatch(handleRegister({ login: username, email, password: firstPassword, langKey: 'en' }));
  };

  const updatePassword = event => setPassword(event.target.value);

  const successMessage = useAppSelector(state => state.register.successMessage);

  useEffect(() => {
    if (successMessage) {
      toast.success(successMessage);
    }
  }, [successMessage]);

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h1 id="register-title" data-cy="registerTitle">
            Sign up
          </h1>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          <ValidatedForm id="register-form" onSubmit={handleValidSubmit}>
            <ValidatedField
              name="username"
              label="Username"
              placeholder="Your login"
              validate={{
                required: { value: true, message: 'Your login is required.' },
                pattern: {
                  value: /^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$/,
                  message: 'Incorrect username.',
                },
                minLength: { value: 1, message: 'Your login must be at least 1 character long' },
                maxLength: { value: 50, message: 'Your login must not be longer than 50 characters' },
              }}
              data-cy="username"
            />
            <ValidatedField
              name="email"
              label="Email"
              placeholder="Your email"
              type="email"
              validate={{
                required: { value: true, message: 'Your email is required.' },
                minLength: { value: 5, message: 'Your email must have at least 5 characters' },
                maxLength: { value: 254, message: 'Your email must not be longer than 50 characters' },
                validate: v => isEmail(v) || 'Your email is incorrect.',
              }}
              data-cy="email"
            />
            <ValidatedField
              name="firstPassword"
              label="New password"
              placeholder="New password"
              type="password"
              onChange={updatePassword}
              validate={{
                required: { value: true, message: 'Your password is required.' },
                minLength: { value: 4, message: 'Your password should have a minimum of 4 characters' },
                maxLength: { value: 50, message: 'Your password must not be longer than 50 characters' },
              }}
              data-cy="firstPassword"
            />
            <PasswordStrengthBar password={password} />
            <ValidatedField
              name="secondPassword"
              label="Confirm new password"
              placeholder="Confirm new password"
              type="password"
              validate={{
                required: { value: true, message: 'Confirmation of the password is required.' },
                minLength: { value: 4, message: 'Password confirmation should have a minimum of 4 characters' },
                maxLength: { value: 50, message: 'Password confirmation should not be longer than 50 characters' },
                validate: v => v === password || 'Password and confirmation do not match!',
              }}
              data-cy="secondPassword"
            />
            <Button id="register-submit" color="primary" type="submit" data-cy="submit">
              Sign up
            </Button>
          </ValidatedForm>
          <p>&nbsp;</p>
          <Alert color="warning">
            <span>If you want to </span>
            <Link to="/login" className="alert-link">
              log in
            </Link>
            <span>
              , you can try one of default accounts:
              <br />- Admin (login=&quot;admin&quot; pass=&quot;admin&quot;) <br />- User (login=&quot;user&quot; pass=&quot;user&quot;).
            </span>
          </Alert>
        </Col>
      </Row>
    </div>
  );
};

export default RegisterPage;

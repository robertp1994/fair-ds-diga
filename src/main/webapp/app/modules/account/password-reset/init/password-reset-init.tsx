import React, { useEffect } from 'react';
import { ValidatedField, ValidatedForm, isEmail } from 'react-jhipster';
import { Button, Alert, Col, Row } from 'reactstrap';
import { toast } from 'react-toastify';

import { handlePasswordResetInit, reset } from '../password-reset.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PasswordResetInit = () => {
  const dispatch = useAppDispatch();

  useEffect(
    () => () => {
      dispatch(reset());
    },
    [],
  );

  const handleValidSubmit = ({ email }) => {
    dispatch(handlePasswordResetInit(email));
  };

  const successMessage = useAppSelector(state => state.passwordReset.successMessage);

  useEffect(() => {
    if (successMessage) {
      toast.success(successMessage);
    }
  }, [successMessage]);

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h1>Reset your password</h1>
          <Alert color="warning">
            <p>Email used during registration.</p>
          </Alert>
          <ValidatedForm onSubmit={handleValidSubmit}>
            <ValidatedField
              name="email"
              label="Email"
              placeholder="Email"
              type="email"
              validate={{
                required: { value: true, message: 'Your email is required.' },
                minLength: { value: 5, message: 'Your email must have at least 5 characters' },
                maxLength: { value: 254, message: 'Your email must not be longer than 50 characters' },
                validate: v => isEmail(v) || 'Your email is incorrect.',
              }}
              data-cy="emailResetPassword"
            />
            <Button color="primary" type="submit" data-cy="submit">
              Password reset
            </Button>
          </ValidatedForm>
        </Col>
      </Row>
    </div>
  );
};

export default PasswordResetInit;

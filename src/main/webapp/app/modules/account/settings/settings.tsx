import React, { useEffect } from 'react';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm, isEmail } from 'react-jhipster';
import { toast } from 'react-toastify';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getSession } from 'app/shared/reducers/authentication';
import { saveAccountSettings, reset } from './settings.reducer';

export const SettingsPage = () => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const successMessage = useAppSelector(state => state.settings.successMessage);

  useEffect(() => {
    dispatch(getSession());
    return () => {
      dispatch(reset());
    };
  }, []);

  useEffect(() => {
    if (successMessage) {
      toast.success(successMessage);
    }
  }, [successMessage]);

  const handleValidSubmit = values => {
    dispatch(
      saveAccountSettings({
        ...account,
        ...values,
      }),
    );
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="settings-title">
            User settings [<strong>{account.login}</strong>]
          </h2>
          <ValidatedForm id="settings-form" onSubmit={handleValidSubmit} defaultValues={account}>
            <ValidatedField
              name="firstName"
              label="Name"
              id="firstName"
              placeholder="Your name"
              validate={{
                required: { value: true, message: 'Your name is required.' },
                minLength: { value: 1, message: 'Your name must have at least 1 letter' },
                maxLength: { value: 50, message: 'Your name must not be longer than 50 letters' },
              }}
              data-cy="firstname"
            />
            <ValidatedField
              name="lastName"
              label="Last name"
              id="lastName"
              placeholder="Your last name"
              validate={{
                required: { value: true, message: 'Your name is required.' },
                minLength: { value: 1, message: 'Your name must have at least 1 letter' },
                maxLength: { value: 50, message: 'Your name must not be longer than 50 letters' },
              }}
              data-cy="lastname"
            />
            <ValidatedField
              name="email"
              label="Email"
              placeholder="Your email"
              type="email"
              validate={{
                required: { value: true, message: 'Your email is required.' },
                minLength: { value: 5, message: "Your email must have at least 5 characters'" },
                maxLength: { value: 254, message: 'Your email must not be longer than 50 characters' },
                validate: v => isEmail(v) || 'Your email is incorrect.',
              }}
              data-cy="email"
            />
            <Button color="primary" type="submit" data-cy="submit">
              Save
            </Button>
          </ValidatedForm>
        </Col>
      </Row>
    </div>
  );
};

export default SettingsPage;

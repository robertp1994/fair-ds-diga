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
            Ustawienia dla użytkownika [<strong>{account.login}</strong>]
          </h2>
          <ValidatedForm id="settings-form" onSubmit={handleValidSubmit} defaultValues={account}>
            <ValidatedField
              name="firstName"
              label="Imię"
              id="firstName"
              placeholder="Twoje imię"
              validate={{
                required: { value: true, message: 'Twoje imię jest wymagane.' },
                minLength: { value: 1, message: 'Twoje imię musi mieć przynajmniej 1 literę' },
                maxLength: { value: 50, message: 'Twoje imię nie może być dłuższe niż 50 liter' },
              }}
              data-cy="firstname"
            />
            <ValidatedField
              name="lastName"
              label="Nazwisko"
              id="lastName"
              placeholder="Twoje nazwisko"
              validate={{
                required: { value: true, message: 'Twoje nazwisko jest wymagane.' },
                minLength: { value: 1, message: 'Twoje nazwisko musi mieć przynajmniej 1 literę' },
                maxLength: { value: 50, message: 'Twoje nazwisko nie może być dłuższe niż 50 liter' },
              }}
              data-cy="lastname"
            />
            <ValidatedField
              name="email"
              label="Email"
              placeholder="Twój email"
              type="email"
              validate={{
                required: { value: true, message: 'Twój email jest wymagany.' },
                minLength: { value: 5, message: 'Twój email musi mieć przynajmniej 5 znaków' },
                maxLength: { value: 254, message: 'Twój email nie może być dłuższy niż 50 znaków' },
                validate: v => isEmail(v) || 'Twój email jest błędny.',
              }}
              data-cy="email"
            />
            <Button color="primary" type="submit" data-cy="submit">
              Zapisz
            </Button>
          </ValidatedForm>
        </Col>
      </Row>
    </div>
  );
};

export default SettingsPage;

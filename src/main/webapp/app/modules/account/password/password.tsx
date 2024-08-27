import React, { useState, useEffect } from 'react';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { Row, Col, Button } from 'reactstrap';
import { toast } from 'react-toastify';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getSession } from 'app/shared/reducers/authentication';
import PasswordStrengthBar from 'app/shared/layout/password/password-strength-bar';
import { savePassword, reset } from './password.reducer';

export const PasswordPage = () => {
  const [password, setPassword] = useState('');
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(reset());
    dispatch(getSession());
    return () => {
      dispatch(reset());
    };
  }, []);

  const handleValidSubmit = ({ currentPassword, newPassword }) => {
    dispatch(savePassword({ currentPassword, newPassword }));
  };

  const updatePassword = event => setPassword(event.target.value);

  const account = useAppSelector(state => state.authentication.account);
  const successMessage = useAppSelector(state => state.password.successMessage);
  const errorMessage = useAppSelector(state => state.password.errorMessage);

  useEffect(() => {
    if (successMessage) {
      toast.success(successMessage);
    } else if (errorMessage) {
      toast.error(errorMessage);
    }
    dispatch(reset());
  }, [successMessage, errorMessage]);

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="password-title">
            Hasło dla [<strong>{account.login}</strong>]
          </h2>
          <ValidatedForm id="password-form" onSubmit={handleValidSubmit}>
            <ValidatedField
              name="currentPassword"
              label="Aktualne hasło"
              placeholder="Aktualne hasło"
              type="password"
              validate={{
                required: { value: true, message: 'Twoje hasło jest wymagane.' },
              }}
              data-cy="currentPassword"
            />
            <ValidatedField
              name="newPassword"
              label="Nowe hasło"
              placeholder="Nowe hasło"
              type="password"
              validate={{
                required: { value: true, message: 'Twoje hasło jest wymagane.' },
                minLength: { value: 4, message: 'Twoje hasło powinno mieć minimum 4 znaki' },
                maxLength: { value: 50, message: 'Twoje hasło nie może być dłuższe niż 50 znaków' },
              }}
              onChange={updatePassword}
              data-cy="newPassword"
            />
            <PasswordStrengthBar password={password} />
            <ValidatedField
              name="confirmPassword"
              label="Potwierdź nowe hasło"
              placeholder="Potwierdź nowe hasło"
              type="password"
              validate={{
                required: { value: true, message: 'Potwierdzenie hasła jest wymagane.' },
                minLength: { value: 4, message: 'Potwierdzenie hasła powinno mieć minimum 4 znaki' },
                maxLength: { value: 50, message: 'Potwierdzenie hasła nie powinno być dłuższe niż 50 znaków' },
                validate: v => v === password || 'Hasło i potwierdzenie nie zgadzają się!',
              }}
              data-cy="confirmPassword"
            />
            <Button color="success" type="submit" data-cy="submit">
              Zapisz
            </Button>
          </ValidatedForm>
        </Col>
      </Row>
    </div>
  );
};

export default PasswordPage;

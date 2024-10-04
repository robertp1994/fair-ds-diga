import React, { useState, useEffect } from 'react';
import { Col, Row, Button } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { useSearchParams } from 'react-router-dom';
import { toast } from 'react-toastify';

import { handlePasswordResetFinish, reset } from '../password-reset.reducer';
import PasswordStrengthBar from 'app/shared/layout/password/password-strength-bar';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PasswordResetFinishPage = () => {
  const dispatch = useAppDispatch();

  const [searchParams] = useSearchParams();
  const key = searchParams.get('key');

  const [password, setPassword] = useState('');

  useEffect(
    () => () => {
      dispatch(reset());
    },
    [],
  );

  const handleValidSubmit = ({ newPassword }) => dispatch(handlePasswordResetFinish({ key, newPassword }));

  const updatePassword = event => setPassword(event.target.value);

  const getResetForm = () => {
    return (
      <ValidatedForm onSubmit={handleValidSubmit}>
        <ValidatedField
          name="newPassword"
          label="New password"
          placeholder="New Password"
          type="password"
          validate={{
            required: { value: true, message: 'Twoje hasło jest wymagane.' },
            minLength: { value: 4, message: 'Twoje hasło powinno mieć minimum 4 znaki' },
            maxLength: { value: 50, message: 'Twoje hasło nie może być dłuższe niż 50 znaków' },
          }}
          onChange={updatePassword}
          data-cy="resetPassword"
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
          data-cy="confirmResetPassword"
        />
        <Button color="success" type="submit" data-cy="submit">
          Sprawdź nowe hasło
        </Button>
      </ValidatedForm>
    );
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
        <Col md="4">
          <h1>Reset hasła</h1>
          <div>{key ? getResetForm() : null}</div>
        </Col>
      </Row>
    </div>
  );
};

export default PasswordResetFinishPage;

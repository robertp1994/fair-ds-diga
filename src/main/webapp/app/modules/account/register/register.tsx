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
            Rejestracja
          </h1>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          <ValidatedForm id="register-form" onSubmit={handleValidSubmit}>
            <ValidatedField
              name="username"
              label="Nazwa użytkownika"
              placeholder="Twój login"
              validate={{
                required: { value: true, message: 'Twój login jest wymagany.' },
                pattern: {
                  value: /^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$/,
                  message: 'Niewłaściwa nazwa użytkownika.',
                },
                minLength: { value: 1, message: 'Twój login musi mieć długość co najmniej 1 znaku' },
                maxLength: { value: 50, message: 'Twój login nie może być dłuższy niż 50 znaków' },
              }}
              data-cy="username"
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
            <ValidatedField
              name="firstPassword"
              label="Nowe hasło"
              placeholder="Nowe hasło"
              type="password"
              onChange={updatePassword}
              validate={{
                required: { value: true, message: 'Twoje hasło jest wymagane.' },
                minLength: { value: 4, message: 'Twoje hasło powinno mieć minimum 4 znaki' },
                maxLength: { value: 50, message: 'Twoje hasło nie może być dłuższe niż 50 znaków' },
              }}
              data-cy="firstPassword"
            />
            <PasswordStrengthBar password={password} />
            <ValidatedField
              name="secondPassword"
              label="Potwierdź nowe hasło"
              placeholder="Potwierdź nowe hasło"
              type="password"
              validate={{
                required: { value: true, message: 'Potwierdzenie hasła jest wymagane.' },
                minLength: { value: 4, message: 'Potwierdzenie hasła powinno mieć minimum 4 znaki' },
                maxLength: { value: 50, message: 'Potwierdzenie hasła nie powinno być dłuższe niż 50 znaków' },
                validate: v => v === password || 'Hasło i potwierdzenie nie zgadzają się!',
              }}
              data-cy="secondPassword"
            />
            <Button id="register-submit" color="primary" type="submit" data-cy="submit">
              Zarejestruj
            </Button>
          </ValidatedForm>
          <p>&nbsp;</p>
          <Alert color="warning">
            <span>Jeśli chcesz się </span>
            <Link to="/login" className="alert-link">
              zalogować
            </Link>
            <span>
              , możesz spróbować któregoś z domyślnych kont:
              <br />- Administrator (login=&quot;admin&quot; i hasło=&quot;admin&quot;) <br />- Użytkownik (login=&quot;user&quot; i
              hasło=&quot;user&quot;).
            </span>
          </Alert>
        </Col>
      </Row>
    </div>
  );
};

export default RegisterPage;

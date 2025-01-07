import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';

import { NavDropdown } from './menu-components';

const accountMenuItemsAuthenticated = () => (
  <>
    <MenuItem icon="wrench" to="/account/settings" data-cy="settings">
      Settings
    </MenuItem>
    <MenuItem icon="lock" to="/account/password" data-cy="passwordItem">
      Password
    </MenuItem>
    <MenuItem icon="sign-out-alt" to="/logout" data-cy="logout">
      Logout
    </MenuItem>
  </>
);

const accountMenuItems = () => (
  <>
    <MenuItem id="login-item" icon="sign-in-alt" to="/login" data-cy="login">
      Authorization
    </MenuItem>
    <MenuItem icon="user-plus" to="/account/register" data-cy="register">
      Sign up
    </MenuItem>
  </>
);

export const AccountMenu = ({ isAuthenticated = false }) => (
  <NavDropdown
    icon="user"
    name={
      <>
        <span>&nbsp;</span>Account
      </>
    }
    id="account-menu"
    data-cy="accountMenu"
  >
    {isAuthenticated && accountMenuItemsAuthenticated()}
    {!isAuthenticated && accountMenuItems()}
  </NavDropdown>
);

export default AccountMenu;

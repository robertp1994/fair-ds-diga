import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/patient">
        Patient
      </MenuItem>
      <MenuItem icon="asterisk" to="/finger-taps">
        Finger Taps
      </MenuItem>
      <MenuItem icon="asterisk" to="/scores">
        Scores
      </MenuItem>
      <MenuItem icon="asterisk" to="/symptoms">
        Symptoms
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;

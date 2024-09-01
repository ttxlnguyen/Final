import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/user-profile">
        <Translate contentKey="global.menu.entities.userProfile" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/messages">
        <Translate contentKey="global.menu.entities.messages" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/channels">
        <Translate contentKey="global.menu.entities.channels" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/reactions">
        <Translate contentKey="global.menu.entities.reactions" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;

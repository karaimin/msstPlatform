import React from 'react';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from '../header-components';

export const EntitiesMenu = props => (
  // tslint:disable-next-line:jsx-self-close
  <NavDropdown icon="th-list" name="Entities" id="entity-menu">
    <DropdownItem tag={Link} to="/entity/movie">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Movie
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/subtitle">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Subtitle
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/subtitle-line">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Subtitle Line
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/line-version">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Line Version
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/line-version-rating">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Line Version Rating
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/movie">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Movie
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/subtitle">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Subtitle
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/subtitle-line">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Subtitle Line
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/line-version">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Line Version
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/line-version-rating">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Line Version Rating
    </DropdownItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);

export default EntitiesMenu;

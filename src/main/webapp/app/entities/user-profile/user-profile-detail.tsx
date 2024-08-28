import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-profile.reducer';

export const UserProfileDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userProfileEntity = useAppSelector(state => state.userProfile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userProfileDetailsHeading">
          <Translate contentKey="finalApp.userProfile.detail.title">UserProfile</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.id}</dd>
          <dt>
            <span id="username">
              <Translate contentKey="finalApp.userProfile.username">Username</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.username}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="finalApp.userProfile.email">Email</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.email}</dd>
          <dt>
            <span id="password">
              <Translate contentKey="finalApp.userProfile.password">Password</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.password}</dd>
          <dt>
            <Translate contentKey="finalApp.userProfile.channels">Channels</Translate>
          </dt>
          <dd>
            {userProfileEntity.channels
              ? userProfileEntity.channels.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {userProfileEntity.channels && i === userProfileEntity.channels.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/user-profile" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-profile/${userProfileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserProfileDetail;

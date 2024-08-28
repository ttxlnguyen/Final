import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './channels.reducer';

export const ChannelsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const channelsEntity = useAppSelector(state => state.channels.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="channelsDetailsHeading">
          <Translate contentKey="finalApp.channels.detail.title">Channels</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{channelsEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="finalApp.channels.name">Name</Translate>
            </span>
          </dt>
          <dd>{channelsEntity.name}</dd>
          <dt>
            <Translate contentKey="finalApp.channels.userProfile">User Profile</Translate>
          </dt>
          <dd>
            {channelsEntity.userProfiles
              ? channelsEntity.userProfiles.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {channelsEntity.userProfiles && i === channelsEntity.userProfiles.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/channels" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/channels/${channelsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ChannelsDetail;

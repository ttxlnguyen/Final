import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './messages.reducer';

export const MessagesDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const messagesEntity = useAppSelector(state => state.messages.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="messagesDetailsHeading">
          <Translate contentKey="finalApp.messages.detail.title">Messages</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{messagesEntity.id}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="finalApp.messages.content">Content</Translate>
            </span>
          </dt>
          <dd>{messagesEntity.content}</dd>
          <dt>
            <span id="sentAt">
              <Translate contentKey="finalApp.messages.sentAt">Sent At</Translate>
            </span>
          </dt>
          <dd>{messagesEntity.sentAt ? <TextFormat value={messagesEntity.sentAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="editedAt">
              <Translate contentKey="finalApp.messages.editedAt">Edited At</Translate>
            </span>
          </dt>
          <dd>{messagesEntity.editedAt ? <TextFormat value={messagesEntity.editedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="finalApp.messages.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{messagesEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="finalApp.messages.userProfile">User Profile</Translate>
          </dt>
          <dd>{messagesEntity.userProfile ? messagesEntity.userProfile.id : ''}</dd>
          <dt>
            <Translate contentKey="finalApp.messages.channels">Channels</Translate>
          </dt>
          <dd>{messagesEntity.channels ? messagesEntity.channels.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/messages" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/messages/${messagesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MessagesDetail;

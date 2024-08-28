import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './reactions.reducer';

export const ReactionsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const reactionsEntity = useAppSelector(state => state.reactions.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="reactionsDetailsHeading">
          <Translate contentKey="finalApp.reactions.detail.title">Reactions</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{reactionsEntity.id}</dd>
          <dt>
            <span id="reaction">
              <Translate contentKey="finalApp.reactions.reaction">Reaction</Translate>
            </span>
          </dt>
          <dd>{reactionsEntity.reaction}</dd>
          <dt>
            <Translate contentKey="finalApp.reactions.user">User</Translate>
          </dt>
          <dd>{reactionsEntity.user ? reactionsEntity.user.id : ''}</dd>
          <dt>
            <Translate contentKey="finalApp.reactions.messages">Messages</Translate>
          </dt>
          <dd>{reactionsEntity.messages ? reactionsEntity.messages.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/reactions" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/reactions/${reactionsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReactionsDetail;

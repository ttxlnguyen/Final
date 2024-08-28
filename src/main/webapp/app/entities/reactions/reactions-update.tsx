import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { IMessages } from 'app/shared/model/messages.model';
import { getEntities as getMessages } from 'app/entities/messages/messages.reducer';
import { IReactions } from 'app/shared/model/reactions.model';
import { getEntity, updateEntity, createEntity, reset } from './reactions.reducer';

export const ReactionsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userProfiles = useAppSelector(state => state.userProfile.entities);
  const messages = useAppSelector(state => state.messages.entities);
  const reactionsEntity = useAppSelector(state => state.reactions.entity);
  const loading = useAppSelector(state => state.reactions.loading);
  const updating = useAppSelector(state => state.reactions.updating);
  const updateSuccess = useAppSelector(state => state.reactions.updateSuccess);

  const handleClose = () => {
    navigate('/reactions');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUserProfiles({}));
    dispatch(getMessages({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...reactionsEntity,
      ...values,
      user: userProfiles.find(it => it.id.toString() === values.user?.toString()),
      messages: messages.find(it => it.id.toString() === values.messages?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...reactionsEntity,
          user: reactionsEntity?.user?.id,
          messages: reactionsEntity?.messages?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="finalApp.reactions.home.createOrEditLabel" data-cy="ReactionsCreateUpdateHeading">
            <Translate contentKey="finalApp.reactions.home.createOrEditLabel">Create or edit a Reactions</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="reactions-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('finalApp.reactions.reaction')}
                id="reactions-reaction"
                name="reaction"
                data-cy="reaction"
                type="text"
              />
              <ValidatedField id="reactions-user" name="user" data-cy="user" label={translate('finalApp.reactions.user')} type="select">
                <option value="" key="0" />
                {userProfiles
                  ? userProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="reactions-messages"
                name="messages"
                data-cy="messages"
                label={translate('finalApp.reactions.messages')}
                type="select"
              >
                <option value="" key="0" />
                {messages
                  ? messages.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/reactions" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ReactionsUpdate;

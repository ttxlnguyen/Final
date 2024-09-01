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
import { IChannels } from 'app/shared/model/channels.model';
import { getEntities as getChannels } from 'app/entities/channels/channels.reducer';
import { IMessages } from 'app/shared/model/messages.model';
import { getEntity, updateEntity, createEntity, reset } from './messages.reducer';

export const MessagesUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userProfiles = useAppSelector(state => state.userProfile.entities);
  const channels = useAppSelector(state => state.channels.entities);
  const messagesEntity = useAppSelector(state => state.messages.entity);
  const loading = useAppSelector(state => state.messages.loading);
  const updating = useAppSelector(state => state.messages.updating);
  const updateSuccess = useAppSelector(state => state.messages.updateSuccess);

  const handleClose = () => {
    navigate('/messages');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUserProfiles({}));
    dispatch(getChannels({}));
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
    values.sentAt = convertDateTimeToServer(values.sentAt);
    values.editedAt = convertDateTimeToServer(values.editedAt);

    const entity = {
      ...messagesEntity,
      ...values,
      userProfile: userProfiles.find(it => it.id.toString() === values.userProfile?.toString()),
      channels: channels.find(it => it.id.toString() === values.channels?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          sentAt: displayDefaultDateTime(),
          editedAt: displayDefaultDateTime(),
        }
      : {
          ...messagesEntity,
          sentAt: convertDateTimeFromServer(messagesEntity.sentAt),
          editedAt: convertDateTimeFromServer(messagesEntity.editedAt),
          userProfile: messagesEntity?.userProfile?.id,
          channels: messagesEntity?.channels?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="finalApp.messages.home.createOrEditLabel" data-cy="MessagesCreateUpdateHeading">
            <Translate contentKey="finalApp.messages.home.createOrEditLabel">Create or edit a Messages</Translate>
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
                  id="messages-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('finalApp.messages.content')}
                id="messages-content"
                name="content"
                data-cy="content"
                type="text"
              />
              <ValidatedField
                label={translate('finalApp.messages.sentAt')}
                id="messages-sentAt"
                name="sentAt"
                data-cy="sentAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('finalApp.messages.editedAt')}
                id="messages-editedAt"
                name="editedAt"
                data-cy="editedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('finalApp.messages.isDeleted')}
                id="messages-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                id="messages-userProfile"
                name="userProfile"
                data-cy="userProfile"
                label={translate('finalApp.messages.userProfile')}
                type="select"
              >
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
                id="messages-channels"
                name="channels"
                data-cy="channels"
                label={translate('finalApp.messages.channels')}
                type="select"
              >
                <option value="" key="0" />
                {channels
                  ? channels.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/messages" replace color="info">
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

export default MessagesUpdate;

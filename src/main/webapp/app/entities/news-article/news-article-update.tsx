import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getArticleImages } from 'app/entities/article-image/article-image.reducer';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { State } from 'app/shared/model/enumerations/state.model';
import { Language } from 'app/shared/model/enumerations/language.model';
import { Location } from 'app/shared/model/enumerations/location.model';
import { createEntity, getEntity, reset, updateEntity } from './news-article.reducer';

export const NewsArticleUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const articleImages = useAppSelector(state => state.articleImage.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const newsArticleEntity = useAppSelector(state => state.newsArticle.entity);
  const loading = useAppSelector(state => state.newsArticle.loading);
  const updating = useAppSelector(state => state.newsArticle.updating);
  const updateSuccess = useAppSelector(state => state.newsArticle.updateSuccess);
  const stateValues = Object.keys(State);
  const languageValues = Object.keys(Language);
  const locationValues = Object.keys(Location);

  const handleClose = () => {
    navigate('/news-article');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getArticleImages({}));
    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.publishedDate = convertDateTimeToServer(values.publishedDate);

    const entity = {
      ...newsArticleEntity,
      ...values,
      articleImage: articleImages.find(it => it.id.toString() === values.articleImage?.toString()),
      user: users.find(it => it.id.toString() === values.user?.toString()),
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
          publishedDate: displayDefaultDateTime(),
        }
      : {
          state: 'PUBLISHED',
          language: 'GERMAN',
          location: 'TEASER',
          ...newsArticleEntity,
          publishedDate: convertDateTimeFromServer(newsArticleEntity.publishedDate),
          articleImage: newsArticleEntity?.articleImage?.id,
          user: newsArticleEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="tiergesundApp.newsArticle.home.createOrEditLabel" data-cy="NewsArticleCreateUpdateHeading">
            <Translate contentKey="tiergesundApp.newsArticle.home.createOrEditLabel">Create or edit a NewsArticle</Translate>
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
                  id="news-article-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('tiergesundApp.newsArticle.title')}
                id="news-article-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('tiergesundApp.newsArticle.content')}
                id="news-article-content"
                name="content"
                data-cy="content"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('tiergesundApp.newsArticle.state')}
                id="news-article-state"
                name="state"
                data-cy="state"
                type="select"
              >
                {stateValues.map(state => (
                  <option value={state} key={state}>
                    {translate(`tiergesundApp.State.${state}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('tiergesundApp.newsArticle.publishedDate')}
                id="news-article-publishedDate"
                name="publishedDate"
                data-cy="publishedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('tiergesundApp.newsArticle.author')}
                id="news-article-author"
                name="author"
                data-cy="author"
                type="text"
              />
              <ValidatedField
                label={translate('tiergesundApp.newsArticle.language')}
                id="news-article-language"
                name="language"
                data-cy="language"
                type="select"
              >
                {languageValues.map(language => (
                  <option value={language} key={language}>
                    {translate(`tiergesundApp.Language.${language}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('tiergesundApp.newsArticle.location')}
                id="news-article-location"
                name="location"
                data-cy="location"
                type="select"
              >
                {locationValues.map(location => (
                  <option value={location} key={location}>
                    {translate(`tiergesundApp.Location.${location}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="news-article-articleImage"
                name="articleImage"
                data-cy="articleImage"
                label={translate('tiergesundApp.newsArticle.articleImage')}
                type="select"
              >
                <option value="" key="0" />
                {articleImages
                  ? articleImages.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="news-article-user"
                name="user"
                data-cy="user"
                label={translate('tiergesundApp.newsArticle.user')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/news-article" replace color="info">
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

export default NewsArticleUpdate;

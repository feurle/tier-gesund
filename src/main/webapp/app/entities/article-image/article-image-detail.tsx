import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, byteSize, openFile } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './article-image.reducer';

export const ArticleImageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const articleImageEntity = useAppSelector(state => state.articleImage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="articleImageDetailsHeading">
          <Translate contentKey="tiergesundApp.articleImage.detail.title">ArticleImage</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{articleImageEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="tiergesundApp.articleImage.title">Title</Translate>
            </span>
          </dt>
          <dd>{articleImageEntity.title}</dd>
          <dt>
            <span id="image">
              <Translate contentKey="tiergesundApp.articleImage.image">Image</Translate>
            </span>
          </dt>
          <dd>
            {articleImageEntity.image ? (
              <div>
                {articleImageEntity.imageContentType ? (
                  <a onClick={openFile(articleImageEntity.imageContentType, articleImageEntity.image)}>
                    <img
                      src={`data:${articleImageEntity.imageContentType};base64,${articleImageEntity.image}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {articleImageEntity.imageContentType}, {byteSize(articleImageEntity.image)}
                </span>
              </div>
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/article-image" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/article-image/${articleImageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ArticleImageDetail;

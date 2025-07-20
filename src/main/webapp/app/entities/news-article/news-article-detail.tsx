import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import DOMPurify from 'dompurify';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './news-article.reducer';

export const NewsArticleDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const newsArticleEntity = useAppSelector(state => state.newsArticle.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="newsArticleDetailsHeading">
          <Translate contentKey="tiergesundApp.newsArticle.detail.title">NewsArticle</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{newsArticleEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="tiergesundApp.newsArticle.title">Title</Translate>
            </span>
          </dt>
          <dd>
            <div dangerouslySetInnerHTML={{ __html: DOMPurify.sanitize(newsArticleEntity.title) }} />
          </dd>
          <dt>
            <span id="content">
              <Translate contentKey="tiergesundApp.newsArticle.content">Content</Translate>
            </span>
          </dt>
          <dd>
            <div dangerouslySetInnerHTML={{ __html: DOMPurify.sanitize(newsArticleEntity.content) }} />
          </dd>
          <dt>
            <span id="state">
              <Translate contentKey="tiergesundApp.newsArticle.state">State</Translate>
            </span>
          </dt>
          <dd>{newsArticleEntity.state}</dd>
          <dt>
            <span id="publishedDate">
              <Translate contentKey="tiergesundApp.newsArticle.publishedDate">Published Date</Translate>
            </span>
          </dt>
          <dd>
            {newsArticleEntity.publishedDate ? (
              <TextFormat value={newsArticleEntity.publishedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="author">
              <Translate contentKey="tiergesundApp.newsArticle.author">Author</Translate>
            </span>
          </dt>
          <dd>{newsArticleEntity.author}</dd>
          <dt>
            <span id="language">
              <Translate contentKey="tiergesundApp.newsArticle.language">Language</Translate>
            </span>
          </dt>
          <dd>{newsArticleEntity.language}</dd>
          <dt>
            <span id="location">
              <Translate contentKey="tiergesundApp.newsArticle.location">Location</Translate>
            </span>
          </dt>
          <dd>{newsArticleEntity.location}</dd>
          <dt>
            <Translate contentKey="tiergesundApp.newsArticle.articleImage">Article Image</Translate>
          </dt>
          <dd>{newsArticleEntity.articleImage ? newsArticleEntity.articleImage.id : ''}</dd>
          <dt>
            <Translate contentKey="tiergesundApp.newsArticle.user">User</Translate>
          </dt>
          <dd>{newsArticleEntity.user ? newsArticleEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/news-article" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/news-article/${newsArticleEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default NewsArticleDetail;

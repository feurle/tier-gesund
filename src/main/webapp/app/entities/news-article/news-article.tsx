import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { TextFormat, Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import DOMPurify from 'dompurify';

import { getEntities } from './news-article.reducer';

export const NewsArticle = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const newsArticleList = useAppSelector(state => state.newsArticle.entities);
  const loading = useAppSelector(state => state.newsArticle.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="news-article-heading" data-cy="NewsArticleHeading">
        <Translate contentKey="tiergesundApp.newsArticle.home.title">News Articles</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="tiergesundApp.newsArticle.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/news-article/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="tiergesundApp.newsArticle.home.createLabel">Create new News Article</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {newsArticleList && newsArticleList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="tiergesundApp.newsArticle.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('title')}>
                  <Translate contentKey="tiergesundApp.newsArticle.title">Title</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('title')} />
                </th>
                <th className="hand" onClick={sort('content')}>
                  <Translate contentKey="tiergesundApp.newsArticle.content">Content</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('content')} />
                </th>
                <th className="hand" onClick={sort('state')}>
                  <Translate contentKey="tiergesundApp.newsArticle.state">State</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('state')} />
                </th>
                <th className="hand" onClick={sort('publishedDate')}>
                  <Translate contentKey="tiergesundApp.newsArticle.publishedDate">Published Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('publishedDate')} />
                </th>
                <th className="hand" onClick={sort('author')}>
                  <Translate contentKey="tiergesundApp.newsArticle.author">Author</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('author')} />
                </th>
                <th className="hand" onClick={sort('language')}>
                  <Translate contentKey="tiergesundApp.newsArticle.language">Language</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('language')} />
                </th>
                <th className="hand" onClick={sort('location')}>
                  <Translate contentKey="tiergesundApp.newsArticle.location">Location</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('location')} />
                </th>
                <th>
                  <Translate contentKey="tiergesundApp.newsArticle.articleImage">Article Image</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="tiergesundApp.newsArticle.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {newsArticleList.map((newsArticle, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/news-article/${newsArticle.id}`} color="link" size="sm">
                      {newsArticle.id}
                    </Button>
                  </td>
                  <td>
                    <span dangerouslySetInnerHTML={{ __html: DOMPurify.sanitize(newsArticle.title) }} />
                  </td>
                  <td>
                    <span dangerouslySetInnerHTML={{ __html: DOMPurify.sanitize(newsArticle.content) }} />
                  </td>
                  <td>
                    <Translate contentKey={`tiergesundApp.State.${newsArticle.state}`} />
                  </td>
                  <td>
                    {newsArticle.publishedDate ? (
                      <TextFormat type="date" value={newsArticle.publishedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{newsArticle.author}</td>
                  <td>
                    <Translate contentKey={`tiergesundApp.Language.${newsArticle.language}`} />
                  </td>
                  <td>
                    <Translate contentKey={`tiergesundApp.Location.${newsArticle.location}`} />
                  </td>
                  <td>
                    {newsArticle.articleImage ? (
                      <Link to={`/article-image/${newsArticle.articleImage.id}`}>{newsArticle.articleImage.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{newsArticle.user ? newsArticle.user.id : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/news-article/${newsArticle.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/news-article/${newsArticle.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/news-article/${newsArticle.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="tiergesundApp.newsArticle.home.notFound">No News Articles found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default NewsArticle;

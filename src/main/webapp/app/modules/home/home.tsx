import './home.scss';

import React from 'react';
import DOMPurify from 'dompurify';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Alert, Col, Row } from 'reactstrap';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getHomeEntities } from 'app/entities/news-article/news-article.reducer';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  const dispatch = useAppDispatch();

  const loading = useAppSelector(state => state.newsArticle.loading);
  const error = useAppSelector(state => state.newsArticle.error);

  const getAllEntities = () => {
    dispatch(getHomeEntities({}));
  };

  React.useEffect(() => {
    // Replace '1' with the ID you want to fetch
    getAllEntities();
  }, []);

  const newsArticleList = useAppSelector(state => state.newsArticle.entities);

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  return (
    <Row>
      <Col md="3" className="pad">
        <span className="hipster rounded" />
      </Col>
      <Col md="9">
        <h1 className="display-4">
          <Translate contentKey="home.title">Welcome, Java Hipster!</Translate>
        </h1>
        <p className="lead">
          <Translate contentKey="home.subtitle">This is your homepage</Translate>
        </p>
        <div>
          <Alert color="secondary">
            <div>
              {newsArticleList
                .filter(newsArticle => newsArticle.location === 'TEASER')
                .map((newsArticle, i) => (
                  <div key={`id-${i}`}>
                    <h1>
                      <span id="title" dangerouslySetInnerHTML={{ __html: DOMPurify.sanitize(newsArticle.title) }} />
                    </h1>
                    <p>
                      <span id="content" dangerouslySetInnerHTML={{ __html: DOMPurify.sanitize(newsArticle.content) }} />
                    </p>
                  </div>
                ))}
            </div>
          </Alert>
        </div>
        <div></div>
        <div>
          {newsArticleList && newsArticleList.length > 0 ? (
            <div>
              {newsArticleList
                .filter(newsArticle => newsArticle.location === 'HOME')
                .map((newsArticle, i) => (
                  <div key={`id-${i}`}>
                    <h1>
                      <span dangerouslySetInnerHTML={{ __html: DOMPurify.sanitize(newsArticle.title) }} />
                    </h1>
                    <p>
                      <span dangerouslySetInnerHTML={{ __html: DOMPurify.sanitize(newsArticle.content) }} />
                    </p>
                  </div>
                ))}
            </div>
          ) : (
            !loading && (
              <div className="alert alert-warning">
                <Translate contentKey="tiergesundApp.newsArticle.home.notFound">No News Articles found</Translate>
              </div>
            )
          )}
        </div>
        <div></div>
      </Col>
    </Row>
  );
};

export default Home;

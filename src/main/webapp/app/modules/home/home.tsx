import './home.scss';

import React from 'react';
import DOMPurify from 'dompurify';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Alert, Col, Row } from 'reactstrap';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from 'app/entities/news-article/news-article.reducer';
import { getEntities } from 'app/entities/news-article/news-article.reducer';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  const dispatch = useAppDispatch();

  const loading = useAppSelector(state => state.newsArticle.loading);
  const error = useAppSelector(state => state.newsArticle.error);

  const getAllEntities = () => {
    dispatch(getEntities({}));
  };

  React.useEffect(() => {
    // Replace '1' with the ID you want to fetch
    dispatch(getEntity(1));
    // getAllEntities();
  }, []);

  const newsArticleEntity = useAppSelector(state => state.newsArticle.entity);
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
            <span id="id">{newsArticleEntity.id}</span>
            <div id="content" dangerouslySetInnerHTML={{ __html: DOMPurify.sanitize(newsArticleEntity.title) }} />
            <div id="content" dangerouslySetInnerHTML={{ __html: DOMPurify.sanitize(newsArticleEntity.content) }} />
          </Alert>
        </div>
        {account?.login ? (
          <div>
            <Alert color="success">
              <Translate contentKey="home.logged.message" interpolate={{ username: account.login }}>
                You are logged in as user {account.login}.
              </Translate>
            </Alert>
          </div>
        ) : (
          <div>
            <Alert color="warning">
              <Translate contentKey="global.messages.info.authenticated.prefix">If you want to </Translate>

              <Link to="/login" className="alert-link">
                <Translate contentKey="global.messages.info.authenticated.link"> sign in</Translate>
              </Link>
              <Translate contentKey="global.messages.info.authenticated.suffix">
                , you can try the default accounts:
                <br />- Administrator (login=&quot;admin&quot; and password=&quot;admin&quot;)
                <br />- User (login=&quot;user&quot; and password=&quot;user&quot;).
              </Translate>
            </Alert>

            <Alert color="warning">
              <Translate contentKey="global.messages.info.register.noaccount">You do not have an account yet?</Translate>&nbsp;
              <Link to="/account/register" className="alert-link">
                <Translate contentKey="global.messages.info.register.link">Register a new account</Translate>
              </Link>
            </Alert>
          </div>
        )}
        <div></div>
        <div>
          {newsArticleList && newsArticleList.length > 0 ? (
            <div>
              <h1>Y</h1>
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

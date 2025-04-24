import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('NewsArticle e2e test', () => {
  const newsArticlePageUrl = '/news-article';
  const newsArticlePageUrlPattern = new RegExp('/news-article(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const newsArticleSample = { title: 'alarmieren imprägnieren instead', content: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=' };

  let newsArticle;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/news-articles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/news-articles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/news-articles/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (newsArticle) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/news-articles/${newsArticle.id}`,
      }).then(() => {
        newsArticle = undefined;
      });
    }
  });

  it('NewsArticles menu should load NewsArticles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('news-article');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('NewsArticle').should('exist');
    cy.url().should('match', newsArticlePageUrlPattern);
  });

  describe('NewsArticle page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(newsArticlePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create NewsArticle page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/news-article/new$'));
        cy.getEntityCreateUpdateHeading('NewsArticle');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', newsArticlePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/news-articles',
          body: newsArticleSample,
        }).then(({ body }) => {
          newsArticle = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/news-articles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [newsArticle],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(newsArticlePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details NewsArticle page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('newsArticle');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', newsArticlePageUrlPattern);
      });

      it('edit button click should load edit NewsArticle page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('NewsArticle');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', newsArticlePageUrlPattern);
      });

      it('edit button click should load edit NewsArticle page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('NewsArticle');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', newsArticlePageUrlPattern);
      });

      it('last delete button click should delete instance of NewsArticle', () => {
        cy.intercept('GET', '/api/news-articles/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('newsArticle').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', newsArticlePageUrlPattern);

        newsArticle = undefined;
      });
    });
  });

  describe('new NewsArticle page', () => {
    beforeEach(() => {
      cy.visit(`${newsArticlePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('NewsArticle');
    });

    it('should create an instance of NewsArticle', () => {
      cy.get(`[data-cy="title"]`).type('fooey');
      cy.get(`[data-cy="title"]`).should('have.value', 'fooey');

      cy.get(`[data-cy="content"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="content"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="state"]`).select('CREATED');

      cy.get(`[data-cy="publishedDate"]`).type('2025-04-20T21:37');
      cy.get(`[data-cy="publishedDate"]`).blur();
      cy.get(`[data-cy="publishedDate"]`).should('have.value', '2025-04-20T21:37');

      cy.get(`[data-cy="author"]`).type('and entlang Physik');
      cy.get(`[data-cy="author"]`).should('have.value', 'and entlang Physik');

      cy.get(`[data-cy="language"]`).select('GERMAN');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        newsArticle = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', newsArticlePageUrlPattern);
    });
  });
});

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

describe('ArticleImage e2e test', () => {
  const articleImagePageUrl = '/article-image';
  const articleImagePageUrlPattern = new RegExp('/article-image(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const articleImageSample = { title: 'meh mehr' };

  let articleImage;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/article-images+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/article-images').as('postEntityRequest');
    cy.intercept('DELETE', '/api/article-images/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (articleImage) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/article-images/${articleImage.id}`,
      }).then(() => {
        articleImage = undefined;
      });
    }
  });

  it('ArticleImages menu should load ArticleImages page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('article-image');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ArticleImage').should('exist');
    cy.url().should('match', articleImagePageUrlPattern);
  });

  describe('ArticleImage page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(articleImagePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ArticleImage page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/article-image/new$'));
        cy.getEntityCreateUpdateHeading('ArticleImage');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', articleImagePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/article-images',
          body: articleImageSample,
        }).then(({ body }) => {
          articleImage = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/article-images+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [articleImage],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(articleImagePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ArticleImage page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('articleImage');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', articleImagePageUrlPattern);
      });

      it('edit button click should load edit ArticleImage page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ArticleImage');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', articleImagePageUrlPattern);
      });

      it('edit button click should load edit ArticleImage page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ArticleImage');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', articleImagePageUrlPattern);
      });

      it('last delete button click should delete instance of ArticleImage', () => {
        cy.intercept('GET', '/api/article-images/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('articleImage').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', articleImagePageUrlPattern);

        articleImage = undefined;
      });
    });
  });

  describe('new ArticleImage page', () => {
    beforeEach(() => {
      cy.visit(`${articleImagePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ArticleImage');
    });

    it('should create an instance of ArticleImage', () => {
      cy.get(`[data-cy="title"]`).type('instead');
      cy.get(`[data-cy="title"]`).should('have.value', 'instead');

      cy.setFieldImageAsBytesOfEntity('image', 'integration-test.png', 'image/png');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        articleImage = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', articleImagePageUrlPattern);
    });
  });
});

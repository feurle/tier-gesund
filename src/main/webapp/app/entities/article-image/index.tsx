import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ArticleImage from './article-image';
import ArticleImageDetail from './article-image-detail';
import ArticleImageUpdate from './article-image-update';
import ArticleImageDeleteDialog from './article-image-delete-dialog';

const ArticleImageRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ArticleImage />} />
    <Route path="new" element={<ArticleImageUpdate />} />
    <Route path=":id">
      <Route index element={<ArticleImageDetail />} />
      <Route path="edit" element={<ArticleImageUpdate />} />
      <Route path="delete" element={<ArticleImageDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ArticleImageRoutes;

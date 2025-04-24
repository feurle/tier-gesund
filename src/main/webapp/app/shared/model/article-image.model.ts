export interface IArticleImage {
  id?: number;
  title?: string;
  imageContentType?: string | null;
  image?: string | null;
}

export const defaultValue: Readonly<IArticleImage> = {};

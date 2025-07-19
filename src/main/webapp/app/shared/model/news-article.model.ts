import dayjs from 'dayjs';
import { IArticleImage } from 'app/shared/model/article-image.model';
import { IUser } from 'app/shared/model/user.model';
import { State } from 'app/shared/model/enumerations/state.model';
import { Language } from 'app/shared/model/enumerations/language.model';
import { Location } from 'app/shared/model/enumerations/location.model';

export interface INewsArticle {
  id?: number;
  title?: string;
  content?: string;
  state?: keyof typeof State | null;
  publishedDate?: dayjs.Dayjs | null;
  author?: string | null;
  language?: keyof typeof Language | null;
  location?: keyof typeof Location | null;
  articleImage?: IArticleImage | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<INewsArticle> = {};

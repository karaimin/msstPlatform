import { Moment } from 'moment';
import { ISubtitle } from 'app/shared/model/subtitle.model';

export interface IMovieInfo {
  id?: string;
  name?: string;
  duration?: Moment;
  description?: string;
  translatedSubtitles?: ISubtitle[];
  pendingTranslates?: ISubtitle[];
}

export const defaultValue: Readonly<IMovieInfo> = {};

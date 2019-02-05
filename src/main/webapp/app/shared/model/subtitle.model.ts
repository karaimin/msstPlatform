import { IMovie } from 'app/shared/model/movie.model';
import { ISubtitle } from 'app/shared/model/subtitle.model';
import { ISubtitleLine } from 'app/shared/model/subtitle-line.model';

export interface ISubtitle {
  id?: string;
  version?: string;
  language?: string;
  movie?: IMovie;
  subtitle?: ISubtitle;
  sources?: ISubtitle[];
  lines?: ISubtitleLine[];
}

export const defaultValue: Readonly<ISubtitle> = {};

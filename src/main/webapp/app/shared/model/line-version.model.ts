import { ISubtitleLine } from 'app/shared/model/subtitle-line.model';
import { ILineVersionRating } from 'app/shared/model/line-version-rating.model';

export interface ILineVersion {
  id?: string;
  version?: string;
  text?: string;
  subtitleLine?: ISubtitleLine;
  ratings?: ILineVersionRating[];
}

export const defaultValue: Readonly<ILineVersion> = {};

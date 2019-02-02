import { ISubtitleLine } from 'app/shared/model/subtitle-line.model';
import { ILineVersionRating } from 'app/shared/model/line-version-rating.model';

export interface ILineVersion {
  id?: string;
  version?: string;
  text?: string;
  subtitleLines?: ISubtitleLine[];
  lineVersionRating?: ILineVersionRating;
}

export const defaultValue: Readonly<ILineVersion> = {};

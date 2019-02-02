import { ILineVersion } from 'app/shared/model/line-version.model';

export interface ILineVersionRating {
  id?: string;
  rating?: number;
  comment?: string;
  lineVersion?: ILineVersion;
}

export const defaultValue: Readonly<ILineVersionRating> = {};

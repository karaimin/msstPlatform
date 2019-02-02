import { Moment } from 'moment';
import { ISubtitle } from 'app/shared/model/subtitle.model';

export interface IMovie {
  id?: string;
  name?: string;
  duration?: Moment;
  description?: string;
  subtitle?: ISubtitle;
}

export const defaultValue: Readonly<IMovie> = {};

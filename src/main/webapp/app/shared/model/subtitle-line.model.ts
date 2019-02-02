import { Moment } from 'moment';
import { ISubtitle } from 'app/shared/model/subtitle.model';
import { ILineVersion } from 'app/shared/model/line-version.model';

export interface ISubtitleLine {
  id?: string;
  startTime?: Moment;
  endTime?: Moment;
  subtitles?: ISubtitle[];
  lineVersion?: ILineVersion;
}

export const defaultValue: Readonly<ISubtitleLine> = {};

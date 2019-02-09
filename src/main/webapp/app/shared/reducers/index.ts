import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import movie, {
    MovieState
} from 'app/entities/movie/movie.reducer';
// prettier-ignore
import subtitle, {
    SubtitleState
} from 'app/entities/subtitle/subtitle.reducer';
// prettier-ignore
import subtitleLine, {
    SubtitleLineState
} from 'app/entities/subtitle-line/subtitle-line.reducer';
// prettier-ignore
import lineVersion, {
    LineVersionState
} from 'app/entities/line-version/line-version.reducer';
// prettier-ignore
import lineVersionRating, {
    LineVersionRatingState
} from 'app/entities/line-version-rating/line-version-rating.reducer';
// prettier-ignore
import movieInfo, {
    MovieInfoState
} from 'app/entities/info/movie-info.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly movie: MovieState;
  readonly movieInfo: MovieInfoState;
  readonly subtitle: SubtitleState;
  readonly subtitleLine: SubtitleLineState;
  readonly lineVersion: LineVersionState;
  readonly lineVersionRating: LineVersionRatingState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  movie,
  movieInfo,
  subtitle,
  subtitleLine,
  lineVersion,
  lineVersionRating,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar
});

export default rootReducer;

import axios from 'axios';
import { ICrudGetAction } from 'react-jhipster';

import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ITranslateInfo, defaultValue } from 'app/shared/model/translate-info.model';

export const ACTION_TYPES = {
  FETCH_TRANSLATE_PARENT: 'subtitle/translate/FETCH_SUBTITLELINE_LIST',
  RESET: 'subtitle/translate/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ITranslateInfo>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type TranslateInfoState = Readonly<typeof initialState>;

// Reducer

export default (state: TranslateInfoState = initialState, action): TranslateInfoState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_TRANSLATE_PARENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case FAILURE(ACTION_TYPES.FETCH_TRANSLATE_PARENT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_TRANSLATE_PARENT):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/subtitles/translate';

// Actions

export const getLinesOrigin: ICrudGetAction<ITranslateInfo> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TRANSLATE_PARENT,
    payload: axios.get<ITranslateInfo>(requestUrl)
  };
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

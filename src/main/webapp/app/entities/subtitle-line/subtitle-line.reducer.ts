import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISubtitleLine, defaultValue } from 'app/shared/model/subtitle-line.model';

export const ACTION_TYPES = {
  FETCH_SUBTITLELINE_LIST: 'subtitleLine/FETCH_SUBTITLELINE_LIST',
  FETCH_SUBTITLELINE: 'subtitleLine/FETCH_SUBTITLELINE',
  CREATE_SUBTITLELINE: 'subtitleLine/CREATE_SUBTITLELINE',
  UPDATE_SUBTITLELINE: 'subtitleLine/UPDATE_SUBTITLELINE',
  DELETE_SUBTITLELINE: 'subtitleLine/DELETE_SUBTITLELINE',
  RESET: 'subtitleLine/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISubtitleLine>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type SubtitleLineState = Readonly<typeof initialState>;

// Reducer

export default (state: SubtitleLineState = initialState, action): SubtitleLineState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SUBTITLELINE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SUBTITLELINE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SUBTITLELINE):
    case REQUEST(ACTION_TYPES.UPDATE_SUBTITLELINE):
    case REQUEST(ACTION_TYPES.DELETE_SUBTITLELINE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SUBTITLELINE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SUBTITLELINE):
    case FAILURE(ACTION_TYPES.CREATE_SUBTITLELINE):
    case FAILURE(ACTION_TYPES.UPDATE_SUBTITLELINE):
    case FAILURE(ACTION_TYPES.DELETE_SUBTITLELINE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBTITLELINE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBTITLELINE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SUBTITLELINE):
    case SUCCESS(ACTION_TYPES.UPDATE_SUBTITLELINE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SUBTITLELINE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/subtitle-lines';

// Actions

export const getEntities: ICrudGetAllAction<ISubtitleLine> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SUBTITLELINE_LIST,
  payload: axios.get<ISubtitleLine>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ISubtitleLine> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SUBTITLELINE,
    payload: axios.get<ISubtitleLine>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ISubtitleLine> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SUBTITLELINE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISubtitleLine> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SUBTITLELINE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISubtitleLine> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SUBTITLELINE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

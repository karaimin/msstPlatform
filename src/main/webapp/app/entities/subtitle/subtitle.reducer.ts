import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISubtitle, defaultValue } from 'app/shared/model/subtitle.model';

export const ACTION_TYPES = {
  FETCH_SUBTITLE_LIST: 'subtitle/FETCH_SUBTITLE_LIST',
  FETCH_SUBTITLE: 'subtitle/FETCH_SUBTITLE',
  CREATE_SUBTITLE: 'subtitle/CREATE_SUBTITLE',
  UPDATE_SUBTITLE: 'subtitle/UPDATE_SUBTITLE',
  DELETE_SUBTITLE: 'subtitle/DELETE_SUBTITLE',
  RESET: 'subtitle/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISubtitle>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type SubtitleState = Readonly<typeof initialState>;

// Reducer

export default (state: SubtitleState = initialState, action): SubtitleState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SUBTITLE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SUBTITLE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SUBTITLE):
    case REQUEST(ACTION_TYPES.UPDATE_SUBTITLE):
    case REQUEST(ACTION_TYPES.DELETE_SUBTITLE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SUBTITLE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SUBTITLE):
    case FAILURE(ACTION_TYPES.CREATE_SUBTITLE):
    case FAILURE(ACTION_TYPES.UPDATE_SUBTITLE):
    case FAILURE(ACTION_TYPES.DELETE_SUBTITLE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBTITLE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBTITLE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SUBTITLE):
    case SUCCESS(ACTION_TYPES.UPDATE_SUBTITLE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SUBTITLE):
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

const apiUrl = 'api/subtitles';

// Actions

export const getEntities: ICrudGetAllAction<ISubtitle> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SUBTITLE_LIST,
  payload: axios.get<ISubtitle>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ISubtitle> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SUBTITLE,
    payload: axios.get<ISubtitle>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ISubtitle> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SUBTITLE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISubtitle> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SUBTITLE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISubtitle> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SUBTITLE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

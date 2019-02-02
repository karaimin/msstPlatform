import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ILineVersionRating, defaultValue } from 'app/shared/model/line-version-rating.model';

export const ACTION_TYPES = {
  FETCH_LINEVERSIONRATING_LIST: 'lineVersionRating/FETCH_LINEVERSIONRATING_LIST',
  FETCH_LINEVERSIONRATING: 'lineVersionRating/FETCH_LINEVERSIONRATING',
  CREATE_LINEVERSIONRATING: 'lineVersionRating/CREATE_LINEVERSIONRATING',
  UPDATE_LINEVERSIONRATING: 'lineVersionRating/UPDATE_LINEVERSIONRATING',
  DELETE_LINEVERSIONRATING: 'lineVersionRating/DELETE_LINEVERSIONRATING',
  RESET: 'lineVersionRating/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ILineVersionRating>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type LineVersionRatingState = Readonly<typeof initialState>;

// Reducer

export default (state: LineVersionRatingState = initialState, action): LineVersionRatingState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_LINEVERSIONRATING_LIST):
    case REQUEST(ACTION_TYPES.FETCH_LINEVERSIONRATING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_LINEVERSIONRATING):
    case REQUEST(ACTION_TYPES.UPDATE_LINEVERSIONRATING):
    case REQUEST(ACTION_TYPES.DELETE_LINEVERSIONRATING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_LINEVERSIONRATING_LIST):
    case FAILURE(ACTION_TYPES.FETCH_LINEVERSIONRATING):
    case FAILURE(ACTION_TYPES.CREATE_LINEVERSIONRATING):
    case FAILURE(ACTION_TYPES.UPDATE_LINEVERSIONRATING):
    case FAILURE(ACTION_TYPES.DELETE_LINEVERSIONRATING):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_LINEVERSIONRATING_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_LINEVERSIONRATING):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_LINEVERSIONRATING):
    case SUCCESS(ACTION_TYPES.UPDATE_LINEVERSIONRATING):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_LINEVERSIONRATING):
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

const apiUrl = 'api/line-version-ratings';

// Actions

export const getEntities: ICrudGetAllAction<ILineVersionRating> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_LINEVERSIONRATING_LIST,
  payload: axios.get<ILineVersionRating>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ILineVersionRating> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_LINEVERSIONRATING,
    payload: axios.get<ILineVersionRating>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ILineVersionRating> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_LINEVERSIONRATING,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ILineVersionRating> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_LINEVERSIONRATING,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ILineVersionRating> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_LINEVERSIONRATING,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ILineVersion, defaultValue } from 'app/shared/model/line-version.model';

export const ACTION_TYPES = {
  FETCH_LINEVERSION_LIST: 'lineVersion/FETCH_LINEVERSION_LIST',
  FETCH_LINEVERSION: 'lineVersion/FETCH_LINEVERSION',
  CREATE_LINEVERSION: 'lineVersion/CREATE_LINEVERSION',
  UPDATE_LINEVERSION: 'lineVersion/UPDATE_LINEVERSION',
  DELETE_LINEVERSION: 'lineVersion/DELETE_LINEVERSION',
  RESET: 'lineVersion/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ILineVersion>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type LineVersionState = Readonly<typeof initialState>;

// Reducer

export default (state: LineVersionState = initialState, action): LineVersionState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_LINEVERSION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_LINEVERSION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_LINEVERSION):
    case REQUEST(ACTION_TYPES.UPDATE_LINEVERSION):
    case REQUEST(ACTION_TYPES.DELETE_LINEVERSION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_LINEVERSION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_LINEVERSION):
    case FAILURE(ACTION_TYPES.CREATE_LINEVERSION):
    case FAILURE(ACTION_TYPES.UPDATE_LINEVERSION):
    case FAILURE(ACTION_TYPES.DELETE_LINEVERSION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_LINEVERSION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_LINEVERSION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_LINEVERSION):
    case SUCCESS(ACTION_TYPES.UPDATE_LINEVERSION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_LINEVERSION):
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

const apiUrl = 'api/line-versions';

// Actions

export const getEntities: ICrudGetAllAction<ILineVersion> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_LINEVERSION_LIST,
  payload: axios.get<ILineVersion>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ILineVersion> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_LINEVERSION,
    payload: axios.get<ILineVersion>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ILineVersion> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_LINEVERSION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ILineVersion> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_LINEVERSION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ILineVersion> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_LINEVERSION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IMovieInfo, defaultValue } from 'app/shared/model/movie-Info.model';

export const ACTION_TYPES = {
  FETCH_MOVIE_LIST: 'movie/FETCH_MOVIE_LIST',
  FETCH_MOVIE: 'movie/FETCH_MOVIE',
  CREATE_MOVIE: 'movie/CREATE_MOVIE',
  UPDATE_MOVIE: 'movie/UPDATE_MOVIE',
  DELETE_MOVIE: 'movie/DELETE_MOVIE',
  RESET: 'movie/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IMovieInfo>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type MovieInfoState = Readonly<typeof initialState>;

// Reducer

export default (state: MovieInfoState = initialState, action): MovieInfoState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_MOVIE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MOVIE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_MOVIE):
    case REQUEST(ACTION_TYPES.UPDATE_MOVIE):
    case REQUEST(ACTION_TYPES.DELETE_MOVIE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_MOVIE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MOVIE):
    case FAILURE(ACTION_TYPES.CREATE_MOVIE):
    case FAILURE(ACTION_TYPES.UPDATE_MOVIE):
    case FAILURE(ACTION_TYPES.DELETE_MOVIE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_MOVIE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_MOVIE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_MOVIE):
    case SUCCESS(ACTION_TYPES.UPDATE_MOVIE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_MOVIE):
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

const apiUrl = 'api/movies/info';

// Actions

export const getMovieInfo: ICrudGetAction<IMovieInfo> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MOVIE,
    payload: axios.get<IMovieInfo>(requestUrl)
  };
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

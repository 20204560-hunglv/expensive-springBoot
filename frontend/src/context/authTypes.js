// Auth action types and constants
export const AUTH_ACTIONS = {
  LOGIN_SUCCESS: "LOGIN_SUCCESS",
  LOGOUT: "LOGOUT",
  SET_LOADING: "SET_LOADING",
  UPDATE_USER: "UPDATE_USER",
};

// Initial state
export const initialAuthState = {
  user: null,
  token: null,
  isAuthenticated: false,
  isLoading: true,
};

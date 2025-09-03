/**
 * API Configuration Constants
 * Chứa các constants liên quan đến API và HTTP requests
 */

// API Base URL
export const API_BASE_URL =
  import.meta.env.VITE_API_URL || "http://localhost:8080/api";

// API Endpoints
export const API_ENDPOINTS = {
  AUTH: {
    LOGIN: "/auth/login",
    REGISTER: "/auth/register",
    LOGOUT: "/auth/logout",
    REFRESH: "/auth/refresh",
    PROFILE: "/auth/profile",
  },
  EXPENSES: {
    BASE: "/expenses",
    BY_ID: (id) => `/expenses/${id}`,
    BY_CATEGORY: (categoryId) => `/expenses/category/${categoryId}`,
    SUMMARY: "/expenses/summary",
  },
  CATEGORIES: {
    BASE: "/categories",
    BY_ID: (id) => `/categories/${id}`,
  },
  USER: {
    PROFILE: "/user/profile",
    SETTINGS: "/user/settings",
  },
};

// HTTP Status Codes
export const HTTP_STATUS = {
  OK: 200,
  CREATED: 201,
  NO_CONTENT: 204,
  BAD_REQUEST: 400,
  UNAUTHORIZED: 401,
  FORBIDDEN: 403,
  NOT_FOUND: 404,
  CONFLICT: 409,
  INTERNAL_SERVER_ERROR: 500,
  BAD_GATEWAY: 502,
  SERVICE_UNAVAILABLE: 503,
};

// Request Timeout
export const REQUEST_TIMEOUT = 10000; // 10 seconds

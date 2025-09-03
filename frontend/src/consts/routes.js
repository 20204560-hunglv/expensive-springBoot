/**
 * Route Constants
 * Chứa các constants liên quan đến routing và navigation
 */

// Public Routes
export const PUBLIC_ROUTES = {
  HOME: "/",
  LOGIN: "/login",
  REGISTER: "/register",
  FORGOT_PASSWORD: "/forgot-password",
  RESET_PASSWORD: "/reset-password",
  NOT_FOUND: "/404",
};

// Protected Routes
export const PROTECTED_ROUTES = {
  DASHBOARD: "/dashboard",
  HOME: "/home",
  HISTORY: "/history",
  PROFILE: "/profile",
  SETTINGS: "/settings",
  EXPENSES: {
    LIST: "/expenses",
    CREATE: "/expenses/create",
    EDIT: "/expenses/edit/:id",
    VIEW: "/expenses/:id",
  },
  CATEGORIES: {
    LIST: "/categories",
    CREATE: "/categories/create",
    EDIT: "/categories/edit/:id",
  },
};

// Route Titles
export const ROUTE_TITLES = {
  [PUBLIC_ROUTES.HOME]: "Trang chủ",
  [PUBLIC_ROUTES.LOGIN]: "Đăng nhập",
  [PUBLIC_ROUTES.REGISTER]: "Đăng ký",
  [PUBLIC_ROUTES.FORGOT_PASSWORD]: "Quên mật khẩu",
  [PUBLIC_ROUTES.RESET_PASSWORD]: "Đặt lại mật khẩu",
  [PROTECTED_ROUTES.DASHBOARD]: "Bảng điều khiển",
  [PROTECTED_ROUTES.HOME]: "Trang chủ",
  [PROTECTED_ROUTES.HISTORY]: "Lịch sử chi tiêu",
  [PROTECTED_ROUTES.PROFILE]: "Hồ sơ cá nhân",
  [PROTECTED_ROUTES.SETTINGS]: "Cài đặt",
  [PROTECTED_ROUTES.EXPENSES.LIST]: "Danh sách chi tiêu",
  [PROTECTED_ROUTES.EXPENSES.CREATE]: "Thêm chi tiêu",
  [PROTECTED_ROUTES.CATEGORIES.LIST]: "Danh mục",
};

// Navigation Menu Items
export const NAVIGATION_ITEMS = [
  {
    label: "Trang chủ",
    path: PROTECTED_ROUTES.HOME,
    icon: "🏠",
    exact: true,
  },
  {
    label: "Lịch sử",
    path: PROTECTED_ROUTES.HISTORY,
    icon: "📊",
    exact: true,
  },
  {
    label: "Hồ sơ",
    path: PROTECTED_ROUTES.PROFILE,
    icon: "👤",
    exact: true,
  },
];

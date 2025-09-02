// API Base URL
export const API_BASE_URL =
  import.meta.env.VITE_API_URL || "http://localhost:8080/api";

// Local Storage Keys
export const STORAGE_KEYS = {
  TOKEN: "token",
  USER: "user",
  EXPENSES: "expenses",
  THEME: "theme",
};

// Date Formats
export const DATE_FORMATS = {
  DISPLAY: "dd/MM/yyyy",
  API: "yyyy-MM-dd",
  DATETIME: "dd/MM/yyyy HH:mm",
};

// Expense Categories (default)
export const DEFAULT_CATEGORIES = [
  { id: 1, name: "Ăn uống", color: "#ff6b6b", icon: "🍔" },
  { id: 2, name: "Di chuyển", color: "#4ecdc4", icon: "🚗" },
  { id: 3, name: "Mua sắm", color: "#45b7d1", icon: "🛍️" },
  { id: 4, name: "Giải trí", color: "#f9ca24", icon: "🎮" },
  { id: 5, name: "Y tế", color: "#6c5ce7", icon: "🏥" },
  { id: 6, name: "Giáo dục", color: "#fd79a8", icon: "📚" },
  { id: 7, name: "Gia đình", color: "#00b894", icon: "👨‍👩‍👧‍👦" },
  { id: 8, name: "Khác", color: "#a0a0a0", icon: "📝" },
];

// Validation Rules
export const VALIDATION = {
  PASSWORD_MIN_LENGTH: 6,
  USERNAME_MIN_LENGTH: 3,
  MAX_EXPENSE_AMOUNT: 999999999,
  MIN_EXPENSE_AMOUNT: 1000,
};

// Pagination
export const PAGINATION = {
  DEFAULT_PAGE_SIZE: 20,
  MAX_PAGE_SIZE: 100,
};

// Currency
export const CURRENCY = {
  CODE: "VND",
  SYMBOL: "₫",
  LOCALE: "vi-VN",
};

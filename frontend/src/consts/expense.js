/**
 * Expense Related Constants
 * Chứa các constants liên quan đến expense và categories
 */

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

// Expense Amount Limits
export const EXPENSE_LIMITS = {
  MIN_AMOUNT: 1000, // 1,000 VND
  MAX_AMOUNT: 999999999, // 999,999,999 VND
  DEFAULT_AMOUNT: 0,
};

// Expense Sort Options
export const EXPENSE_SORT = {
  DATE_DESC: "date_desc",
  DATE_ASC: "date_asc",
  AMOUNT_DESC: "amount_desc",
  AMOUNT_ASC: "amount_asc",
  CATEGORY: "category",
  NAME: "name",
};

// Expense Filter Types
export const EXPENSE_FILTERS = {
  ALL: "all",
  TODAY: "today",
  THIS_WEEK: "this_week",
  THIS_MONTH: "this_month",
  THIS_YEAR: "this_year",
  CUSTOM: "custom",
};

// Expense Status
export const EXPENSE_STATUS = {
  ACTIVE: "active",
  DELETED: "deleted",
  ARCHIVED: "archived",
};

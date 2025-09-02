// Expense action types and constants
export const EXPENSE_ACTIONS = {
  SET_EXPENSES: "SET_EXPENSES",
  ADD_EXPENSE: "ADD_EXPENSE",
  UPDATE_EXPENSE: "UPDATE_EXPENSE",
  DELETE_EXPENSE: "DELETE_EXPENSE",
  SET_LOADING: "SET_LOADING",
  SET_FILTERS: "SET_FILTERS",
  CALCULATE_TOTAL: "CALCULATE_TOTAL",
};

// Initial state
export const initialExpenseState = {
  expenses: [],
  categories: [
    { id: 1, name: "Ăn uống", color: "#ff6b6b", icon: "🍔" },
    { id: 2, name: "Di chuyển", color: "#4ecdc4", icon: "🚗" },
    { id: 3, name: "Mua sắm", color: "#45b7d1", icon: "🛍️" },
    { id: 4, name: "Giải trí", color: "#f9ca24", icon: "🎮" },
    { id: 5, name: "Y tế", color: "#6c5ce7", icon: "🏥" },
    { id: 6, name: "Khác", color: "#a0a0a0", icon: "📝" },
  ],
  totalExpense: 0,
  isLoading: false,
  filters: {
    category: null,
    dateRange: null,
    sortBy: "date",
    sortOrder: "desc",
  },
};

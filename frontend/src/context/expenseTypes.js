import { DEFAULT_CATEGORIES, EXPENSE_SORT } from "../consts/expense.js";

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
  categories: DEFAULT_CATEGORIES,
  totalExpense: 0,
  isLoading: false,
  filters: {
    category: null,
    dateRange: null,
    sortBy: EXPENSE_SORT.DATE_DESC.split('_')[0], // "date"
    sortOrder: EXPENSE_SORT.DATE_DESC.split('_')[1], // "desc"
  },
};

import { createContext, useReducer, useEffect } from "react";
import { EXPENSE_ACTIONS, initialExpenseState } from "./expenseTypes";

// Reducer
const expenseReducer = (state, action) => {
  switch (action.type) {
    case EXPENSE_ACTIONS.SET_EXPENSES:
      return {
        ...state,
        expenses: action.payload,
        isLoading: false,
      };
    case EXPENSE_ACTIONS.ADD_EXPENSE:
      return {
        ...state,
        expenses: [action.payload, ...state.expenses],
      };
    case EXPENSE_ACTIONS.UPDATE_EXPENSE:
      return {
        ...state,
        expenses: state.expenses.map((expense) =>
          expense.id === action.payload.id ? action.payload : expense
        ),
      };
    case EXPENSE_ACTIONS.DELETE_EXPENSE:
      return {
        ...state,
        expenses: state.expenses.filter(
          (expense) => expense.id !== action.payload
        ),
      };
    case EXPENSE_ACTIONS.SET_LOADING:
      return {
        ...state,
        isLoading: action.payload,
      };
    case EXPENSE_ACTIONS.SET_FILTERS:
      return {
        ...state,
        filters: { ...state.filters, ...action.payload },
      };
    case EXPENSE_ACTIONS.CALCULATE_TOTAL: {
      const total = state.expenses.reduce(
        (sum, expense) => sum + expense.amount,
        0
      );
      return {
        ...state,
        totalExpense: total,
      };
    }
    default:
      return state;
  }
};

// Context
const ExpenseContext = createContext();

// Provider component
export const ExpenseProvider = ({ children }) => {
  const [state, dispatch] = useReducer(expenseReducer, initialExpenseState);

  // Load expenses from localStorage on app start
  useEffect(() => {
    const loadExpenses = () => {
      try {
        const savedExpenses = localStorage.getItem("expenses");
        if (savedExpenses) {
          const expenses = JSON.parse(savedExpenses);
          dispatch({ type: EXPENSE_ACTIONS.SET_EXPENSES, payload: expenses });
          // Calculate total after loading
          setTimeout(() => {
            dispatch({ type: EXPENSE_ACTIONS.CALCULATE_TOTAL });
          }, 0);
        } else {
          dispatch({ type: EXPENSE_ACTIONS.SET_LOADING, payload: false });
        }
      } catch (error) {
        console.error("Error loading expenses from localStorage:", error);
        dispatch({ type: EXPENSE_ACTIONS.SET_LOADING, payload: false });
      }
    };

    loadExpenses();
  }, []);

  // Save expenses to localStorage whenever expenses change
  useEffect(() => {
    if (state.expenses.length > 0) {
      localStorage.setItem("expenses", JSON.stringify(state.expenses));
      dispatch({ type: EXPENSE_ACTIONS.CALCULATE_TOTAL });
    }
  }, [state.expenses]);

  // Add expense function
  const addExpense = (expenseData) => {
    const newExpense = {
      id: Date.now().toString(),
      ...expenseData,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
    };

    dispatch({ type: EXPENSE_ACTIONS.ADD_EXPENSE, payload: newExpense });
    return newExpense;
  };

  // Update expense function
  const updateExpense = (id, expenseData) => {
    const updatedExpense = {
      ...expenseData,
      id,
      updatedAt: new Date().toISOString(),
    };

    dispatch({ type: EXPENSE_ACTIONS.UPDATE_EXPENSE, payload: updatedExpense });
    return updatedExpense;
  };

  // Delete expense function
  const deleteExpense = (id) => {
    dispatch({ type: EXPENSE_ACTIONS.DELETE_EXPENSE, payload: id });
  };

  // Set filters function
  const setFilters = (newFilters) => {
    dispatch({ type: EXPENSE_ACTIONS.SET_FILTERS, payload: newFilters });
  };

  // Get filtered expenses
  const getFilteredExpenses = () => {
    let filtered = [...state.expenses];

    // Filter by category
    if (state.filters.category) {
      filtered = filtered.filter(
        (expense) => expense.categoryId === state.filters.category
      );
    }

    // Filter by date range
    if (state.filters.dateRange) {
      const { startDate, endDate } = state.filters.dateRange;
      filtered = filtered.filter((expense) => {
        const expenseDate = new Date(expense.date);
        return expenseDate >= startDate && expenseDate <= endDate;
      });
    }

    // Sort expenses
    filtered.sort((a, b) => {
      const order = state.filters.sortOrder === "asc" ? 1 : -1;

      switch (state.filters.sortBy) {
        case "amount":
          return (a.amount - b.amount) * order;
        case "category":
          return a.categoryId.localeCompare(b.categoryId) * order;
        case "date":
        default:
          return (new Date(a.date) - new Date(b.date)) * order;
      }
    });

    return filtered;
  };

  // Get category by id
  const getCategoryById = (id) => {
    return state.categories.find((category) => category.id === id);
  };

  const value = {
    ...state,
    addExpense,
    updateExpense,
    deleteExpense,
    setFilters,
    getFilteredExpenses,
    getCategoryById,
  };

  return (
    <ExpenseContext.Provider value={value}>{children}</ExpenseContext.Provider>
  );
};

export default ExpenseContext;

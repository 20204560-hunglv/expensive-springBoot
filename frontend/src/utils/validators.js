import { VALIDATION } from "./constants";

/**
 * Validate email format
 * @param {string} email - Email to validate
 * @returns {boolean} True if email is valid
 */
export const isValidEmail = (email) => {
  if (!email || typeof email !== "string") return false;

  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email.trim());
};

/**
 * Validate password strength
 * @param {string} password - Password to validate
 * @returns {object} Validation result with isValid and message
 */
export const validatePassword = (password) => {
  if (!password || typeof password !== "string") {
    return {
      isValid: false,
      message: "Mật khẩu không được để trống",
    };
  }

  if (password.length < VALIDATION.PASSWORD_MIN_LENGTH) {
    return {
      isValid: false,
      message: `Mật khẩu phải có ít nhất ${VALIDATION.PASSWORD_MIN_LENGTH} ký tự`,
    };
  }

  // Check for at least one number and one letter
  const hasNumber = /\d/.test(password);
  const hasLetter = /[a-zA-Z]/.test(password);

  if (!hasNumber || !hasLetter) {
    return {
      isValid: false,
      message: "Mật khẩu phải chứa ít nhất một chữ cái và một số",
    };
  }

  return {
    isValid: true,
    message: "Mật khẩu hợp lệ",
  };
};

/**
 * Validate username
 * @param {string} username - Username to validate
 * @returns {object} Validation result with isValid and message
 */
export const validateUsername = (username) => {
  if (!username || typeof username !== "string") {
    return {
      isValid: false,
      message: "Tên đăng nhập không được để trống",
    };
  }

  const trimmed = username.trim();

  if (trimmed.length < VALIDATION.USERNAME_MIN_LENGTH) {
    return {
      isValid: false,
      message: `Tên đăng nhập phải có ít nhất ${VALIDATION.USERNAME_MIN_LENGTH} ký tự`,
    };
  }

  // Only allow alphanumeric characters and underscores
  const validChars = /^[a-zA-Z0-9_]+$/;
  if (!validChars.test(trimmed)) {
    return {
      isValid: false,
      message: "Tên đăng nhập chỉ được chứa chữ cái, số và dấu gạch dưới",
    };
  }

  return {
    isValid: true,
    message: "Tên đăng nhập hợp lệ",
  };
};

/**
 * Validate expense amount
 * @param {number|string} amount - Amount to validate
 * @returns {object} Validation result with isValid and message
 */
export const validateExpenseAmount = (amount) => {
  const numAmount = typeof amount === "string" ? parseFloat(amount) : amount;

  if (isNaN(numAmount) || numAmount <= 0) {
    return {
      isValid: false,
      message: "Số tiền phải là một số dương",
    };
  }

  if (numAmount < VALIDATION.MIN_EXPENSE_AMOUNT) {
    return {
      isValid: false,
      message: `Số tiền tối thiểu là ${VALIDATION.MIN_EXPENSE_AMOUNT.toLocaleString("vi-VN")} ₫`,
    };
  }

  if (numAmount > VALIDATION.MAX_EXPENSE_AMOUNT) {
    return {
      isValid: false,
      message: `Số tiền tối đa là ${VALIDATION.MAX_EXPENSE_AMOUNT.toLocaleString("vi-VN")} ₫`,
    };
  }

  return {
    isValid: true,
    message: "Số tiền hợp lệ",
  };
};

/**
 * Validate required field
 * @param {any} value - Value to validate
 * @param {string} fieldName - Name of the field for error message
 * @returns {object} Validation result with isValid and message
 */
export const validateRequired = (value, fieldName = "Trường này") => {
  if (value === null || value === undefined || value === "") {
    return {
      isValid: false,
      message: `${fieldName} không được để trống`,
    };
  }

  if (typeof value === "string" && value.trim() === "") {
    return {
      isValid: false,
      message: `${fieldName} không được để trống`,
    };
  }

  return {
    isValid: true,
    message: "Hợp lệ",
  };
};

/**
 * Validate date
 * @param {Date|string} date - Date to validate
 * @returns {object} Validation result with isValid and message
 */
export const validateDate = (date) => {
  if (!date) {
    return {
      isValid: false,
      message: "Ngày không được để trống",
    };
  }

  const dateObj = typeof date === "string" ? new Date(date) : date;

  if (isNaN(dateObj.getTime())) {
    return {
      isValid: false,
      message: "Ngày không hợp lệ",
    };
  }

  // Check if date is not in the future
  const today = new Date();
  today.setHours(23, 59, 59, 999); // End of today

  if (dateObj > today) {
    return {
      isValid: false,
      message: "Ngày không thể trong tương lai",
    };
  }

  return {
    isValid: true,
    message: "Ngày hợp lệ",
  };
};

/**
 * Validate form with multiple fields
 * @param {object} data - Form data object
 * @param {object} rules - Validation rules object
 * @returns {object} Validation result with isValid, errors, and firstError
 */
export const validateForm = (data, rules) => {
  const errors = {};
  let isValid = true;
  let firstError = null;

  Object.keys(rules).forEach((field) => {
    const rule = rules[field];
    const value = data[field];

    if (rule.required) {
      const result = validateRequired(value, rule.label || field);
      if (!result.isValid) {
        errors[field] = result.message;
        isValid = false;
        if (!firstError) firstError = result.message;
        return;
      }
    }

    if (
      rule.validator &&
      value !== null &&
      value !== undefined &&
      value !== ""
    ) {
      const result = rule.validator(value);
      if (!result.isValid) {
        errors[field] = result.message;
        isValid = false;
        if (!firstError) firstError = result.message;
      }
    }
  });

  return {
    isValid,
    errors,
    firstError,
  };
};

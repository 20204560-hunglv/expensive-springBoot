/**
 * Validation Constants
 * Chứa các constants liên quan đến validation rules
 */

// Password Validation
export const PASSWORD_RULES = {
  MIN_LENGTH: 6,
  MAX_LENGTH: 128,
  REQUIRE_UPPERCASE: false,
  REQUIRE_LOWERCASE: false,
  REQUIRE_NUMBERS: false,
  REQUIRE_SPECIAL_CHARS: false,
};

// Username Validation
export const USERNAME_RULES = {
  MIN_LENGTH: 3,
  MAX_LENGTH: 50,
  ALLOWED_CHARS: /^[a-zA-Z0-9_.-]+$/,
};

// Email Validation
export const EMAIL_RULES = {
  MAX_LENGTH: 255,
  PATTERN: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
};

// Name Validation
export const NAME_RULES = {
  MIN_LENGTH: 2,
  MAX_LENGTH: 100,
  PATTERN: /^[a-zA-ZÀ-ỹ\s]+$/,
};

// Expense Validation
export const EXPENSE_VALIDATION = {
  TITLE: {
    MIN_LENGTH: 1,
    MAX_LENGTH: 200,
  },
  DESCRIPTION: {
    MAX_LENGTH: 1000,
  },
  AMOUNT: {
    MIN: 1000,
    MAX: 999999999,
  },
};

// Form Validation Messages
export const VALIDATION_MESSAGES = {
  REQUIRED: "Trường này là bắt buộc",
  EMAIL_INVALID: "Email không hợp lệ",
  PASSWORD_TOO_SHORT: `Mật khẩu phải có ít nhất ${PASSWORD_RULES.MIN_LENGTH} ký tự`,
  USERNAME_TOO_SHORT: `Tên đăng nhập phải có ít nhất ${USERNAME_RULES.MIN_LENGTH} ký tự`,
  USERNAME_INVALID: "Tên đăng nhập chỉ được chứa chữ cái, số, dấu gạch dưới và dấu chấm",
  NAME_TOO_SHORT: `Tên phải có ít nhất ${NAME_RULES.MIN_LENGTH} ký tự`,
  NAME_INVALID: "Tên chỉ được chứa chữ cái và khoảng trắng",
  AMOUNT_TOO_SMALL: `Số tiền phải ít nhất ${EXPENSE_VALIDATION.AMOUNT.MIN.toLocaleString()} VND`,
  AMOUNT_TOO_LARGE: `Số tiền không được vượt quá ${EXPENSE_VALIDATION.AMOUNT.MAX.toLocaleString()} VND`,
  PASSWORDS_NOT_MATCH: "Mật khẩu xác nhận không khớp",
};

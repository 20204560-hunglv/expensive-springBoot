/**
 * @deprecated
 * This file is deprecated. Please import directly from specific files in ../consts/ instead.
 *
 * Tệp này đã lỗi thời. Vui lòng import trực tiếp từ các file cụ thể trong ../consts/ thay thế.
 *
 * Examples:
 * import { API_BASE_URL } from "../consts/api.js";
 * import { STORAGE_KEYS } from "../consts/storage.js";
 * import { DEFAULT_CATEGORIES } from "../consts/expense.js";
 */

// Re-export from new constants structure for backward compatibility
export { API_BASE_URL } from "../consts/api.js";
export { STORAGE_KEYS } from "../consts/storage.js";
export { DATE_FORMATS, PAGINATION, CURRENCY } from "../consts/app.js";
export { DEFAULT_CATEGORIES } from "../consts/expense.js";
export {
  PASSWORD_RULES as VALIDATION,
  EXPENSE_VALIDATION,
} from "../consts/validation.js";

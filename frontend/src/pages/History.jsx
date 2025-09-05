import { useState } from "react";
import {
  Container,
  Typography,
  Box,
  Card,
  CardContent,
  Button,
  TextField,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  IconButton,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Chip,
} from "@mui/material";
import {
  Delete as DeleteIcon,
  Edit as EditIcon,
  FilterList as FilterIcon,
  Search as SearchIcon,
} from "@mui/icons-material";
import { useExpense } from "../hooks/useExpense";

const History = () => {
  const { expenses, categories, deleteExpense, getCategoryById } = useExpense();
  const [searchTerm, setSearchTerm] = useState("");
  const [categoryFilter, setCategoryFilter] = useState("");
  const [sortBy, setSortBy] = useState("date");
  const [deleteDialog, setDeleteDialog] = useState({
    open: false,
    expense: null,
  });

  // Filter and sort expenses
  const filteredExpenses = expenses
    .filter((expense) => {
      const matchesSearch =
        expense.description?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        "";
      const matchesCategory =
        !categoryFilter || expense.categoryId === categoryFilter;
      return matchesSearch && matchesCategory;
    })
    .sort((a, b) => {
      switch (sortBy) {
        case "amount":
          return b.amount - a.amount;
        case "category":
          return a.categoryId - b.categoryId;
        case "date":
        default:
          return new Date(b.date) - new Date(a.date);
      }
    });

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
    }).format(amount);
  };

  const handleDeleteClick = (expense) => {
    setDeleteDialog({ open: true, expense });
  };

  const handleDeleteConfirm = () => {
    if (deleteDialog.expense) {
      deleteExpense(deleteDialog.expense.id);
      setDeleteDialog({ open: false, expense: null });
    }
  };

  const handleDeleteCancel = () => {
    setDeleteDialog({ open: false, expense: null });
  };

  const totalFiltered = filteredExpenses.reduce(
    (sum, expense) => sum + expense.amount,
    0
  );

  return (
    <Container maxWidth="lg">
      <Box className="space-y-6">
        {/* Header */}
        <Box className="text-center">
          <Typography
            variant="h4"
            component="h1"
            className="text-white font-bold mb-2"
          >
            Lịch sử chi tiêu 📊
          </Typography>
          <Typography variant="body1" className="text-white/80">
            Quản lý và theo dõi tất cả các giao dịch chi tiêu của bạn
          </Typography>
        </Box>

        {/* Filters */}
        <Card>
          <CardContent>
            <Box className="flex flex-col md:flex-row gap-4 items-center">
              <TextField
                fullWidth
                variant="outlined"
                placeholder="Tìm kiếm theo mô tả..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                InputProps={{
                  startAdornment: <SearchIcon className="mr-2 text-gray-400" />,
                }}
                className="md:max-w-xs"
              />

              <FormControl className="md:max-w-xs w-full">
                <InputLabel>Phân loại</InputLabel>
                <Select
                  value={categoryFilter}
                  label="Phân loại"
                  onChange={(e) => setCategoryFilter(e.target.value)}
                >
                  <MenuItem value="">Tất cả</MenuItem>
                  {categories.map((category) => (
                    <MenuItem key={category.id} value={category.id}>
                      {category.icon} {category.name}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>

              <FormControl className="md:max-w-xs w-full">
                <InputLabel>Sắp xếp</InputLabel>
                <Select
                  value={sortBy}
                  label="Sắp xếp"
                  onChange={(e) => setSortBy(e.target.value)}
                >
                  <MenuItem value="date">Ngày (mới nhất)</MenuItem>
                  <MenuItem value="amount">Số tiền (cao nhất)</MenuItem>
                  <MenuItem value="category">Phân loại</MenuItem>
                </Select>
              </FormControl>
            </Box>

            {/* Summary */}
            <Box className="mt-4 pt-4 border-t border-gray-200">
              <Box className="flex justify-between items-center">
                <Typography variant="body1">
                  Hiển thị <strong>{filteredExpenses.length}</strong> /{" "}
                  {expenses.length} giao dịch
                </Typography>
                <Chip
                  label={`Tổng: ${formatCurrency(totalFiltered)}`}
                  color="primary"
                  variant="outlined"
                />
              </Box>
            </Box>
          </CardContent>
        </Card>

        {/* Expense List */}
        {filteredExpenses.length === 0 ? (
          <Card>
            <CardContent className="text-center py-12">
              <Typography variant="h6" color="text.secondary" gutterBottom>
                {expenses.length === 0
                  ? "Chưa có chi tiêu nào được ghi nhận"
                  : "Không tìm thấy giao dịch nào phù hợp"}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                {expenses.length === 0
                  ? "Hãy thêm chi tiêu đầu tiên của bạn!"
                  : "Thử thay đổi bộ lọc hoặc từ khóa tìm kiếm"}
              </Typography>
            </CardContent>
          </Card>
        ) : (
          <Box className="space-y-3">
            {filteredExpenses.map((expense) => {
              const category = getCategoryById(expense.categoryId);
              return (
                <Card
                  key={expense.id}
                  className="hover:shadow-md transition-shadow"
                >
                  <CardContent>
                    <Box className="flex items-center justify-between">
                      <Box className="flex items-center flex-1">
                        <Typography className="text-3xl mr-4">
                          {category?.icon || "📝"}
                        </Typography>
                        <Box className="flex-1">
                          <Typography variant="h6" className="font-medium">
                            {expense.description || "Không có mô tả"}
                          </Typography>
                          <Box className="flex items-center gap-2 mt-1">
                            <Chip
                              label={category?.name || "Không phân loại"}
                              size="small"
                              style={{
                                backgroundColor: category?.color + "20",
                                color: category?.color,
                              }}
                            />
                            <Typography variant="body2" color="text.secondary">
                              {new Date(expense.date).toLocaleDateString(
                                "vi-VN",
                                {
                                  weekday: "short",
                                  year: "numeric",
                                  month: "short",
                                  day: "numeric",
                                }
                              )}
                            </Typography>
                          </Box>
                        </Box>
                      </Box>

                      <Box className="text-right">
                        <Typography
                          variant="h6"
                          className="font-bold text-red-600 mb-2"
                        >
                          -{formatCurrency(expense.amount)}
                        </Typography>
                        <Box className="flex gap-1">
                          <IconButton size="small" color="primary">
                            <EditIcon fontSize="small" />
                          </IconButton>
                          <IconButton
                            size="small"
                            color="error"
                            onClick={() => handleDeleteClick(expense)}
                          >
                            <DeleteIcon fontSize="small" />
                          </IconButton>
                        </Box>
                      </Box>
                    </Box>
                  </CardContent>
                </Card>
              );
            })}
          </Box>
        )}
      </Box>

      {/* Delete Confirmation Dialog */}
      <Dialog
        open={deleteDialog.open}
        onClose={handleDeleteCancel}
        aria-labelledby="delete-dialog-title"
      >
        <DialogTitle id="delete-dialog-title">
          Xác nhận xóa giao dịch
        </DialogTitle>
        <DialogContent>
          <Typography>
            Bạn có chắc chắn muốn xóa giao dịch "
            {deleteDialog.expense?.description}" với số tiền{" "}
            {deleteDialog.expense &&
              formatCurrency(deleteDialog.expense.amount)}
            ?
          </Typography>
          <Typography variant="body2" color="error" className="mt-2">
            Hành động này không thể hoàn tác.
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleDeleteCancel}>Hủy</Button>
          <Button
            onClick={handleDeleteConfirm}
            color="error"
            variant="contained"
          >
            Xóa
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default History;

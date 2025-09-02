import { useState } from 'react';
import {
  Container,
  Typography,
  Box,
  Card,
  CardContent,
  Button,
  Grid,
  Fab,
} from '@mui/material';
import {
  Add as AddIcon,
  TrendingUp as TrendingUpIcon,
  AccountBalanceWallet as WalletIcon,
  Receipt as ReceiptIcon,
} from '@mui/icons-material';
import { useExpense } from '../hooks/useExpense';
import { useAuth } from '../hooks/useAuth';

const Home = () => {
  const { user } = useAuth();
  const { expenses, totalExpense, categories } = useExpense();
  const [showAddExpense, setShowAddExpense] = useState(false);

  // Calculate statistics
  const todayExpenses = expenses.filter(expense => {
    const today = new Date().toDateString();
    const expenseDate = new Date(expense.date).toDateString();
    return today === expenseDate;
  });

  const thisMonthExpenses = expenses.filter(expense => {
    const today = new Date();
    const expenseDate = new Date(expense.date);
    return expenseDate.getMonth() === today.getMonth() && 
           expenseDate.getFullYear() === today.getFullYear();
  });

  const monthlyTotal = thisMonthExpenses.reduce((sum, expense) => sum + expense.amount, 0);
  const dailyAverage = monthlyTotal / new Date().getDate();

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND',
    }).format(amount);
  };

  return (
    <Container maxWidth="lg">
      <Box className="space-y-6">
        {/* Welcome Section */}
        <Box className="text-center mb-8">
          <Typography variant="h4" component="h1" className="text-white font-bold mb-2">
            Xin chào, {user?.name}! 👋
          </Typography>
          <Typography variant="body1" className="text-white/80">
            Hôm nay bạn có {todayExpenses.length} giao dịch chi tiêu
          </Typography>
        </Box>

        {/* Quick Stats */}
        <Grid container spacing={3}>
          <Grid item xs={12} md={4}>
            <Card className="glass-effect">
              <CardContent className="text-center">
                <Box className="flex justify-center mb-2">
                  <WalletIcon color="primary" sx={{ fontSize: 40 }} />
                </Box>
                <Typography variant="h6" color="primary" gutterBottom>
                  Tháng này
                </Typography>
                <Typography variant="h4" className="font-bold text-green-600">
                  {formatCurrency(monthlyTotal)}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Tổng chi tiêu
                </Typography>
              </CardContent>
            </Card>
          </Grid>

          <Grid item xs={12} md={4}>
            <Card className="glass-effect">
              <CardContent className="text-center">
                <Box className="flex justify-center mb-2">
                  <ReceiptIcon color="primary" sx={{ fontSize: 40 }} />
                </Box>
                <Typography variant="h6" color="primary" gutterBottom>
                  Hôm nay
                </Typography>
                <Typography variant="h4" className="font-bold text-blue-600">
                  {todayExpenses.length}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Giao dịch
                </Typography>
              </CardContent>
            </Card>
          </Grid>

          <Grid item xs={12} md={4}>
            <Card className="glass-effect">
              <CardContent className="text-center">
                <Box className="flex justify-center mb-2">
                  <TrendingUpIcon color="primary" sx={{ fontSize: 40 }} />
                </Box>
                <Typography variant="h6" color="primary" gutterBottom>
                  Trung bình
                </Typography>
                <Typography variant="h4" className="font-bold text-purple-600">
                  {formatCurrency(dailyAverage)}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Mỗi ngày
                </Typography>
              </CardContent>
            </Card>
          </Grid>
        </Grid>

        {/* Recent Expenses */}
        <Card>
          <CardContent>
            <Typography variant="h6" gutterBottom className="flex items-center">
              <ReceiptIcon className="mr-2" />
              Chi tiêu gần đây
            </Typography>
            
            {expenses.length === 0 ? (
              <Box className="text-center py-8">
                <Typography variant="body1" color="text.secondary" gutterBottom>
                  Chưa có chi tiêu nào được ghi nhận
                </Typography>
                <Button
                  variant="contained"
                  startIcon={<AddIcon />}
                  onClick={() => setShowAddExpense(true)}
                  className="mt-2"
                >
                  Thêm chi tiêu đầu tiên
                </Button>
              </Box>
            ) : (
              <Box className="space-y-2">
                {expenses.slice(0, 5).map((expense) => {
                  const category = categories.find(c => c.id === expense.categoryId);
                  return (
                    <Box
                      key={expense.id}
                      className="flex items-center justify-between p-3 rounded-lg bg-gray-50 hover:bg-gray-100 transition-colors"
                    >
                      <Box className="flex items-center">
                        <Typography className="text-2xl mr-3">
                          {category?.icon || '📝'}
                        </Typography>
                        <Box>
                          <Typography variant="subtitle1" className="font-medium">
                            {expense.description || 'Không có mô tả'}
                          </Typography>
                          <Typography variant="body2" color="text.secondary">
                            {category?.name} • {new Date(expense.date).toLocaleDateString('vi-VN')}
                          </Typography>
                        </Box>
                      </Box>
                      <Typography variant="h6" className="font-bold text-red-600">
                        -{formatCurrency(expense.amount)}
                      </Typography>
                    </Box>
                  );
                })}
                
                {expenses.length > 5 && (
                  <Box className="text-center pt-4">
                    <Button variant="outlined" href="/history">
                      Xem tất cả ({expenses.length} giao dịch)
                    </Button>
                  </Box>
                )}
              </Box>
            )}
          </CardContent>
        </Card>

        {/* Categories Overview */}
        <Card>
          <CardContent>
            <Typography variant="h6" gutterBottom>
              Phân loại chi tiêu
            </Typography>
            <Grid container spacing={2}>
              {categories.map((category) => {
                const categoryExpenses = thisMonthExpenses.filter(
                  expense => expense.categoryId === category.id
                );
                const categoryTotal = categoryExpenses.reduce(
                  (sum, expense) => sum + expense.amount, 0
                );
                const percentage = monthlyTotal > 0 ? (categoryTotal / monthlyTotal) * 100 : 0;

                return (
                  <Grid item xs={6} sm={4} md={2} key={category.id}>
                    <Box className="text-center p-3 rounded-lg bg-gray-50 hover:bg-gray-100 transition-colors">
                      <Typography className="text-3xl mb-1">
                        {category.icon}
                      </Typography>
                      <Typography variant="body2" className="font-medium">
                        {category.name}
                      </Typography>
                      <Typography variant="caption" color="text.secondary">
                        {percentage.toFixed(1)}%
                      </Typography>
                      <Typography variant="body2" className="font-bold text-sm">
                        {formatCurrency(categoryTotal)}
                      </Typography>
                    </Box>
                  </Grid>
                );
              })}
            </Grid>
          </CardContent>
        </Card>
      </Box>

      {/* Floating Action Button */}
      <Fab
        color="primary"
        aria-label="add expense"
        className="fixed bottom-6 right-6"
        onClick={() => setShowAddExpense(true)}
      >
        <AddIcon />
      </Fab>
    </Container>
  );
};

export default Home;

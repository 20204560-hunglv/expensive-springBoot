import { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import {
  Container,
  Paper,
  TextField,
  Button,
  Typography,
  Box,
  Alert,
  Link,
  InputAdornment,
  IconButton,
  Divider,
  Stack,
  Checkbox,
  FormControlLabel,
  Grid,
} from "@mui/material";
import {
  Visibility,
  VisibilityOff,
  Person as PersonIcon,
  Lock as LockIcon,
  Login as LoginIcon,
  Facebook,
  Twitter,
  Google,
} from "@mui/icons-material";
import { useAuth } from "../../hooks/useAuth";

const Login = () => {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [rememberMe, setRememberMe] = useState(false);

  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const from = location.state?.from?.pathname || "/";

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
    // Clear error when user starts typing
    if (error) setError("");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setError("");

    try {
      // Simulate API call - replace with actual API later
      await new Promise((resolve) => setTimeout(resolve, 1000));

      // Mock successful login
      if (formData.username && formData.password) {
        const mockUser = {
          id: 1,
          name: formData.username,
          email: `${formData.username}@example.com`,
        };
        const mockToken = "mock-jwt-token-" + Date.now();

        login(mockToken, mockUser);
        navigate(from, { replace: true });
      } else {
        setError("Vui lòng nhập đầy đủ thông tin đăng nhập");
      }
    } catch {
      setError("Đăng nhập thất bại. Vui lòng thử lại.");
    } finally {
      setIsLoading(false);
    }
  };

  const handleClickShowPassword = () => {
    setShowPassword(!showPassword);
  };

  return (
    <Box className="min-h-screen bg-gray-50 flex items-center justify-center p-4">
      <Container maxWidth="lg">
        <Grid container spacing={0} className="bg-white rounded-3xl shadow-2xl overflow-hidden">
          {/* Left Side - Illustration */}
          <Grid item xs={12} md={6} className="bg-gradient-to-br from-blue-50 to-indigo-100 p-8 md:p-12">
            <Box className="h-full flex flex-col justify-center items-center">
              {/* Illustration Placeholder - You can replace with actual illustration */}
              <Box className="w-full max-w-md mb-8">
                <Box className="relative">
                  {/* Person working on laptop illustration */}
                  <Box className="bg-white rounded-2xl p-8 shadow-lg">
                    <Box className="flex justify-center mb-6">
                      <Box className="w-24 h-24 bg-gradient-to-br from-blue-400 to-indigo-500 rounded-full flex items-center justify-center">
                        <Typography className="text-4xl">👨‍💻</Typography>
                      </Box>
                    </Box>
                    <Typography variant="h5" className="text-center font-bold text-gray-800 mb-2">
                      Chào mừng trở lại!
                    </Typography>
                    <Typography variant="body2" className="text-center text-gray-600">
                      Đăng nhập để tiếp tục quản lý chi tiêu của bạn
                    </Typography>
                  </Box>
                  
                  {/* Decorative elements */}
                  <Box className="absolute -top-4 -right-4 w-8 h-8 bg-yellow-300 rounded-full opacity-60"></Box>
                  <Box className="absolute -bottom-4 -left-4 w-6 h-6 bg-pink-300 rounded-full opacity-60"></Box>
                </Box>
              </Box>
              
              {/* Additional decorative elements */}
              <Box className="flex space-x-2">
                <Box className="w-3 h-3 bg-blue-400 rounded-full"></Box>
                <Box className="w-3 h-3 bg-indigo-400 rounded-full"></Box>
                <Box className="w-3 h-3 bg-purple-400 rounded-full"></Box>
              </Box>
            </Box>
          </Grid>

          {/* Right Side - Login Form */}
          <Grid item xs={12} md={6} className="p-8 md:p-12">
            <Box className="h-full flex flex-col justify-center">
              {/* Header */}
              <Box className="mb-8">
                <Typography variant="h4" className="font-bold text-gray-900 mb-2">
                  Đăng nhập
                </Typography>
                <Typography variant="body1" className="text-gray-600">
                  Nhập thông tin để truy cập tài khoản của bạn
                </Typography>
              </Box>

              {/* Error Alert */}
              {error && (
                <Alert severity="error" className="mb-6 rounded-lg">
                  {error}
                </Alert>
              )}

              {/* Login Form */}
              <Box component="form" onSubmit={handleSubmit} className="space-y-6">
                {/* Username Field */}
                <TextField
                  fullWidth
                  id="username"
                  label="Tên đăng nhập"
                  name="username"
                  autoComplete="username"
                  autoFocus
                  value={formData.username}
                  onChange={handleChange}
                  disabled={isLoading}
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <PersonIcon className="text-gray-400" />
                      </InputAdornment>
                    ),
                  }}
                  className="[&_.MuiOutlinedInput-root]:rounded-lg [&_.MuiOutlinedInput-root]:bg-gray-50"
                />

                {/* Password Field */}
                <TextField
                  fullWidth
                  name="password"
                  label="Mật khẩu"
                  type={showPassword ? "text" : "password"}
                  id="password"
                  autoComplete="current-password"
                  value={formData.password}
                  onChange={handleChange}
                  disabled={isLoading}
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <LockIcon className="text-gray-400" />
                      </InputAdornment>
                    ),
                    endAdornment: (
                      <InputAdornment position="end">
                        <IconButton
                          aria-label="toggle password visibility"
                          onClick={handleClickShowPassword}
                          edge="end"
                          disabled={isLoading}
                        >
                          {showPassword ? <VisibilityOff /> : <Visibility />}
                        </IconButton>
                      </InputAdornment>
                    ),
                  }}
                  className="[&_.MuiOutlinedInput-root]:rounded-lg [&_.MuiOutlinedInput-root]:bg-gray-50"
                />

                {/* Remember Me */}
                <Box className="flex items-center justify-between">
                  <FormControlLabel
                    control={
                      <Checkbox
                        checked={rememberMe}
                        onChange={(e) => setRememberMe(e.target.checked)}
                        color="primary"
                      />
                    }
                    label="Ghi nhớ đăng nhập"
                    className="text-sm"
                  />
                  <Link
                    component="button"
                    type="button"
                    variant="body2"
                    className="text-blue-600 hover:text-blue-800 text-sm"
                  >
                    Quên mật khẩu?
                  </Link>
                </Box>

                {/* Login Button */}
                <Button
                  type="submit"
                  fullWidth
                  variant="contained"
                  disabled={isLoading}
                  className="py-3 text-lg font-semibold rounded-lg bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 shadow-lg"
                >
                  {isLoading ? "Đang đăng nhập..." : "Đăng nhập"}
                </Button>

                {/* Divider */}
                <Box className="relative my-6">
                  <Divider className="border-gray-300" />
                  <Typography 
                    variant="body2" 
                    className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 bg-white px-4 text-gray-500"
                  >
                    hoặc
                  </Typography>
                </Box>

                {/* Social Login Buttons */}
                <Box className="space-y-3">
                  <Typography variant="body2" className="text-center text-gray-600 mb-4">
                    Đăng nhập bằng
                  </Typography>
                  
                  <Stack direction="row" spacing={2} className="justify-center">
                    <IconButton 
                      className="bg-blue-600 text-white hover:bg-blue-700 rounded-lg w-12 h-12"
                      size="large"
                    >
                      <Facebook />
                    </IconButton>
                    <IconButton 
                      className="bg-sky-500 text-white hover:bg-sky-600 rounded-lg w-12 h-12"
                      size="large"
                    >
                      <Twitter />
                    </IconButton>
                    <IconButton 
                      className="bg-red-500 text-white hover:bg-red-600 rounded-lg w-12 h-12"
                      size="large"
                    >
                      <Google />
                    </IconButton>
                  </Stack>
                </Box>

                {/* Sign Up Link */}
                <Box className="text-center mt-6">
                  <Typography variant="body2" className="text-gray-600">
                    Chưa có tài khoản?{" "}
                    <Link
                      component="button"
                      type="button"
                      variant="body2"
                      onClick={() => navigate("/register")}
                      className="text-blue-600 hover:text-blue-800 font-semibold"
                    >
                      Đăng ký ngay
                    </Link>
                  </Typography>
                </Box>
              </Box>
            </Box>
          </Grid>
        </Grid>

        {/* Demo Info */}
        <Box className="mt-6 text-center">
          <Paper
            elevation={2}
            className="inline-block px-6 py-3 bg-white rounded-xl shadow-lg"
          >
            <Typography variant="body2" className="text-blue-600 font-semibold">
              💡 <strong>Demo:</strong> Nhập bất kỳ tên đăng nhập và mật khẩu nào để thử nghiệm
            </Typography>
          </Paper>
        </Box>
      </Container>
    </Box>
  );
};

export default Login;

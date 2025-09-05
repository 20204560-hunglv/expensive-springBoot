import { useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  Container,
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
import { Paper } from "@mui/material";
import {
  Visibility,
  VisibilityOff,
  Person as PersonIcon,
  Email as EmailIcon,
  Lock as LockIcon,
  PersonAdd as PersonAddIcon,
  AccountCircle as AccountCircleIcon,
  Facebook,
  Twitter,
  Google,
} from "@mui/icons-material";

const Register = () => {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    username: "",
    password: "",
    confirmPassword: "",
  });
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [agreeTerms, setAgreeTerms] = useState(false);

  const navigate = useNavigate();

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
      // Validate passwords match
      if (formData.password !== formData.confirmPassword) {
        setError("Mật khẩu xác nhận không khớp");
        return;
      }

      // Validate required fields
      if (
        !formData.name ||
        !formData.email ||
        !formData.username ||
        !formData.password
      ) {
        setError("Vui lòng điền đầy đủ thông tin");
        return;
      }

      // Simulate API call - replace with actual API later
      await new Promise((resolve) => setTimeout(resolve, 1000));

      // Mock successful registration
      alert("Đăng ký thành công! Bạn có thể đăng nhập ngay bây giờ.");
      navigate("/login");
    } catch {
      setError("Đăng ký thất bại. Vui lòng thử lại.");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Box className="min-h-screen bg-gray-50 flex items-center justify-center p-4">
      <Container maxWidth="lg">
        <Grid container spacing={0} className="bg-white rounded-3xl shadow-2xl overflow-hidden min-h-[600px]">
          {/* Left Side - Illustration */}
          <Grid item xs={12} md={6} className="bg-gradient-to-br from-green-50 to-emerald-100 p-8 md:p-12 flex items-center justify-center">
            <Box className="w-full max-w-sm">
              <Box className="relative">
                {/* Person creating account illustration */}
                <Box className="bg-white rounded-2xl p-8 shadow-lg mb-6">
                  <Box className="flex justify-center mb-6">
                    <Box className="w-24 h-24 bg-gradient-to-br from-green-400 to-emerald-500 rounded-full flex items-center justify-center">
                      <Typography className="text-4xl">👤</Typography>
                    </Box>
                  </Box>
                  <Typography variant="h5" className="text-center font-bold text-gray-800 mb-2">
                    Tạo tài khoản mới!
                  </Typography>
                  <Typography variant="body2" className="text-center text-gray-600">
                    Tham gia cùng chúng tôi để quản lý chi tiêu thông minh
                  </Typography>
                </Box>
                
                {/* Decorative elements */}
                <Box className="absolute -top-4 -right-4 w-8 h-8 bg-green-300 rounded-full opacity-60"></Box>
                <Box className="absolute -bottom-4 -left-4 w-6 h-6 bg-emerald-300 rounded-full opacity-60"></Box>
              </Box>
              
              {/* Additional decorative elements */}
              <Box className="flex justify-center space-x-2">
                <Box className="w-3 h-3 bg-green-400 rounded-full"></Box>
                <Box className="w-3 h-3 bg-emerald-400 rounded-full"></Box>
                <Box className="w-3 h-3 bg-teal-400 rounded-full"></Box>
              </Box>
            </Box>
          </Grid>

          {/* Right Side - Register Form */}
          <Grid item xs={12} md={6} className="p-8 md:p-12 flex items-center justify-center">
            <Box className="w-full max-w-md">
              {/* Header */}
              <Box className="mb-8">
                <Typography variant="h4" className="font-bold text-gray-900 mb-2">
                  Đăng ký
                </Typography>
                <Typography variant="body1" className="text-gray-600">
                  Tạo tài khoản để bắt đầu hành trình quản lý chi tiêu thông minh
                </Typography>
              </Box>

              {/* Error Alert */}
              {error && (
                <Alert severity="error" className="mb-6 rounded-lg">
                  {error}
                </Alert>
              )}

              {/* Register Form */}
              <Box component="form" onSubmit={handleSubmit} className="space-y-4">
                {/* Name Field */}
                <TextField
                  fullWidth
                  id="name"
                  label="Họ và tên"
                  name="name"
                  autoComplete="name"
                  autoFocus
                  value={formData.name}
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

                {/* Email Field */}
                <TextField
                  fullWidth
                  id="email"
                  label="Email"
                  name="email"
                  type="email"
                  autoComplete="email"
                  value={formData.email}
                  onChange={handleChange}
                  disabled={isLoading}
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <EmailIcon className="text-gray-400" />
                      </InputAdornment>
                    ),
                  }}
                  className="[&_.MuiOutlinedInput-root]:rounded-lg [&_.MuiOutlinedInput-root]:bg-gray-50"
                />

                {/* Username Field */}
                <TextField
                  fullWidth
                  id="username"
                  label="Tên đăng nhập"
                  name="username"
                  autoComplete="username"
                  value={formData.username}
                  onChange={handleChange}
                  disabled={isLoading}
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <AccountCircleIcon className="text-gray-400" />
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
                  autoComplete="new-password"
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
                          onClick={() => setShowPassword(!showPassword)}
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

                {/* Confirm Password Field */}
                <TextField
                  fullWidth
                  name="confirmPassword"
                  label="Xác nhận mật khẩu"
                  type={showConfirmPassword ? "text" : "password"}
                  id="confirmPassword"
                  autoComplete="new-password"
                  value={formData.confirmPassword}
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
                          aria-label="toggle confirm password visibility"
                          onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                          edge="end"
                          disabled={isLoading}
                        >
                          {showConfirmPassword ? <VisibilityOff /> : <Visibility />}
                        </IconButton>
                      </InputAdornment>
                    ),
                  }}
                  className="[&_.MuiOutlinedInput-root]:rounded-lg [&_.MuiOutlinedInput-root]:bg-gray-50"
                />

                {/* Terms and Conditions */}
                <Box className="flex items-start space-x-2">
                  <Checkbox
                    checked={agreeTerms}
                    onChange={(e) => setAgreeTerms(e.target.checked)}
                    color="primary"
                    size="small"
                  />
                  <Typography variant="body2" className="text-gray-600 text-sm">
                    Tôi đồng ý với{" "}
                    <Link
                      component="button"
                      type="button"
                      variant="body2"
                      className="text-blue-600 hover:text-blue-800 font-semibold"
                    >
                      Điều khoản sử dụng
                    </Link>{" "}
                    và{" "}
                    <Link
                      component="button"
                      type="button"
                      variant="body2"
                      className="text-blue-600 hover:text-blue-800 font-semibold"
                    >
                      Chính sách bảo mật
                    </Link>
                  </Typography>
                </Box>

                {/* Register Button */}
                <Button
                  type="submit"
                  fullWidth
                  variant="contained"
                  disabled={isLoading || !agreeTerms}
                  startIcon={<PersonAddIcon />}
                  className="py-3 text-lg font-semibold rounded-lg bg-gradient-to-r from-green-600 to-emerald-600 hover:from-green-700 hover:to-emerald-700 shadow-lg disabled:opacity-50"
                >
                  {isLoading ? "Đang đăng ký..." : "Đăng ký"}
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

                {/* Social Register Buttons */}
                <Box className="space-y-3">
                  <Typography variant="body2" className="text-center text-gray-600 mb-4">
                    Đăng ký bằng
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

                {/* Login Link */}
                <Box className="text-center mt-6">
                  <Typography variant="body2" className="text-gray-600">
                    Đã có tài khoản?{" "}
                    <Link
                      component="button"
                      type="button"
                      variant="body2"
                      onClick={() => navigate("/login")}
                      className="text-green-600 hover:text-green-800 font-semibold"
                    >
                      Đăng nhập ngay
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
            <Typography variant="body2" className="text-green-600 font-semibold">
              💡 <strong>Demo:</strong> Điền đầy đủ thông tin để tạo tài khoản demo
            </Typography>
          </Paper>
        </Box>
      </Container>
    </Box>
  );
};

export default Register;

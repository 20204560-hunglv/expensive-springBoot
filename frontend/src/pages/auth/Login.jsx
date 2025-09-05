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
} from "@mui/material";
import {
  Visibility,
  VisibilityOff,
  Person as PersonIcon,
  Lock as LockIcon,
  Login as LoginIcon,
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
    <Container className="min-h-screen" maxWidth="xs">
      <Paper
        elevation={6}
        sx={{
          padding: 4,
          borderRadius: 3,
          bgcolor: "primary.main",
        }}
      >
        {/* Logo và Header */}
        <Box sx={{ textAlign: "center", mb: 4 }}>
          <Box
            sx={{
              display: "inline-flex",
              alignItems: "center",
              justifyContent: "center",
              width: 60,
              height: 60,
              bgcolor: "rgba(255, 255, 255, 0.2)",
              borderRadius: "50%",
              mb: 2,
            }}
          >
            <Typography sx={{ fontSize: "2rem" }}>💰</Typography>
          </Box>

          <Typography
            variant="h4"
            component="h1"
            sx={{
              fontWeight: "bold",
              color: "white",
              mb: 1,
            }}
          >
            Expensive App
          </Typography>

          <Typography
            variant="h5"
            component="h2"
            sx={{
              fontWeight: 600,
              color: "white",
              mb: 1,
            }}
          >
            Đăng nhập
          </Typography>

          <Typography
            variant="body1"
            sx={{
              color: "white",
            }}
          >
            Chào mừng bạn trở lại!
          </Typography>
        </Box>

        {/* Error Alert */}
        {error && (
          <Alert severity="error" sx={{ mb: 3, borderRadius: 2 }}>
            {error}
          </Alert>
        )}

        {/* Form */}
        <Box component="form" onSubmit={handleSubmit} sx={{ mt: 1 }}>
          <TextField
            margin="normal"
            required
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
                  <PersonIcon color="action" />
                </InputAdornment>
              ),
            }}
            sx={{
              "& .MuiOutlinedInput-root": {
                bgcolor: "rgba(255, 255, 255, 0.9)",
                borderRadius: 2,
              },
              "& .MuiInputLabel-root": {
                color: "rgba(255, 255, 255, 0.9)",
              },
            }}
          />

          <TextField
            margin="normal"
            required
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
                  <LockIcon color="action" />
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
            sx={{
              "& .MuiOutlinedInput-root": {
                bgcolor: "rgba(255, 255, 255, 0.9)",
                borderRadius: 2,
              },
              "& .MuiInputLabel-root": {
                color: "rgba(255, 255, 255, 0.9)",
              },
            }}
          />

          <Button
            type="submit"
            fullWidth
            variant="contained"
            disabled={isLoading}
            startIcon={<LoginIcon />}
            sx={{
              mt: 3,
              mb: 2,
              py: 1.5,
              fontSize: "1.1rem",
              fontWeight: 600,
              bgcolor: "rgba(255, 255, 255, 0.2)",
              color: "white",
              borderRadius: 2,
            }}
          >
            {isLoading ? "Đang đăng nhập..." : "Đăng nhập"}
          </Button>

          <Divider sx={{ my: 3, borderColor: "rgba(255, 255, 255, 0.6)" }}>
            <Typography
              variant="body2"
              sx={{ color: "white", fontWeight: 600 }}
            >
              hoặc
            </Typography>
          </Divider>

          <Box sx={{ textAlign: "center" }}>
            <Typography variant="body1" sx={{ color: "white" }}>
              Chưa có tài khoản?{" "}
              <Link
                component="button"
                type="button"
                variant="body1"
                onClick={() => navigate("/register")}
                sx={{
                  fontWeight: 700,
                  color: "white",
                  textDecoration: "underline",
                }}
              >
                Đăng ký ngay
              </Link>
            </Typography>
          </Box>
        </Box>
      </Paper>

      {/* Demo hint */}
      <Box sx={{ mt: 3, textAlign: "center" }}>
        <Paper
          elevation={2}
          sx={{
            display: "inline-block",
            px: 3,
            py: 2,
            bgcolor: "background.paper",
            borderRadius: 2,
            maxWidth: "400px",
          }}
        >
          <Typography
            variant="body2"
            sx={{ color: "primary.main", fontWeight: 600 }}
          >
            💡 <strong>Demo:</strong> Nhập bất kỳ tên đăng nhập và mật khẩu nào
            để thử nghiệm
          </Typography>
        </Paper>
      </Box>
    </Container>
  );
};

export default Login;

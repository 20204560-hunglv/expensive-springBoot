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
    <Box
      sx={{
        minHeight: "100vh",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        bgcolor: "background.default",
        py: 2,
      }}
    >
      <Container component="main" maxWidth="xs">
        <Paper
          elevation={10}
          sx={{
            padding: 4,
            borderRadius: 4,
            background:
              "linear-gradient(135deg, #1976d2 0%, #42a5f5 50%, #2196f3 100%)",
            boxShadow: "0 8px 32px rgba(25, 118, 210, 0.3)",
          }}
        >
          {/* Logo và Header */}
          <Box sx={{ textAlign: "center", mb: 4 }}>
            <Box
              sx={{
                display: "inline-flex",
                alignItems: "center",
                justifyContent: "center",
                width: 80,
                height: 80,
                background: "rgba(255, 255, 255, 0.2)",
                borderRadius: "50%",
                mb: 3,
                boxShadow: "0 8px 32px rgba(255, 255, 255, 0.3)",
                border: "2px solid rgba(255, 255, 255, 0.3)",
              }}
            >
              <Typography sx={{ fontSize: "2.5rem" }}>💰</Typography>
            </Box>

            <Typography
              variant="h4"
              component="h1"
              sx={{
                fontWeight: "bold",
                color: "white",
                textShadow: "2px 2px 4px rgba(0, 0, 0, 0.3)",
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
                textShadow: "1px 1px 2px rgba(0, 0, 0, 0.3)",
              }}
            >
              Đăng nhập
            </Typography>

            <Typography
              variant="body1"
              sx={{
                color: "white",
                textShadow: "1px 1px 2px rgba(0, 0, 0, 0.3)",
              }}
            >
              Chào mừng bạn trở lại! Hãy đăng nhập để tiếp tục quản lý chi tiêu
              thông minh.
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
                  borderRadius: 3,
                  backgroundColor: "rgba(255, 255, 255, 0.95)",
                  "& .MuiOutlinedInput-notchedOutline": {
                    borderColor: "rgba(255, 255, 255, 0.8)",
                    borderWidth: "1px",
                  },
                  "&:hover": {
                    backgroundColor: "white",
                    "& .MuiOutlinedInput-notchedOutline": {
                      borderColor: "white",
                      borderWidth: "2px",
                    },
                  },
                  "&.Mui-focused": {
                    backgroundColor: "white",
                    "& .MuiOutlinedInput-notchedOutline": {
                      borderColor: "white",
                      borderWidth: "2px",
                    },
                  },
                },
                "& .MuiInputLabel-root": {
                  color: "rgba(255, 255, 255, 0.9)",
                  fontWeight: 500,
                  fontSize: "1rem",
                },
                "& .MuiInputLabel-root.Mui-focused": {
                  color: "white",
                  fontWeight: 600,
                },
                "& .MuiInputLabel-root.MuiInputLabel-shrink": {
                  color: "white",
                  fontWeight: 600,
                  fontSize: "0.875rem",
                  transform: "translate(14px, -22px) scale(0.85)",
                  textShadow: "0 1px 3px rgba(0,0,0,0.3)",
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
                  borderRadius: 3,
                  backgroundColor: "rgba(255, 255, 255, 0.95)",
                  "& .MuiOutlinedInput-notchedOutline": {
                    borderColor: "rgba(255, 255, 255, 0.8)",
                    borderWidth: "1px",
                  },
                  "&:hover": {
                    backgroundColor: "white",
                    "& .MuiOutlinedInput-notchedOutline": {
                      borderColor: "white",
                      borderWidth: "2px",
                    },
                  },
                  "&.Mui-focused": {
                    backgroundColor: "white",
                    "& .MuiOutlinedInput-notchedOutline": {
                      borderColor: "white",
                      borderWidth: "2px",
                    },
                  },
                },
                "& .MuiInputLabel-root": {
                  color: "rgba(255, 255, 255, 0.9)",
                  fontWeight: 500,
                  fontSize: "1rem",
                },
                "& .MuiInputLabel-root.Mui-focused": {
                  color: "white",
                  fontWeight: 600,
                },
                "& .MuiInputLabel-root.MuiInputLabel-shrink": {
                  color: "white",
                  fontWeight: 600,
                  fontSize: "0.875rem",
                  transform: "translate(14px, -22px) scale(0.85)",
                  textShadow: "0 1px 3px rgba(0,0,0,0.3)",
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
                borderRadius: 2,
                background: "rgba(255, 255, 255, 0.2)",
                color: "white",
                border: "2px solid rgba(255, 255, 255, 0.4)",
                backdropFilter: "blur(10px)",
                boxShadow: "0 4px 20px rgba(255, 255, 255, 0.2)",
                "&:hover": {
                  background: "rgba(255, 255, 255, 0.3)",
                  boxShadow: "0 6px 25px rgba(255, 255, 255, 0.3)",
                  border: "2px solid rgba(255, 255, 255, 0.6)",
                },
                "&:disabled": {
                  background: "linear-gradient(45deg, #ccc 30%, #999 90%)",
                },
              }}
            >
              {isLoading ? "Đang đăng nhập..." : "Đăng nhập"}
            </Button>

            <Divider sx={{ my: 3, borderColor: "rgba(255, 255, 255, 0.6)" }}>
              <Typography
                variant="body2"
                sx={{
                  color: "white",
                  fontWeight: 600,
                  textShadow: "1px 1px 2px rgba(0, 0, 0, 0.3)",
                }}
              >
                hoặc
              </Typography>
            </Divider>

            <Box sx={{ textAlign: "center" }}>
              <Typography
                variant="body1"
                sx={{
                  color: "white",
                  textShadow: "1px 1px 2px rgba(0, 0, 0, 0.3)",
                }}
              >
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
                    textShadow: "1px 1px 2px rgba(0, 0, 0, 0.3)",
                    "&:hover": {
                      color: "#e3f2fd",
                      textShadow: "2px 2px 4px rgba(0, 0, 0, 0.5)",
                    },
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
            elevation={3}
            sx={{
              display: "inline-block",
              px: 3,
              py: 2,
              background: "linear-gradient(45deg, #e3f2fd, #f3e5f5)",
              border: "1px solid #1976d2",
              borderRadius: 3,
              maxWidth: "400px",
            }}
          >
            <Typography
              variant="body2"
              sx={{ color: "#1976d2", fontWeight: 600 }}
            >
              💡 <strong>Demo:</strong> Nhập bất kỳ tên đăng nhập và mật khẩu
              nào để thử nghiệm
            </Typography>
          </Paper>
        </Box>
      </Container>
    </Box>
  );
};

export default Login;

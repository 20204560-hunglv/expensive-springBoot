import React from "react";
import { Box, Typography, Button } from "@mui/material";

class ErrorBoundary extends React.Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false, error: null };
  }

  static getDerivedStateFromError(error) {
    return { hasError: true, error };
  }

  componentDidCatch(error, errorInfo) {
    console.error("Error caught by boundary:", error, errorInfo);
  }

  render() {
    if (this.state.hasError) {
      return (
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            justifyContent: "center",
            minHeight: "100vh",
            p: 3,
            textAlign: "center",
          }}
        >
          <Typography variant="h4" gutterBottom color="error">
            Oops! Có lỗi xảy ra
          </Typography>
          <Typography variant="body1" sx={{ mb: 3 }}>
            {this.state.error?.message || "Đã xảy ra lỗi không mong muốn"}
          </Typography>
          <Button variant="contained" onClick={() => window.location.reload()}>
            Tải lại trang
          </Button>
        </Box>
      );
    }

    return this.props.children;
  }
}

export default ErrorBoundary;

import { useNavigate } from "react-router-dom";
import {
  Container,
  Typography,
  Box,
  Button,
  Card,
  CardContent,
} from "@mui/material";
import {
  Home as HomeIcon,
  ArrowBack as ArrowBackIcon,
} from "@mui/icons-material";

const NotFound = () => {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-500 to-purple-600">
      <Container maxWidth="sm" className="flex items-center min-h-screen">
        <Card className="w-full bg-white/25 backdrop-blur-sm border border-white/20">
          <CardContent className="text-center py-12">
            <Typography variant="h1" className="text-8xl mb-4">
              🤔
            </Typography>

            <Typography
              variant="h3"
              component="h1"
              gutterBottom
              className="font-bold"
            >
              404
            </Typography>

            <Typography variant="h5" component="h2" gutterBottom>
              Trang không tồn tại
            </Typography>

            <Typography variant="body1" color="text.secondary" className="mb-8">
              Trang bạn đang tìm kiếm không tồn tại hoặc đã được di chuyển.
            </Typography>

            <Box className="flex flex-col sm:flex-row gap-3 justify-center">
              <Button
                variant="contained"
                startIcon={<HomeIcon />}
                onClick={() => navigate("/")}
                size="large"
              >
                Về trang chủ
              </Button>

              <Button
                variant="outlined"
                startIcon={<ArrowBackIcon />}
                onClick={() => navigate(-1)}
                size="large"
              >
                Quay lại
              </Button>
            </Box>
          </CardContent>
        </Card>
      </Container>
    </div>
  );
};

export default NotFound;

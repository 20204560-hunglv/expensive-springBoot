import { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  IconButton,
  Menu,
  MenuItem,
  Avatar,
  Box,
  Container,
} from '@mui/material';
import {
  AccountCircle as AccountCircleIcon,
  Home as HomeIcon,
  History as HistoryIcon,
  Add as AddIcon,
} from '@mui/icons-material';
import { useAuth } from '../../hooks/useAuth';

const Header = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [anchorEl, setAnchorEl] = useState(null);

  const handleMenuOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const handleLogout = () => {
    logout();
    handleMenuClose();
    navigate('/login');
  };

  const isActive = (path) => location.pathname === path;

  return (
    <AppBar 
      position="static" 
      className="bg-white/10 backdrop-blur-sm border-b border-white/20"
      elevation={0}
    >
      <Container maxWidth="lg">
        <Toolbar className="px-0">
          <Typography 
            variant="h6" 
            component="div" 
            className="text-white font-semibold flex-1 cursor-pointer"
            onClick={() => navigate('/')}
          >
            💰 Expensive App
          </Typography>

          {user && (
            <Box className="flex items-center space-x-2">
              {/* Navigation Buttons */}
              <Button
                color="inherit"
                startIcon={<HomeIcon />}
                onClick={() => navigate('/')}
                className={`text-white ${isActive('/') ? 'bg-white/20' : ''}`}
              >
                Trang chủ
              </Button>
              
              <Button
                color="inherit"
                startIcon={<HistoryIcon />}
                onClick={() => navigate('/history')}
                className={`text-white ${isActive('/history') ? 'bg-white/20' : ''}`}
              >
                Lịch sử
              </Button>

              <Button
                color="inherit"
                startIcon={<AddIcon />}
                variant="outlined"
                className="text-white border-white hover:bg-white/20"
              >
                Thêm chi tiêu
              </Button>

              {/* User Menu */}
              <IconButton
                size="large"
                edge="end"
                aria-label="account of current user"
                aria-controls="menu-appbar"
                aria-haspopup="true"
                onClick={handleMenuOpen}
                color="inherit"
              >
                <Avatar 
                  className="w-8 h-8 bg-white/20 text-white"
                  alt={user?.name || 'User'}
                >
                  {user?.name?.charAt(0).toUpperCase() || <AccountCircleIcon />}
                </Avatar>
              </IconButton>

              <Menu
                id="menu-appbar"
                anchorEl={anchorEl}
                anchorOrigin={{
                  vertical: 'bottom',
                  horizontal: 'right',
                }}
                keepMounted
                transformOrigin={{
                  vertical: 'top',
                  horizontal: 'right',
                }}
                open={Boolean(anchorEl)}
                onClose={handleMenuClose}
              >
                <MenuItem onClick={handleMenuClose}>
                  <Typography variant="body2" className="font-medium">
                    {user?.name || 'Người dùng'}
                  </Typography>
                </MenuItem>
                <MenuItem onClick={handleMenuClose}>Cài đặt</MenuItem>
                <MenuItem onClick={handleLogout}>Đăng xuất</MenuItem>
              </Menu>
            </Box>
          )}
        </Toolbar>
      </Container>
    </AppBar>
  );
};

export default Header;

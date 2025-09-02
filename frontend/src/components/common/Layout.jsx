import { Outlet } from 'react-router-dom';
import { Box } from '@mui/material';
import Header from './Header';
import Footer from './Footer';

const Layout = () => {
  return (
    <Box className="app-container min-h-screen flex flex-col">
      <Header />
      <Box component="main" className="flex-1 py-6">
        <Outlet />
      </Box>
      <Footer />
    </Box>
  );
};

export default Layout;

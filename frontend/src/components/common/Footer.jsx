import { Box, Container, Typography } from "@mui/material";

const Footer = () => {
  return (
    <Box
      component="footer"
      className="bg-white/10 backdrop-blur-sm border-t border-white/20 mt-auto"
      sx={{ py: 2 }}
    >
      <Container maxWidth="lg">
        <Typography
          variant="body2"
          color="white"
          align="center"
          className="opacity-75"
        >
          © 2024 Expensive App. Được phát triển với ❤️ bằng React và
          Material-UI.
        </Typography>
      </Container>
    </Box>
  );
};

export default Footer;

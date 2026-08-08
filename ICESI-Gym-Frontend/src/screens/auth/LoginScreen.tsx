import Stack from '@mui/material/Stack';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import { login as loginService } from '../../services/AuthService';
import { useNavigate } from "react-router-dom";
import useAuth from '../../hooks/useAuth';
import { useState } from 'react';
import Snackbar from "@mui/material/Snackbar";
import Alert from "@mui/material/Alert";
import backgroundlogin from "../../assets/backgroundlogin.jpg";

const LoginScreen: React.FC = () => {
    const navigate = useNavigate();
    const { login } = useAuth();

    const [username, setUsername] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [error, setError] = useState(false);

    const handleLogin = async (): Promise<void> => {
        const authRequest = {
            username,
            password,
        };

        const result = await loginService(authRequest);
        
        if (result.error || !result.data) {
            setError(true);
            return;
        }

        // The backend returns the token and user data
        login(result.data.token, result.data.user);

        navigate('/');
    };

    const handleRegister = (): void => {
        navigate("/register");
    };

    return (
        // main container
        <Box 
            sx={{ 
                display: 'flex', 
                justifyContent: 'center', 
                alignItems: 'center', 
                minHeight: '100vh',
                backgroundColor: '#f5f5f7',
                backgroundImage: `url(${backgroundlogin})`, 
                backgroundSize: 'cover',
                backgroundPosition: 'center',
                backgroundRepeat: 'no-repeat',
            }}
        >
            {/* Form card*/}
            <Paper 
                elevation={3} 
                sx={{ 
                    padding: 4, 
                    width: '100%', 
                    maxWidth: 400, 
                    borderRadius: 3 
                }}
            >
                <Stack spacing={3} direction="column">
                    <Box sx={{ textAlign: 'center', mb: 1 }}>
                        <Typography variant='h4' component="h1" sx={{fontWeight:"bold"}} color="primary">
                            Bienvenido
                        </Typography>
                        <Typography variant='body2' color="text.secondary" sx={{ mt: 1 }}>
                            Ingresa tus credenciales para acceder
                        </Typography>
                    </Box>

                    <TextField 
                        label='Usuario' 
                        variant='outlined'
                        fullWidth
                        value={username} 
                        onChange={(e) => setUsername(e.target.value)}
                    />

                    <TextField 
                        label='Contraseña' 
                        type='password' 
                        variant='outlined'
                        fullWidth
                        value={password} 
                        onChange={(e) => setPassword(e.target.value)}
                       
                    />

                    <Button 
                        onClick={handleLogin} 
                        variant="contained" 
                        size="large"
                        fullWidth
                        sx={{ 
                            textTransform: 'none',
                            fontWeight: 'bold',
                            borderRadius: 2,
                            py: 1 
                        }}
                    >
                        Iniciar sesión
                    </Button>

                    <Snackbar
                        open={error}
                        autoHideDuration={4000}
                        onClose={() => setError(false)}
                    >
                        <Alert severity="error" onClose={() => setError(false)}>
                        Usuario o contraseña incorrectos
                        </Alert>
                    </Snackbar>

                    <Button 
                        onClick={handleRegister} 
                        variant="contained" 
                        size="large"
                        fullWidth
                        color="secondary"
                        sx={{ 
                            textTransform: 'none',
                            fontWeight: 'bold',
                            borderRadius: 2,
                            py: 1 
                        }}
                    >
                        Registrarse
                    </Button>

                    
                </Stack>
            </Paper>
        </Box>
    );
};

export default LoginScreen;
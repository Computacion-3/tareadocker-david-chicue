import React, { useEffect, useState } from 'react';
import { 
  Box, 
  Typography,
  Paper, 
  Button, 
  CircularProgress, 
  Alert, 
  Divider,
  Stack,
  Avatar
} from '@mui/material';
import Grid from '@mui/material/Grid';
import { useParams, useNavigate } from 'react-router-dom';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import CalendarMonthIcon from '@mui/icons-material/CalendarMonth';
import MeetingRoomIcon from '@mui/icons-material/MeetingRoom';
import InfoIcon from '@mui/icons-material/Info';
import { getActivityById, type ActivityWithSpace } from '../../services/ActivityService';

const ActivitieDetailScreen: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [activity, setActivity] = useState<ActivityWithSpace | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    let isMounted = true;
    const fetchActivity = async () => {
      if (!id) {return;}
      const result = await getActivityById(Number(id));
      if (isMounted) {
        if (result.error) {
          setError(result.message || 'Error al cargar la actividad');
        } else if (result.data) {
          setActivity(result.data);
        }
        setLoading(false);
      }
    };

    fetchActivity();
    return () => { isMounted = false; };
  }, [id]);

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', py: 8 }}>
        <CircularProgress thickness={5} />
      </Box>
    );
  }

  if (error || !activity) {
    return (
      <Box sx={{ py: 4, maxWidth: 600, mx: 'auto' }}>
        <Alert severity="error" variant="outlined" sx={{ borderRadius: 3 }}>{error || 'Actividad no encontrada'}</Alert>
        <Button 
          startIcon={<ArrowBackIcon />} 
          onClick={() => navigate('/activities')}
          sx={{ mt: 3, fontWeight: 700 }}
        >
          Volver a actividades
        </Button>
      </Box>
    );
  }

  return (
    <Box sx={{ maxWidth: 900, mx: 'auto' }}>
      <Button 
        startIcon={<ArrowBackIcon />} 
        onClick={() => navigate('/activities')}
        sx={{ mb: 4, fontWeight: 700, textTransform: 'none', color: 'text.secondary' }}
      >
        Volver a actividades
      </Button>

      <Paper 
        elevation={0} 
        sx={{ 
          p: { xs: 3, md: 6 }, 
          borderRadius: 5, 
          border: '1px solid',
          borderColor: 'divider',
          backgroundColor: 'white',
          boxShadow: '0 10px 40px 0 rgba(0,0,0,0.03)'
        }}
      >
        <Stack spacing={4}>
          <Box>
            <Typography variant="h3" sx={{ fontWeight: 800, color: 'primary.main', mb: 2, letterSpacing: '-1px' }}>
              {activity.name}
            </Typography>
            
            <Typography variant="body1" sx={{ color: 'text.secondary', fontSize: '1.1rem', fontWeight: 500, lineHeight: 1.6 }}>
              {activity.description || 'Sin descripción detallada disponible.'}
            </Typography>
          </Box>

          <Divider />

          <Grid container spacing={5}>
            <Grid size={{ xs: 12, md: 6 }}>
              <Stack direction="row" spacing={2} sx={{ alignItems: 'center', mb: 3 }}>
                <Avatar sx={{ bgcolor: 'rgba(83, 83, 238, 0.1)', color: 'primary.main' }}>
                  <CalendarMonthIcon />
                </Avatar>
                <Typography variant="h6" sx={{ fontWeight: 800 }}>
                  Cronograma
                </Typography>
              </Stack>
              
              <Stack spacing={3}>
                <Box>
                  <Typography variant="caption" sx={{ fontWeight: 700, color: 'text.disabled', textTransform: 'uppercase' }}>
                    Fecha de Inicio
                  </Typography>
                  <Typography variant="h6" sx={{ fontWeight: 600 }}>
                    {activity.startDate ? new Date(activity.startDate).toLocaleDateString(undefined, { dateStyle: 'long' }) : 'N/A'}
                  </Typography>
                </Box>
                <Box>
                  <Typography variant="caption" sx={{ fontWeight: 700, color: 'text.disabled', textTransform: 'uppercase' }}>
                    Fecha de Finalización
                  </Typography>
                  <Typography variant="h6" sx={{ fontWeight: 600 }}>
                    {activity.endDate ? new Date(activity.endDate).toLocaleDateString(undefined, { dateStyle: 'long' }) : 'N/A'}
                  </Typography>
                </Box>
              </Stack>
            </Grid>

            <Grid size={{ xs: 12, md: 6 }}>
              <Stack direction="row" spacing={2} sx={{ alignItems: 'center', mb: 3 }}>
                <Avatar sx={{ bgcolor: 'rgba(83, 83, 238, 0.1)', color: 'primary.main' }}>
                  <MeetingRoomIcon />
                </Avatar>
                <Typography variant="h6" sx={{ fontWeight: 800 }}>
                  Ubicación
                </Typography>
              </Stack>
              
              {activity.space ? (
                <Stack spacing={3}>
                  <Box>
                    <Typography variant="caption" sx={{ fontWeight: 700, color: 'text.disabled', textTransform: 'uppercase' }}>
                      Espacio asignado
                    </Typography>
                    <Typography variant="h6" sx={{ fontWeight: 600 }}>
                      {activity.space.name}
                    </Typography>
                  </Box>
                  {activity.space.location && (
                    <Box>
                      <Typography variant="caption" sx={{ fontWeight: 700, color: 'text.disabled', textTransform: 'uppercase' }}>
                        Dirección/Piso
                      </Typography>
                      <Typography variant="body1" sx={{ fontWeight: 500, color: 'text.secondary' }}>
                        {activity.space.location}
                      </Typography>
                    </Box>
                  )}
                  {activity.space.capacity && (
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                      <InfoIcon fontSize="small" color="primary" />
                      <Typography variant="body2" sx={{ fontWeight: 700 }}>
                        Capacidad: {activity.space.capacity} personas
                      </Typography>
                    </Box>
                  )}
                </Stack>
              ) : (
                <Typography variant="body1" sx={{ color: 'text.disabled', fontStyle: 'italic' }}>
                  No se ha asignado un espacio específico.
                </Typography>
              )}
            </Grid>
          </Grid>
        </Stack>
      </Paper>
    </Box>
  );
};

export default ActivitieDetailScreen;

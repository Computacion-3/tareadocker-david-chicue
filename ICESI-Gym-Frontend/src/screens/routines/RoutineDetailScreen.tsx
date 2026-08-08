import React, { useEffect, useState } from 'react';
import { 
  Box, 
  Typography, 
  Paper, 
  Button, 
  CircularProgress, 
  Alert, 
  Divider,
  List,
  ListItem,
  ListItemText,
  ListItemIcon,
  Chip
} from '@mui/material';
import { useParams, useNavigate } from 'react-router-dom';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import FitnessCenterIcon from '@mui/icons-material/FitnessCenter';
import { getRoutineById, type RoutineWithExercises } from '../../services/RoutineService';

const RoutineDetailScreen: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [routine, setRoutine] = useState<RoutineWithExercises | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    let isMounted = true;
    const fetchRoutine = async () => {
      if (!id) {return;}
      const result = await getRoutineById(Number(id));
      if (isMounted) {
        if (result.error) {
          setError(result.message || 'Error al cargar la rutina');
        } else if (result.data) {
          setRoutine({
            ...result.data,
            routineExercises: result.data.routineExercises ?? []
          } as RoutineWithExercises);
        }
        setLoading(false);
      }
    };

    fetchRoutine();
    return () => { isMounted = false; };
  }, [id]);

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', py: 8 }}>
        <CircularProgress />
      </Box>
    );
  }

  if (error || !routine) {
    return (
      <Box sx={{ py: 4 }}>
        <Alert severity="error">{error || 'Rutina no encontrada'}</Alert>
        <Button 
          startIcon={<ArrowBackIcon />} 
          onClick={() => navigate('/routines')}
          sx={{ mt: 2 }}
        >
          Volver a rutinas
        </Button>
      </Box>
    );
  }

  return (
    <Box>
      <Button 
        startIcon={<ArrowBackIcon />} 
        onClick={() => navigate('/routines')}
        sx={{ mb: 3 }}
      >
        Volver a rutinas
      </Button>

      <Paper elevation={3} sx={{ p: 4, borderRadius: 4 }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', mb: 2 }}>
          <Box>
            <Typography variant="h3" sx={{ fontWeight: 'bold', color: 'primary.main' }}>
              {routine.name}
            </Typography>
            <Typography variant="body1" sx={{ mt: 1, fontSize: '1.1rem' }}>
              {routine.description || 'Sin descripción detallada.'}
            </Typography>
          </Box>
          {routine.isPredesigned && (
            <Chip label="Plan Oficial" color="secondary" sx={{ fontWeight: 'bold' }} />
          )}
        </Box>

        <Divider sx={{ my: 4 }} />

        <Typography variant="h5" sx={{ fontWeight: 'bold', mb: 3 }}>
          Ejercicios de la Rutina
        </Typography>

        {routine.routineExercises && routine.routineExercises.length > 0 ? (
          <List>
            {routine.routineExercises
              .sort((a, b) => (a.exerciseOrder || 0) - (b.exerciseOrder || 0))
              .map((item, index) => (
                <Paper key={index} variant="outlined" sx={{ mb: 2, borderRadius: 2 }}>
                  <ListItem>
                    <ListItemIcon>
                      <FitnessCenterIcon color="primary" />
                    </ListItemIcon>
                    <ListItemText
                        primary={
                          <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                            {item.exercise.name}
                          </Typography>
                        }
                        secondary={
                          <Box sx={{ mt: 1 }} component="span">
                            <Typography variant="body2" color="text.primary" component="span">
                              <strong>Series:</strong> {item.sets || 'N/A'} |
                              <strong> Repeticiones:</strong> {item.targetReps || 'N/A'}
                            </Typography>
                            <Typography variant="caption" color="text.secondary" component="span" sx={{ display: 'block' }}>
                              Tipo: {item.exercise.type} | Dificultad: {item.exercise.difficulty || 'Media'}
                            </Typography>
                          </Box>
                        }
                        slotProps={{
                          secondary: { component: 'span' }
                        }}
                    />
                  </ListItem>
                </Paper>
              ))}
          </List>
        ) : (
          <Alert severity="warning">Esta rutina aún no tiene ejercicios asignados.</Alert>
        )}
      </Paper>
    </Box>
  );
};

export default RoutineDetailScreen;

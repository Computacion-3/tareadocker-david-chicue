import React, {useEffect, useState, useCallback} from 'react';
import {
    Box,
    Typography,
    Button,
    CircularProgress,
    Alert,
    Chip,
    Stack
} from '@mui/material';
import {useNavigate} from 'react-router-dom';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import Grid from '@mui/material/Grid';
import AddIcon from '@mui/icons-material/Add';
import CalendarMonthIcon from '@mui/icons-material/CalendarMonth';
import FitnessCenterIcon from '@mui/icons-material/FitnessCenter';
import {getAllRoutines, createRoutine, adoptRoutine} from '../../services/RoutineService';
import {getAllExercises, createExercise} from '../../services/ExerciseService';
import {assignExercisesToRoutine} from '../../services/RoutineExerciseService';
import type {RoutineResponse, RoutineRequest, ExerciseResponse, ExerciseRequest} from '../../types/api.types';
import type {RoutineExerciseDraft} from '../../utils/routine.utils';
import EntityCard from '../../components/common/EntityCard';
import RoutineTypeChoiceDialog from '../../components/routines/RoutineTypeChoiceDialog';
import RoutineFormDialog from '../../components/routines/RoutineFormDialog';
import TemplatePickerDialog from '../../components/routines/TemplatePickerDialog';
import useAuth from '../../hooks/useAuth';
import AutoFixHighIcon from '@mui/icons-material/AutoFixHigh';

const RoutinesScreen: React.FC = () => {
    const [routines, setRoutines] = useState<RoutineResponse[]>([]);
    const [exercises, setExercises] = useState<ExerciseResponse[]>([]);
    const [loading, setLoading] = useState(true);
    const [, setError] = useState<string | null>(null);
    const [adopting, setAdopting] = useState<number | null>(null);
    const [openTypeChoice, setOpenTypeChoice] = useState(false);
    const [openForm, setOpenForm] = useState(false);
    const [openTemplatePicker, setOpenTemplatePicker] = useState(false);
    const navigate = useNavigate();
    const {user} = useAuth();
    const isTrainer = user?.authorities?.includes('ROLE_TRAINER') || user?.authorities?.includes('TRAINER') ||
        user?.authorities?.includes('ROLE_ADMIN') || user?.authorities?.includes('ADMIN');

    const fetchRoutines = useCallback(async () => {
        setLoading(true);
        const result = await getAllRoutines();
        if (result.error) {
            setError(result.message || 'Error al cargar rutinas');
        } else if (result.data) {
            setRoutines(result.data);
        }
        setLoading(false);
    }, []);

    const fetchExercises = useCallback(async () => {
        const result = await getAllExercises();
        if (!result.error && result.data) {
            setExercises(result.data);
        }
    }, []);

    useEffect(() => {
        void (async () => {
            await fetchRoutines();
            await fetchExercises();
        })();
    }, [fetchRoutines, fetchExercises]);

    const handleSaveRoutine = async (payload: RoutineRequest, drafts: RoutineExerciseDraft[]) => {
        const result = await createRoutine(payload);
        if (result.error) {
            alert(result.message || 'Error al crear rutina');
            return;
        }

        if (result.data && drafts.length > 0) {
            const exerciseIds = drafts.map(d => d.exerciseId);
            await assignExercisesToRoutine(result.data.idRoutine, exerciseIds);
        }

        setOpenForm(false);
        fetchRoutines();
    };

    const handleAdoptRoutine = async (e: React.MouseEvent, id: number) => {
        e.stopPropagation();
        setAdopting(id);
        const result = await adoptRoutine(id);
        if (result.error) {
            alert(result.message);
        } else {
            alert('Rutina adoptada con éxito. Ahora la verás en tu lista personal.');
            await fetchRoutines();
        }
        setAdopting(null);
    };

    const handleCreateCustomExercise = async (payload: ExerciseRequest) => {
        const result = await createExercise(payload);
        if (result.error) {
            alert(result.message || 'Error al crear ejercicio');
        } else {
            fetchExercises();
        }
    };

    if (loading) {
        return (
            <Box sx={{display: 'flex', justifyContent: 'center', py: 8}}>
                <CircularProgress thickness={5}/>
            </Box>
        );
    }

    return (
        <Box>
            <Stack direction="row" sx={{justifyContent: 'space-between', alignItems: 'flex-start', mb: 5}}>
                <Box>
                    <Typography variant="h4" sx={{fontWeight: 800, letterSpacing: '-1px'}}>
                        Catálogo de Rutinas
                    </Typography>
                    <Typography variant="body1" color="text.secondary" sx={{mt: 1, fontWeight: 500}}>
                        Explora las rutinas prediseñadas o crea tu propio plan de entrenamiento.
                    </Typography>
                </Box>
                <Button
                    variant="contained"
                    color="primary"
                    startIcon={<AddIcon/>}
                    sx={{borderRadius: 2, fontWeight: 700, px: 3, py: 1.2}}
                    onClick={() => setOpenTypeChoice(true)}
                >
                    Nueva Rutina
                </Button>
            </Stack>

            {routines.length === 0 ? (
                <Alert severity="info" variant="outlined" sx={{borderRadius: 3}}>No tienes rutinas creadas. ¡Empieza
                    creando una nueva!</Alert>
            ) : (
                <Grid container spacing={4}>
                    {routines.map((routine) => {
                      const isOwn = routine.userId === user?.idUser;
                      const alreadyAdopted = routines.some(r => r.userId === user?.idUser && r.name === `${routine.name} (Adoptada)`);

                      return (
                        <Grid key={routine.idRoutine} size={{ xs: 12, sm: 6, md: 4 }}>
                          <Box sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
                              <EntityCard
                                  onClick={() => navigate(`/routines/${routine.idRoutine}`)}
                              >
                                  <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', mb: 1 }}>
                                      <Typography variant="h6" sx={{ fontWeight: 800, color: 'primary.main', letterSpacing: '-0.3px' }}>
                                      {routine.name}
                                      </Typography>
                                      {routine.isPredesigned && (
                                      <Chip label="Oficial" color="secondary" size="small" sx={{ fontWeight: 700, borderRadius: 1.5 }} />
                                      )}
                                  </Box>

                                  <Typography variant="body2" color="text.secondary" sx={{ flexGrow: 1, minHeight: '3em', fontWeight: 500 }}>
                                  {routine.description || 'Sin descripción disponible.'}
                                  </Typography>

                                  <Stack spacing={1} sx={{ mt: 3 }}>
                                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                                      <CalendarMonthIcon fontSize="small" sx={{ color: 'text.disabled' }} />
                                      <Typography variant="caption" sx={{ fontWeight: 600, color: 'text.secondary' }}>
                                      {new Date(routine.creationDate).toLocaleDateString()}
                                      </Typography>
                                  </Box>
                                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                                      <FitnessCenterIcon fontSize="small" sx={{ color: 'text.disabled' }} />
                                      <Typography variant="caption" sx={{ fontWeight: 600, color: 'text.secondary' }}>
                                      Ver detalles del entrenamiento
                                      </Typography>
                                  </Box>
                                  </Stack>
                              </EntityCard>
                              {routine.isPredesigned && !isOwn && (
                                  <Button
                                      fullWidth
                                      variant={alreadyAdopted ? "text" : "outlined"}
                                      color={alreadyAdopted ? "success" : "secondary"}
                                      startIcon={adopting === routine.idRoutine ? <CircularProgress size={16} /> : (alreadyAdopted ? <CheckCircleIcon /> : <AutoFixHighIcon />)}
                                      disabled={adopting === routine.idRoutine || alreadyAdopted}
                                      onClick={(e) => handleAdoptRoutine(e, routine.idRoutine)}
                                      sx={{ mt: 1, borderRadius: 2, fontWeight: 700, textTransform: 'none' }}
                                  >
                                      {alreadyAdopted ? 'Ya adoptada' : 'Adoptar esta rutina'}
                                  </Button>
                              )}
                          </Box>
                        </Grid>
                      );
                    })}
                </Grid>
            )}

            {/* Creation Dialogs */}
            <RoutineTypeChoiceDialog
                open={openTypeChoice}
                onClose={() => setOpenTypeChoice(false)}
                onCreateBlank={() => {
                    setOpenTypeChoice(false);
                    setOpenForm(true);
                }}
                onChooseTemplate={() => {
                    setOpenTypeChoice(false);
                    setOpenTemplatePicker(true);
                }}
            />

            <RoutineFormDialog
                open={openForm}
                routine={null}
                exercises={exercises}
                routineExercises={[]}
                currentUserId={user?.idUser || null}
                isTrainer={!!isTrainer}
                onClose={() => setOpenForm(false)}
                onSave={handleSaveRoutine}
                onCreateCustomExercise={handleCreateCustomExercise}
            />

            <TemplatePickerDialog
                open={openTemplatePicker}
                templates={routines.filter(r => r.isPredesigned)}
                onClose={() => setOpenTemplatePicker(false)}
                onConfirm={(_templateId: number) => {
                    setOpenTemplatePicker(false);
                    setOpenForm(true);
                }}
            />
        </Box>
    );
};

export default RoutinesScreen;

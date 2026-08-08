import { useCallback, useEffect, useMemo, useState } from "react";
import {
  Alert,
  Box,
  Button,
  CircularProgress,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  FormControl,
  InputLabel,
  MenuItem,
  Paper,
  Select,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import Grid from "@mui/material/Grid";
import {
  createExercise,
  deleteExercise,
  getAllExercises,
  updateExercise,
} from "../../services/ExerciseService";
import type { ExerciseRequest, ExerciseResponse } from "../../types/api.types";
import useAuth from "../../hooks/useAuth";
import { useAppDispatch } from "../../hooks/useDispatch";
import { showSnackbar } from "../../store/slices/uiSlice";
import ExerciseCard from "../../components/exercises/ExerciseCard";
import ExerciseDetailsDialog from "../../components/exercises/ExerciseDetailsDialog";
import {
  getCurrentUserId,
  getRoutineRoleFlags,
} from "../../utils/routine.utils";
import {
  getVisibleExercises,
  buildExercisePayload,
} from "../../utils/exercise.utils";

const ExerciseManagementScreen = () => {
  const { user: authUser } = useAuth();

  const currentUserId = useMemo(
    () => getCurrentUserId(authUser),
    [authUser],
  );

  const { isTrainer, isAdmin } = useMemo(
    () => getRoutineRoleFlags(authUser),
    [authUser],
  );
  
  const dispatch = useAppDispatch();
  const [exercises, setExercises] = useState<ExerciseResponse[]>([]);
  const [loading, setLoading] = useState(true);

  const [openCreate, setOpenCreate] = useState(false);
  const [selectedExercise, setSelectedExercise] = useState<ExerciseResponse | null>(null);

  const [name, setName] = useState("");
  const [type, setType] = useState("");
  const [description, setDescription] = useState("");
  const [durationMin, setDurationMin] = useState("");
  const [difficulty, setDifficulty] = useState("");
  const [videoUrl, setVideoUrl] = useState("");

  const [search, setSearch] = useState("");
  const [difficultyFilter, setDifficultyFilter] = useState("");
  const [predefinedFilter, setPredefinedFilter] = useState("");

  const notify = useCallback(
    (message: string, severity: "success" | "error") => {
      dispatch(showSnackbar({ message, severity }));
    },
    [dispatch],
  );

  const loadData = useCallback(async () => {
    try {
      await Promise.resolve(); // Satisfy react-hooks/set-state-in-effect
      setLoading(true);
      const res = await getAllExercises();

      if (res.error) {
        notify(
          res.message || "No se pudo cargar la información de ejercicios",
          "error",
        );
        return;
      }

      setExercises(res.data ?? []);
    } catch {
      notify("No se pudo cargar la información de ejercicios", "error");
    } finally {
      setLoading(false);
    }
  }, [notify]);

  useEffect(() => {
    const t = setTimeout(() => {
        void loadData();
    }, 0);
    return () => clearTimeout(t);
  }, [loadData]);

  const resetCreateForm = useCallback(() => {
    setName("");
    setType("");
    setDescription("");
    setDurationMin("");
    setDifficulty("");
    setVideoUrl("");
  }, []);

  const handleOpenCreate = useCallback(() => {
    resetCreateForm();
    setOpenCreate(true);
  }, [resetCreateForm]);

  const handleCloseCreate = useCallback(() => {
    setOpenCreate(false);
    resetCreateForm();
  }, [resetCreateForm]);

  const handleOpenDetails = useCallback((exercise: ExerciseResponse) => {
    setSelectedExercise(exercise);
  }, []);

  const handleCloseDetails = useCallback(() => {
    setSelectedExercise(null);
  }, []);

  const handleCreateExercise = useCallback(async () => {
    if (!name.trim() || !type.trim()) {
      notify("Nombre y tipo son obligatorios", "error");
      return;
    }

    const payload = buildExercisePayload({
      name,
      type,
      description,
      durationMin,
      difficulty,
      videoUrl,
      currentUserId,
      isTrainer,
    });

    try {
      const res = await createExercise(payload);

      if (res.error) {
        notify(res.message || "No se pudo crear el ejercicio", "error");
        return;
      }

      notify("Ejercicio creado correctamente", "success");
      handleCloseCreate();
      await loadData();
    } catch {
      notify("No se pudo crear el ejercicio", "error");
    }
  }, [
    name,
    type,
    description,
    durationMin,
    difficulty,
    videoUrl,
    currentUserId,
    isTrainer,
    notify,
    handleCloseCreate,
    loadData,
  ]);

  const handleSaveEditedExercise = useCallback(
    async (id: number, payload: ExerciseRequest) => {
      try {
        const res = await updateExercise(id, payload);

        if (res.error) {
          notify(res.message || "No se pudo actualizar el ejercicio", "error");
          return;
        }

        notify("Ejercicio actualizado correctamente", "success");
        handleCloseDetails();
        await loadData();
      } catch {
        notify("No se pudo actualizar el ejercicio", "error");
      }
    },
    [notify, handleCloseDetails, loadData],
  );

  const handleDeleteExercise = useCallback(
    async (id: number) => {
      try {
        const res = await deleteExercise(id);

        if (res.error) {
          notify(res.message || "No se pudo eliminar el ejercicio", "error");
          return;
        }

        notify("Ejercicio eliminado correctamente", "success");
        handleCloseDetails();
        await loadData();
      } catch {
        notify("No se pudo eliminar el ejercicio", "error");
      }
    },
    [notify, handleCloseDetails, loadData],
  );

  const filteredExercises = useMemo(() => {
    const allowedExercises = getVisibleExercises(
      exercises,
      currentUserId,
      isAdmin || isTrainer,
    );

    const searchValue = search.trim().toLowerCase();

    return allowedExercises
      .filter((exercise) => {
        const matchesSearch =
          !searchValue ||
          exercise.name.toLowerCase().includes(searchValue) ||
          exercise.type.toLowerCase().includes(searchValue) ||
          (exercise.description ?? "").toLowerCase().includes(searchValue);

        const matchesDifficulty =
          !difficultyFilter || exercise.difficulty === difficultyFilter;

        const matchesPredefined =
          !predefinedFilter ||
          String(Boolean(exercise.isPredefined)) === predefinedFilter;

        return matchesSearch && matchesDifficulty && matchesPredefined;
      })
      .sort((a, b) => a.name.localeCompare(b.name));
  }, [exercises, search, difficultyFilter, predefinedFilter, currentUserId, isAdmin, isTrainer]);

  if (loading) {
    return (
      <Stack sx={{ alignItems: "center", justifyContent: "center", py: 8 }}>
        <CircularProgress thickness={5} />
      </Stack>
    );
  }

  return (
    <Box>
      <Paper 
        elevation={0} 
        sx={{ 
            p: { xs: 2, md: 3 }, 
            borderRadius: 4, 
            border: '1px solid',
            borderColor: 'divider',
            boxShadow: '0 4px 20px 0 rgba(0,0,0,0.05)'
        }}
      >
        <Stack spacing={4}>
          <Stack
            direction={{ xs: "column", sm: "row" }}
            sx={{
              justifyContent: "space-between",
              alignItems: { sm: "center" },
            }}
            spacing={2}
          >
            <Box>
              <Typography variant="h4" sx={{ fontWeight: 800, letterSpacing: '-1px' }}>
                {isAdmin || isTrainer ? 'Gestión de ejercicios' : 'Catálogo de ejercicios'}
              </Typography>
              <Typography variant="body1" color="text.secondary" sx={{ mt: 0.5, fontWeight: 500 }}>
                {isAdmin || isTrainer ? 'Administra ejercicios predefinidos y personalizados.' : 'Explora los ejercicios disponibles para tu entrenamiento.'}
              </Typography>
            </Box>

            {(isAdmin) && (
              <Button
                variant="contained"
                startIcon={<AddIcon />}
                onClick={handleOpenCreate}
                sx={{ borderRadius: 2, fontWeight: 700, px: 3, py: 1.2 }}
              >
                Crear ejercicio
              </Button>
            )}
          </Stack>

          <Stack direction={{ xs: "column", md: "row" }} spacing={2}>
            <TextField
              fullWidth
              placeholder="Buscar por nombre, tipo o descripción..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              sx={{ flexGrow: 2 }}
            />

            <FormControl fullWidth sx={{ flexGrow: 1 }}>
              <InputLabel>Dificultad</InputLabel>
              <Select
                value={difficultyFilter}
                label="Dificultad"
                onChange={(e) => setDifficultyFilter(e.target.value)}
              >
                <MenuItem value="">Todas</MenuItem>
                <MenuItem value="Baja">Baja</MenuItem>
                <MenuItem value="Media">Media</MenuItem>
                <MenuItem value="Alta">Alta</MenuItem>
              </Select>
            </FormControl>

            <FormControl fullWidth sx={{ flexGrow: 1 }}>
              <InputLabel>Tipo</InputLabel>
              <Select
                value={predefinedFilter}
                label="Tipo"
                onChange={(e) => setPredefinedFilter(e.target.value)}
              >
                <MenuItem value="">Todos</MenuItem>
                <MenuItem value="true">Sistema (Predefinidos)</MenuItem>
                <MenuItem value="false">Usuario (Personalizados)</MenuItem>
              </Select>
            </FormControl>
          </Stack>

          {filteredExercises.length === 0 ? (
            <Alert severity="info" variant="outlined" sx={{ borderRadius: 3 }}>
              No se encontraron ejercicios con los filtros aplicados.
            </Alert>
          ) : (
            <Grid container spacing={3}>
              {filteredExercises.map((exercise) => (
                <Grid size={{ xs: 12, sm: 6, lg: 4 }} key={exercise.idExercise}>
                  <ExerciseCard
                    exercise={exercise}
                    onClick={handleOpenDetails}
                  />
                </Grid>
              ))}
            </Grid>
          )}
        </Stack>
      </Paper>

      <Dialog
        open={openCreate}
        onClose={handleCloseCreate}
        fullWidth
        maxWidth="sm"
        sx={{ '& .MuiDialog-paper': { borderRadius: 3 } }}
      >
        <DialogTitle sx={{ fontWeight: 'bold' }}>Nuevo Ejercicio</DialogTitle>

        <DialogContent dividers>
          <Stack spacing={2.5} sx={{ mt: 1 }}>
            <TextField
              label="Nombre"
              fullWidth
              value={name}
              onChange={(e) => setName(e.target.value)}
            />

            <TextField
              label="Tipo"
              fullWidth
              value={type}
              onChange={(e) => setType(e.target.value)}
            />

            <TextField
              label="Descripción"
              fullWidth
              multiline
              minRows={3}
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />

            <TextField
              label="Duración (min)"
              type="number"
              fullWidth
              value={durationMin}
              onChange={(e) => setDurationMin(e.target.value)}
            />

            <FormControl fullWidth>
              <InputLabel>Dificultad</InputLabel>
              <Select
                value={difficulty}
                label="Dificultad"
                onChange={(e) => setDifficulty(e.target.value)}
              >
                <MenuItem value="">Sin especificar</MenuItem>
                <MenuItem value="Baja">Baja</MenuItem>
                <MenuItem value="Media">Media</MenuItem>
                <MenuItem value="Alta">Alta</MenuItem>
              </Select>
            </FormControl>

            <TextField
              label="URL del video (YouTube/Vimeo)"
              fullWidth
              value={videoUrl}
              onChange={(e) => setVideoUrl(e.target.value)}
            />
          </Stack>
        </DialogContent>

        <DialogActions sx={{ p: 2.5 }}>
          <Button onClick={handleCloseCreate} sx={{ fontWeight: 600 }}>Cancelar</Button>
          <Button variant="contained" onClick={handleCreateExercise} sx={{ borderRadius: 2, fontWeight: 700, px: 3 }}>
            Crear Ejercicio
          </Button>
        </DialogActions>
      </Dialog>

      <ExerciseDetailsDialog
        open={Boolean(selectedExercise)}
        exercise={selectedExercise}
        onClose={handleCloseDetails}
        onSave={handleSaveEditedExercise}
        onDelete={handleDeleteExercise}
      />
    </Box>
  );
};

export default ExerciseManagementScreen;

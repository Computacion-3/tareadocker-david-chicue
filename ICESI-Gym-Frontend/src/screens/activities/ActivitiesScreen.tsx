import { 
  Box, 
  Typography,
  Button, 
  CircularProgress, 
  Alert,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  MenuItem,
  Stack,
  Tabs,
  Tab
} from '@mui/material';
import Grid from '@mui/material/Grid';
import AddIcon from '@mui/icons-material/Add';
import { useNavigate } from 'react-router-dom';
import { getAllActivities, createActivity, updateActivity, type ActivityWithSpace } from '../../services/ActivityService';
import { getAllSpaces } from '../../services/SpaceService';
import { createEnrollment, getEnrollmentsByUserId, deleteEnrollment } from '../../services/EnrollmentService';
import type { ActivityRequest, SpaceResponse, EnrollmentResponse } from '../../types/api.types';
import useAuth from '../../hooks/useAuth';
import React, {useState, useEffect} from "react";
import ActivityCard from '../../components/activities/ActivityCard';

const ActivitiesScreen: React.FC = () => {
  const [activities, setActivities] = useState<ActivityWithSpace[]>([]);
  const [spaces, setSpaces] = useState<SpaceResponse[]>([]);
  const [enrollments, setEnrollments] = useState<EnrollmentResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [tabValue, setTabValue] = useState(0);

  const navigate = useNavigate();
  const { user } = useAuth();
  const isAdmin = user?.authorities?.includes('ROLE_ADMIN') || user?.authorities?.includes('ADMIN');

  // Dialog state
  const [open, setOpen] = useState(false);
  const [editingActivity, setEditingActivity] = useState<ActivityWithSpace | null>(null);
  const [formData, setFormData] = useState<ActivityRequest>({
    name: '',
    description: '',
    startDate: new Date().toISOString().split('T')[0],
    endDate: new Date().toISOString().split('T')[0],
    spaceId: undefined
  });

  const loadData = async () => {
    setLoading(true);
    const [actResult, spaceResult] = await Promise.all([
      getAllActivities(),
      getAllSpaces()
    ]);

    if (actResult.error) {
      setError(actResult.message || 'Error al cargar actividades');
    } else if (actResult.data) {
      setActivities(actResult.data);
    }

    if (!spaceResult.error && spaceResult.data) {
      setSpaces(spaceResult.data);
    }

    if (user?.idUser) {
      const enrollResult = await getEnrollmentsByUserId(user.idUser);
      if (!enrollResult.error && enrollResult.data) {
        setEnrollments(enrollResult.data);
      }
    }
    setLoading(false);
  };

  useEffect(() => {
    void (async () => { await loadData(); })();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handleOpen = (activity?: ActivityWithSpace) => {
    if (activity) {
      setEditingActivity(activity);
      setFormData({
        name: activity.name,
        description: activity.description || '',
        startDate: activity.startDate.split('T')[0],
        endDate: activity.endDate.split('T')[0],
        spaceId: activity.spaceId
      });
    } else {
      setEditingActivity(null);
      setFormData({
        name: '',
        description: '',
        startDate: new Date().toISOString().split('T')[0],
        endDate: new Date().toISOString().split('T')[0],
        spaceId: undefined
      });
    }
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    setEditingActivity(null);
  };

  const handleSubmit = async () => {
    let result;
    if (editingActivity) {
      result = await updateActivity(editingActivity.idActivity, formData);
    } else {
      result = await createActivity(formData);
    }

    if (result.error) {
      alert(result.message);
    } else {
      handleClose();
      loadData();
    }
  };

  const handleEnroll = async (activity: ActivityWithSpace) => {
    const currentUserId = user?.idUser;
    if (!currentUserId) {
        alert('Error: No se pudo identificar al usuario. Por favor, inicia sesión de nuevo.');
        return;
    }
    const res = await createEnrollment({
        userId: currentUserId,
        activityId: activity.idActivity,
        enrollmentDate: new Date().toISOString().split('T')[0]
    });
    if (res.error) {
        alert(res.message);
    } else {
        loadData();
    }
  };

  const handleCancelEnroll = async (activity: ActivityWithSpace) => {
    if (!user?.idUser) {return;}
    if (window.confirm(`¿Deseas cancelar tu inscripción a ${activity.name}?`)) {
        const res = await deleteEnrollment(user.idUser, activity.idActivity);
        if (res.error) {
            alert(res.message);
        } else {
            loadData();
        }
    }
  };

  const isEnrolled = (activityId: number) => {
      return enrollments.some(e => e.activityId === activityId);
  };

  const filteredActivities = tabValue === 0 
    ? activities 
    : activities.filter(a => isEnrolled(a.idActivity));

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', py: 8 }}>
        <CircularProgress thickness={5} />
      </Box>
    );
  }

  return (
    <Box>
      <Stack direction="row" sx={{ justifyContent: 'space-between', alignItems: 'flex-start', mb: 4 }}>
        <Box>
            <Typography variant="h4" sx={{ fontWeight: 800, letterSpacing: '-1px' }}>
            Actividades
            </Typography>
            <Typography variant="body1" color="text.secondary" sx={{ mt: 1, fontWeight: 500 }}>
            Inscríbete en nuestras clases grupales y eventos especiales.
            </Typography>
        </Box>
        {isAdmin && (
          <Button 
            variant="contained" 
            startIcon={<AddIcon />} 
            onClick={() => handleOpen()}
            sx={{ borderRadius: 2, fontWeight: 700, px: 3, py: 1.2 }}
          >
            Nueva Actividad
          </Button>
        )}
      </Stack>

      {!isAdmin && (
        <Box sx={{ borderBottom: 1, borderColor: 'divider', mb: 4 }}>
            <Tabs value={tabValue} onChange={(_, newValue) => setTabValue(newValue)}>
            <Tab label="Todas las Actividades" sx={{ fontWeight: 700 }} />
            <Tab label="Mis Actividades" sx={{ fontWeight: 700 }} />
            </Tabs>
        </Box>
      )}

      {error && <Alert severity="error" variant="outlined" sx={{ mb: 3, borderRadius: 3 }}>{error}</Alert>}

      {filteredActivities.length === 0 ? (
        <Alert severity="info" variant="outlined" sx={{ borderRadius: 3 }}>
          {tabValue === 0 ? "No hay actividades disponibles." : "No estás inscrito en ninguna actividad todavía."}
        </Alert>
      ) : (
        <Grid container spacing={4}>
          {filteredActivities.map((activity) => (
            <Grid key={activity.idActivity} size={{ xs: 12, sm: 6, md: 4 }}>
              <ActivityCard 
                activity={activity} 
                isAdmin={isAdmin} 
                isEnrolled={isEnrolled(activity.idActivity)}
                onClickDetails={(act) => navigate(`/activities/${act.idActivity}`)}
                onEnroll={handleEnroll}
                onCancelEnroll={handleCancelEnroll}
              />
            </Grid>
          ))}
        </Grid>
      )}

      {/* Create/Edit Dialog */}
      <Dialog open={open} onClose={handleClose} fullWidth maxWidth="sm">
        <DialogTitle sx={{ fontWeight: 800 }}>{editingActivity ? 'Editar Actividad' : 'Nueva Actividad'}</DialogTitle>
        <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: 2, pt: 2 }}>
          <TextField
            label="Nombre"
            fullWidth
            value={formData.name}
            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
          />
          <TextField
            label="Descripción"
            multiline
            rows={3}
            fullWidth
            value={formData.description}
            onChange={(e) => setFormData({ ...formData, description: e.target.value })}
          />
          <TextField
            label="Fecha Inicio"
            type="date"
            fullWidth
            slotProps={{ inputLabel: { shrink: true } }}
            value={formData.startDate}
            onChange={(e) => setFormData({ ...formData, startDate: e.target.value })}
          />
          <TextField
            label="Fecha Fin"
            type="date"
            fullWidth
            slotProps={{ inputLabel: { shrink: true } }}
            value={formData.endDate}
            onChange={(e) => setFormData({ ...formData, endDate: e.target.value })}
          />
          <TextField
            label="Espacio"
            select
            fullWidth
            value={formData.spaceId || ''}
            onChange={(e) => setFormData({ ...formData, spaceId: Number(e.target.value) })}
          >
            {spaces.map((space) => (
              <MenuItem key={space.idSpace} value={space.idSpace}>
                {space.name}
              </MenuItem>
            ))}
          </TextField>
        </DialogContent>
        <DialogActions sx={{ p: 3 }}>
          <Button onClick={handleClose} sx={{ fontWeight: 600 }}>Cancelar</Button>
          <Button onClick={handleSubmit} variant="contained" color="primary" sx={{ fontWeight: 700, borderRadius: 2 }}>
            {editingActivity ? 'Guardar Cambios' : 'Crear'}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default ActivitiesScreen;

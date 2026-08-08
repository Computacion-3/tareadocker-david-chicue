import React, { useEffect, useState, useCallback } from 'react';
import { 
  Box, 
  Typography, 
  Paper, 
  CircularProgress, 
  Alert,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  Chip,
  Stack,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  MenuItem,
  IconButton
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import { getAllSchedules, createSchedule, updateSchedule, deleteSchedule, type ScheduleWithActivity } from '../../services/ScheduleService';
import { getAllActivities } from '../../services/ActivityService';
import type { ScheduleRequest, ActivityResponse } from '../../types/api.types';
import useAuth from '../../hooks/useAuth';

const SchedulesScreen: React.FC = () => {
  const [schedules, setSchedules] = useState<ScheduleWithActivity[]>([]);
  const [activities, setActivities] = useState<ActivityResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const { user } = useAuth();
  const isAdmin = user?.authorities?.includes('ROLE_ADMIN') || user?.authorities?.includes('ADMIN');

  // Dialog state
  const [open, setOpen] = useState(false);
  const [editingSchedule, setEditingSchedule] = useState<ScheduleWithActivity | null>(null);
  const [formData, setFormData] = useState<ScheduleRequest>({
    activityId: 0,
    dayOfWeek: 'MONDAY',
    startTime: '08:00:00',
    endTime: '10:00:00'
  });

  const loadData = useCallback(async () => {
    setLoading(true);
    const [schedResult, actResult] = await Promise.all([
      getAllSchedules(),
      getAllActivities()
    ]);

    if (schedResult.error) {
      setError(schedResult.message || 'Error al cargar horarios');
    } else if (schedResult.data) {
      setSchedules(schedResult.data);
    }

    if (!actResult.error && actResult.data) {
      setActivities(actResult.data);
    }
    setLoading(false);
  }, []);

  useEffect(() => {
    void (async () => { await loadData(); })();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handleOpen = (schedule?: ScheduleWithActivity) => {
    if (schedule) {
      setEditingSchedule(schedule);
      setFormData({
        activityId: schedule.activityId,
        dayOfWeek: schedule.dayOfWeek,
        startTime: schedule.startTime,
        endTime: schedule.endTime
      });
    } else {
      setEditingSchedule(null);
      setFormData({
        activityId: activities[0]?.idActivity || 0,
        dayOfWeek: 'MONDAY',
        startTime: '08:00:00',
        endTime: '10:00:00'
      });
    }
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    setEditingSchedule(null);
  };

  const handleSubmit = async () => {
    if (!formData.activityId) {
        alert("Selecciona una actividad");
        return;
    }

    let result;
    if (editingSchedule) {
      result = await updateSchedule(editingSchedule.idSchedule, formData);
    } else {
      result = await createSchedule(formData);
    }

    if (result.error) {
      alert(result.message);
    } else {
      handleClose();
      loadData();
    }
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('¿Deseas eliminar este horario?')) {
      const result = await deleteSchedule(id);
      if (result.error) {
        alert(result.message);
      } else {
        loadData();
      }
    }
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', py: 8 }}>
        <CircularProgress thickness={5} />
      </Box>
    );
  }

  const daysMapping: Record<string, string> = {
    'MONDAY': 'Lunes',
    'TUESDAY': 'Martes',
    'WEDNESDAY': 'Miércoles',
    'THURSDAY': 'Jueves',
    'FRIDAY': 'Viernes',
    'SATURDAY': 'Sábado',
    'SUNDAY': 'Domingo'
  };

  const daysOrder = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'];

  return (
    <Box>
      <Stack direction="row" sx={{ justifyContent: 'space-between', alignItems: 'flex-start', mb: 4 }}>
        <Box>
            <Typography variant="h4" sx={{ fontWeight: 800, letterSpacing: '-1px' }}>
            Horarios de Actividades
            </Typography>
            <Typography variant="body1" color="text.secondary" sx={{ mt: 1, fontWeight: 500 }}>
            Consulta la programación semanal de las clases y actividades en el centro.
            </Typography>
        </Box>
        {isAdmin && (
          <Button 
            variant="contained" 
            startIcon={<AddIcon />} 
            onClick={() => handleOpen()}
            sx={{ borderRadius: 2, fontWeight: 700, px: 3, py: 1.2 }}
          >
            Nuevo Horario
          </Button>
        )}
      </Stack>

      {error && <Alert severity="error" variant="outlined" sx={{ mb: 3, borderRadius: 3 }}>{error}</Alert>}

      <Paper 
        elevation={0} 
        sx={{ 
          borderRadius: 4, 
          overflow: 'hidden', 
          border: '1px solid',
          borderColor: 'divider',
          boxShadow: '0 4px 20px 0 rgba(0,0,0,0.05)'
        }}
      >
        <Table sx={{ minWidth: 650 }}>
          <TableHead sx={{ bgcolor: 'rgba(83, 83, 238, 0.05)' }}>
            <TableRow>
              <TableCell sx={{ fontWeight: 700 }}>Día</TableCell>
              <TableCell sx={{ fontWeight: 700 }}>Actividad</TableCell>
              <TableCell sx={{ fontWeight: 700 }}>Hora Inicio</TableCell>
              <TableCell sx={{ fontWeight: 700 }}>Hora Fin</TableCell>
              <TableCell sx={{ fontWeight: 700 }}>Lugar</TableCell>
              {isAdmin && <TableCell align="right" sx={{ fontWeight: 700 }}>Acciones</TableCell>}
            </TableRow>
          </TableHead>
          <TableBody>
            {schedules.length === 0 ? (
              <TableRow>
                <TableCell colSpan={isAdmin ? 6 : 5} align="center" sx={{ py: 6 }}>
                    <Typography variant="body2" color="text.secondary" sx={{ fontWeight: 500 }}>
                        No hay horarios programados en este momento.
                    </Typography>
                </TableCell>
              </TableRow>
            ) : (
              schedules
                .sort((a, b) => daysOrder.indexOf(a.dayOfWeek) - daysOrder.indexOf(b.dayOfWeek))
                .map((schedule) => (
                  <TableRow key={schedule.idSchedule} hover>
                    <TableCell>
                      <Chip 
                        label={daysMapping[schedule.dayOfWeek] || schedule.dayOfWeek} 
                        color="primary" 
                        variant="outlined" 
                        size="small" 
                        sx={{ fontWeight: 700, borderRadius: 1.5 }}
                      />
                    </TableCell>
                    <TableCell sx={{ fontWeight: 700, color: 'primary.main' }}>
                        {schedule.activity?.name || 'N/A'}
                    </TableCell>
                    <TableCell sx={{ fontWeight: 500 }}>{schedule.startTime}</TableCell>
                    <TableCell sx={{ fontWeight: 500 }}>{schedule.endTime}</TableCell>
                    <TableCell>
                      <Stack direction="row" spacing={1} sx={{ alignItems: 'center' }}>
                        <Typography variant="body2" sx={{ fontWeight: 600 }}>
                            {schedule.activity?.space?.name || 'Por definir'}
                        </Typography>
                        {schedule.activity?.space?.location && (
                            <Typography variant="caption" color="text.secondary">
                                ({schedule.activity.space.location})
                            </Typography>
                        )}
                      </Stack>
                    </TableCell>
                    {isAdmin && (
                      <TableCell align="right">
                        <IconButton size="small" onClick={() => handleOpen(schedule)} color="primary">
                          <EditIcon fontSize="small" />
                        </IconButton>
                        <IconButton size="small" onClick={() => handleDelete(schedule.idSchedule)} color="error">
                          <DeleteIcon fontSize="small" />
                        </IconButton>
                      </TableCell>
                    )}
                  </TableRow>
                ))
            )}
          </TableBody>
        </Table>
      </Paper>

      {/* Create/Edit Dialog */}
      <Dialog open={open} onClose={handleClose} fullWidth maxWidth="sm">
        <DialogTitle sx={{ fontWeight: 800 }}>{editingSchedule ? 'Editar Horario' : 'Nuevo Horario'}</DialogTitle>
        <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: 3, pt: 2 }}>
          <TextField
            label="Actividad"
            select
            fullWidth
            value={formData.activityId || ''}
            onChange={(e) => setFormData({ ...formData, activityId: Number(e.target.value) })}
          >
            {activities.map((act) => (
              <MenuItem key={act.idActivity} value={act.idActivity}>
                {act.name}
              </MenuItem>
            ))}
          </TextField>

          <TextField
            label="Día de la semana"
            select
            fullWidth
            value={formData.dayOfWeek}
            onChange={(e) => setFormData({ ...formData, dayOfWeek: e.target.value })}
          >
            {daysOrder.map((day) => (
              <MenuItem key={day} value={day}>
                {daysMapping[day]}
              </MenuItem>
            ))}
          </TextField>

          <Stack direction="row" spacing={2}>
            <TextField
                label="Hora Inicio"
                type="time"
                fullWidth
                slotProps={{ inputLabel: { shrink: true } }}
                value={formData.startTime}
                onChange={(e) => setFormData({ ...formData, startTime: e.target.value.includes(':') && e.target.value.split(':').length === 2 ? e.target.value + ':00' : e.target.value })}
            />
            <TextField
                label="Hora Fin"
                type="time"
                fullWidth
                slotProps={{ inputLabel: { shrink: true } }}
                value={formData.endTime}
                onChange={(e) => setFormData({ ...formData, endTime: e.target.value.includes(':') && e.target.value.split(':').length === 2 ? e.target.value + ':00' : e.target.value })}
            />
          </Stack>
        </DialogContent>
        <DialogActions sx={{ p: 3 }}>
          <Button onClick={handleClose} sx={{ fontWeight: 600 }}>Cancelar</Button>
          <Button onClick={handleSubmit} variant="contained" color="primary" sx={{ fontWeight: 700, borderRadius: 2 }}>
            {editingSchedule ? 'Guardar Cambios' : 'Crear'}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default SchedulesScreen;

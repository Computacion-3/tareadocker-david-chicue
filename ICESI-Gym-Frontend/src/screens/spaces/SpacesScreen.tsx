import React, { useEffect, useState, useCallback } from 'react';
import { 
  Box, 
  Typography,
  CircularProgress, 
  Alert,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Stack,
  IconButton
} from '@mui/material';
import Grid from '@mui/material/Grid';
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import { getAllSpaces, createSpace, updateSpace, deleteSpace } from '../../services/SpaceService';
import type { SpaceResponse, SpaceRequest } from '../../types/api.types';
import SpaceCard from '../../components/spaces/SpaceCard';
import useAuth from '../../hooks/useAuth';

const SpacesScreen: React.FC = () => {
  const [spaces, setSpaces] = useState<SpaceResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const { user } = useAuth();
  const isAdmin = user?.authorities?.includes('ROLE_ADMIN') || user?.authorities?.includes('ADMIN');

  // Dialog state
  const [open, setOpen] = useState(false);
  const [editingSpace, setEditingSpace] = useState<SpaceResponse | null>(null);
  const [formData, setFormData] = useState<SpaceRequest>({
    name: '',
    capacity: 0,
    location: ''
  });

  const fetchSpaces = useCallback(async () => {
    setLoading(true);
    const result = await getAllSpaces();
    if (result.error) {
      setError(result.message || 'Error al cargar espacios');
    } else if (result.data) {
      setSpaces(result.data);
    }
    setLoading(false);
  }, []);

  useEffect(() => {
    void (async () => { await fetchSpaces(); })();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handleOpen = (space?: SpaceResponse) => {
    if (space) {
      setEditingSpace(space);
      setFormData({
        name: space.name,
        capacity: space.capacity || 0,
        location: space.location || ''
      });
    } else {
      setEditingSpace(null);
      setFormData({
        name: '',
        capacity: 0,
        location: ''
      });
    }
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    setEditingSpace(null);
  };

  const handleSubmit = async () => {
    if (!formData.name) {
        alert("El nombre es obligatorio");
        return;
    }

    let result;
    if (editingSpace) {
      result = await updateSpace(editingSpace.idSpace, formData);
    } else {
      result = await createSpace(formData);
    }

    if (result.error) {
      alert(result.message);
    } else {
      handleClose();
      fetchSpaces();
    }
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('¿Deseas eliminar este espacio?')) {
      const result = await deleteSpace(id);
      if (result.error) {
        alert(result.message);
      } else {
        fetchSpaces();
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

  return (
    <Box>
      <Stack direction="row" sx={{ justifyContent: 'space-between', alignItems: 'flex-start', mb: 5 }}>
        <Box>
            <Typography variant="h4" sx={{ fontWeight: 800, letterSpacing: '-1px' }}>
            Nuestros Espacios
            </Typography>
            <Typography variant="body1" color="text.secondary" sx={{ mt: 1, fontWeight: 500 }}>
            Descubre las instalaciones diseñadas para tu mejor rendimiento.
            </Typography>
        </Box>
        {isAdmin && (
          <Button 
            variant="contained" 
            startIcon={<AddIcon />} 
            onClick={() => handleOpen()}
            sx={{ borderRadius: 2, fontWeight: 700, px: 3, py: 1.2 }}
          >
            Nuevo Espacio
          </Button>
        )}
      </Stack>

      {error && <Alert severity="error" variant="outlined" sx={{ mb: 3, borderRadius: 3 }}>{error}</Alert>}

      {spaces.length === 0 ? (
        <Alert severity="info" variant="outlined" sx={{ borderRadius: 3 }}>No hay espacios registrados.</Alert>
      ) : (
        <Grid container spacing={4}>
          {spaces.map((space) => (
            <Grid key={space.idSpace} size={{ xs: 12, sm: 6, md: 4 }}>
              <Box sx={{ position: 'relative' }}>
                <SpaceCard space={space} />
                {isAdmin && (
                  <Box sx={{ position: 'absolute', top: 8, right: 8, zIndex: 1 }}>
                    <IconButton size="small" onClick={() => handleOpen(space)} sx={{ bgcolor: 'background.paper', mb: 1, '&:hover': { bgcolor: 'primary.light', color: 'white' } }}>
                      <EditIcon fontSize="small" />
                    </IconButton>
                    <br />
                    <IconButton size="small" onClick={() => handleDelete(space.idSpace)} sx={{ bgcolor: 'background.paper', '&:hover': { bgcolor: 'error.main', color: 'white' } }}>
                      <DeleteIcon fontSize="small" />
                    </IconButton>
                  </Box>
                )}
              </Box>
            </Grid>
          ))}
        </Grid>
      )}

      {/* Create/Edit Dialog */}
      <Dialog open={open} onClose={handleClose} fullWidth maxWidth="sm">
        <DialogTitle sx={{ fontWeight: 800 }}>{editingSpace ? 'Editar Espacio' : 'Nuevo Espacio'}</DialogTitle>
        <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: 2, pt: 2 }}>
          <TextField
            label="Nombre"
            fullWidth
            value={formData.name}
            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
          />
          <TextField
            label="Ubicación"
            fullWidth
            value={formData.location || ''}
            onChange={(e) => setFormData({ ...formData, location: e.target.value })}
          />
          <TextField
            label="Capacidad"
            type="number"
            fullWidth
            value={formData.capacity}
            onChange={(e) => setFormData({ ...formData, capacity: Number(e.target.value) })}
          />
        </DialogContent>
        <DialogActions sx={{ p: 3 }}>
          <Button onClick={handleClose} sx={{ fontWeight: 600 }}>Cancelar</Button>
          <Button onClick={handleSubmit} variant="contained" color="primary" sx={{ fontWeight: 700, borderRadius: 2 }}>
            {editingSpace ? 'Guardar Cambios' : 'Crear'}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default SpacesScreen;

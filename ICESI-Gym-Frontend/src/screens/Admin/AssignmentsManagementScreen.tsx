import React, { useEffect, useState, useCallback, useMemo } from 'react';
import {
  Box,
  Typography,
  Paper,
  Button,
  CircularProgress,
  Stack,
} from '@mui/material';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import { getAllAssignments, createAssignment, deleteAssignment } from '../../services/AssignmentService';
import { getAllUsers } from '../../services/UserService';
import { getAllRoles } from '../../services/RoleService';
import { getAllUserRoles } from '../../services/UserRoleService';
import type { AssignmentResponse, AssignmentRequest, UserResponse } from '../../types/api.types';
import { useAppDispatch } from '../../hooks/useDispatch';
import { showSnackbar } from '../../store/slices/uiSlice';
import AssignmentsTable from '../../components/assignments/AssignmentsTable';
import AssignmentFormDialog from '../../components/assignments/AssignmentFormDialog';

const AssignmentsManagementScreen: React.FC = () => {
  const dispatch = useAppDispatch();

  const [assignments, setAssignments] = useState<AssignmentResponse[]>([]);
  const [users, setUsers] = useState<UserResponse[]>([]);
  const [userRoles, setUserRoles] = useState<{ userId: number; roleId: number }[]>([]);
  const [roles, setRoles] = useState<{ id: number; name: string }[]>([]);
  const [loading, setLoading] = useState(true);

  // Dialog state
  const [open, setOpen] = useState(false);
  const [formData, setFormData] = useState<AssignmentRequest>({
    userId: 0,
    trainerId: 0,
    assignmentDate: new Date().toISOString().split('T')[0]
  });

  const notify = useCallback(
    (message: string, severity: "success" | "error") => {
      dispatch(showSnackbar({ message, severity }));
    },
    [dispatch],
  );

  const fetchData = useCallback(async () => {
    try {
      const [assignmentsRes, usersRes, rolesRes, userRolesRes] = await Promise.all([
        getAllAssignments(),
        getAllUsers(),
        getAllRoles(),
        getAllUserRoles(),
      ]);

      if (assignmentsRes.error || usersRes.error) {
        notify('Error al cargar datos de asignaciones', 'error');
      } else {
        setAssignments(assignmentsRes.data || []);
        setUsers(usersRes.data || []);
        setRoles(rolesRes.data || []);
        setUserRoles(userRolesRes.data || []);
      }
    } catch {
      notify('Error inesperado al cargar datos', 'error');
    } finally {
      setLoading(false);
    }
  }, [notify]);

  useEffect(() => {
    const t = setTimeout(fetchData, 0);
    return () => clearTimeout(t);
  }, [fetchData]);

  const userMap = useMemo(() => {
    return new Map(users.map(user => [user.idUser, user]));
  }, [users]);

  const trainers = useMemo(() => {
    // Try to find roles that sound like "trainer" or "entrenador"
    const trainerRoleIds = roles
      .filter(r => r.name.toLowerCase().includes('trainer') || r.name.toLowerCase().includes('entrenador'))
      .map(r => r.id);
    
    if (trainerRoleIds.length === 0) {return users;} // Fallback to all users if no trainer role found

    const trainerUserIds = userRoles
      .filter(ur => trainerRoleIds.includes(ur.roleId))
      .map(ur => ur.userId);

    return users.filter(u => trainerUserIds.includes(u.idUser));
  }, [users, roles, userRoles]);

  const handleOpen = () => {
    setFormData({
      userId: 0,
      trainerId: 0,
      assignmentDate: new Date().toISOString().split('T')[0]
    });
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleSubmit = async () => {
    if (!formData.userId || !formData.trainerId) {
      notify('Debe seleccionar un usuario y un entrenador', 'error');
      return;
    }

    const result = await createAssignment(formData);
    if (result.error) {
      notify(result.message || 'Error al crear la asignación', 'error');
    } else {
      notify('Asignación creada correctamente', 'success');
      handleClose();
      fetchData();
    }
  };

  const handleDelete = async (userId: number, trainerId: number) => {
    if (window.confirm('¿Estás seguro de que deseas eliminar esta asignación?')) {
      const result = await deleteAssignment(userId, trainerId);
      if (result.error) {
        notify(result.message || 'Error al eliminar la asignación', 'error');
      } else {
        notify('Asignación eliminada correctamente', 'success');
        fetchData();
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
        <Stack
          direction="row"
          sx={{
            justifyContent: "space-between",
            alignItems: "center",
            mb: 3,
          }}
        >
          <Box>
            <Typography variant="h5" sx={{ fontWeight: "bold" }}>
              Gestión de Asignaciones
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Vincula usuarios con sus respectivos entrenadores personales.
            </Typography>
          </Box>

          <Button 
            variant="contained" 
            startIcon={<PersonAddIcon />} 
            onClick={handleOpen}
            sx={{ borderRadius: 2, fontWeight: 700 }}
          >
            Nueva Asignación
          </Button>
        </Stack>

        <AssignmentsTable 
          assignments={assignments} 
          userMap={userMap} 
          onDelete={handleDelete} 
        />
      </Paper>

      <AssignmentFormDialog 
        open={open}
        formData={formData}
        setFormData={setFormData}
        users={users}
        trainers={trainers}
        onClose={handleClose}
        onSave={handleSubmit}
      />
    </Box>
  );
};

export default AssignmentsManagementScreen;

import {
  Autocomplete,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Stack,
  TextField,
} from "@mui/material";
import type { UserResponse, AssignmentRequest } from "../../types/api.types";

type Props = {
  open: boolean;
  formData: AssignmentRequest;
  setFormData: (value: AssignmentRequest) => void;
  users: UserResponse[];
  trainers: UserResponse[];
  onClose: () => void;
  onSave: () => void;
};

const AssignmentFormDialog = ({
  open,
  formData,
  setFormData,
  users,
  trainers,
  onClose,
  onSave,
}: Props) => {
  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="xs" sx={{ '& .MuiDialog-paper': { borderRadius: 3 } }}>
      <DialogTitle sx={{ fontWeight: 'bold' }}>Nueva Asignación</DialogTitle>

      <DialogContent dividers>
        <Stack spacing={3} sx={{ mt: 1 }}>
          <Autocomplete
            options={users}
            getOptionLabel={(option) => `${option.firstName} ${option.lastName} (${option.institutionalEmail})`}
            value={users.find(u => u.idUser === formData.userId) || null}
            onChange={(_, newValue) => setFormData({ ...formData, userId: newValue?.idUser || 0 })}
            renderInput={(params) => <TextField {...params} label="Seleccionar Usuario" fullWidth />}
          />

          <Autocomplete
            options={trainers}
            getOptionLabel={(option) => `${option.firstName} ${option.lastName} (${option.institutionalEmail})`}
            value={trainers.find(t => t.idUser === formData.trainerId) || null}
            onChange={(_, newValue) => setFormData({ ...formData, trainerId: newValue?.idUser || 0 })}
            renderInput={(params) => <TextField {...params} label="Seleccionar Entrenador" fullWidth />}
          />

          <TextField
            label="Fecha de Asignación"
            type="date"
            fullWidth
            slotProps={{ inputLabel: { shrink: true } }}
            value={formData.assignmentDate}
            onChange={(e) => setFormData({ ...formData, assignmentDate: e.target.value })}
          />
        </Stack>
      </DialogContent>

      <DialogActions sx={{ p: 2.5 }}>
        <Button onClick={onClose} sx={{ fontWeight: 600 }}>Cancelar</Button>
        <Button
          variant="contained"
          onClick={onSave}
          disabled={!formData.userId || !formData.trainerId}
          sx={{ borderRadius: 2, fontWeight: 700, px: 3 }}
        >
          Asignar
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default AssignmentFormDialog;

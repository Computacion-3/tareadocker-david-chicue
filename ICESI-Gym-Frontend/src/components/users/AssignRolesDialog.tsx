import {
  Box,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Typography,
  Stack,
} from "@mui/material";
import type { RoleResponse, UserResponse } from "../../types/api.types";
import RolesSelector from "./RolesSelector";

type Props = {
  open: boolean;
  selectedUser: UserResponse | null;
  roles: RoleResponse[];
  selectedRoleIds: number[];
  onToggleRole: (roleId: number) => void;
  onClose: () => void;
  onSave: () => void;
};

const AssignRolesDialog = ({
  open,
  selectedUser,
  roles,
  selectedRoleIds,
  onToggleRole,
  onClose,
  onSave,
}: Props) => {
  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm" sx={{ '& .MuiDialog-paper': { borderRadius: 3 } }}>
      <DialogTitle sx={{ fontWeight: 'bold' }}>Asignar roles</DialogTitle>

      <DialogContent dividers>
        <Stack spacing={3}>
          <Box>
            <Typography variant="subtitle1" sx={{ fontWeight: "bold", color: 'primary.main' }}>
              Usuario
            </Typography>
            <Typography variant="body1" sx={{ fontWeight: 500 }}>
              {selectedUser
                ? `${selectedUser.firstName} ${selectedUser.lastName}`
                : ""}
            </Typography>
            <Typography variant="body2" color="text.secondary">
              {selectedUser?.institutionalEmail}
            </Typography>
          </Box>

          <RolesSelector
            roles={roles}
            selectedRoleIds={selectedRoleIds}
            onToggleRole={onToggleRole}
          />
        </Stack>
      </DialogContent>

      <DialogActions sx={{ p: 2.5 }}>
        <Button onClick={onClose} sx={{ fontWeight: 600 }}>Cancelar</Button>
        <Button variant="contained" onClick={onSave} sx={{ borderRadius: 2, fontWeight: 700, px: 3 }}>
          Guardar cambios
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default AssignRolesDialog;

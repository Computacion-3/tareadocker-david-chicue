import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
} from "@mui/material";
import UserForm from "./UserForm";

type Props = {
  open: boolean;
  firstName: string;
  setFirstName: (value: string) => void;
  lastName: string;
  setLastName: (value: string) => void;
  institutionalEmail: string;
  setInstitutionalEmail: (value: string) => void;
  password: string;
  setPassword: (value: string) => void;
  age: string;
  setAge: (value: string) => void;
  onClose: () => void;
  onSave: () => void;
};

const UserCreateDialog = ({
  open,
  firstName,
  setFirstName,
  lastName,
  setLastName,
  institutionalEmail,
  setInstitutionalEmail,
  password,
  setPassword,
  age,
  setAge,
  onClose,
  onSave,
}: Props) => {
  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm" sx={{ '& .MuiDialog-paper': { borderRadius: 3 } }}>
      <DialogTitle sx={{ fontWeight: 'bold' }}>Crear nuevo usuario</DialogTitle>

      <DialogContent dividers>
        <UserForm
          firstName={firstName}
          setFirstName={setFirstName}
          lastName={lastName}
          setLastName={setLastName}
          institutionalEmail={institutionalEmail}
          setInstitutionalEmail={setInstitutionalEmail}
          password={password}
          setPassword={setPassword}
          age={age}
          setAge={setAge}
        />
      </DialogContent>

      <DialogActions sx={{ p: 2.5 }}>
        <Button onClick={onClose} sx={{ fontWeight: 600 }}>Cancelar</Button>
        <Button variant="contained" onClick={onSave} sx={{ borderRadius: 2, fontWeight: 700, px: 3 }}>
          Crear usuario
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default UserCreateDialog;

import { Stack, TextField } from "@mui/material";

type Props = {
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
};

const UserForm = ({
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
}: Props) => {
  return (
    <Stack spacing={2.5} sx={{ mt: 1 }}>
      <TextField
        label="Nombre"
        fullWidth
        value={firstName}
        onChange={(e) => setFirstName(e.target.value)}
      />

      <TextField
        label="Apellido"
        fullWidth
        value={lastName}
        onChange={(e) => setLastName(e.target.value)}
      />

      <TextField
        label="Correo institucional"
        fullWidth
        type="email"
        value={institutionalEmail}
        onChange={(e) => setInstitutionalEmail(e.target.value)}
      />

      <TextField
        label="Contraseña"
        fullWidth
        type="password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />

      <TextField
        label="Edad"
        fullWidth
        type="number"
        value={age}
        onChange={(e) => setAge(e.target.value)}
      />
    </Stack>
  );
};

export default UserForm;

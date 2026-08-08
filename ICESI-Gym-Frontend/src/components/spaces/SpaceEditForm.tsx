import Stack from "@mui/material/Stack";
import TextField from "@mui/material/TextField";

type Props = {
  name: string;
  setName: (value: string) => void;
  capacity: string;
  setCapacity: (value: string) => void;
  location: string;
  setLocation: (value: string) => void;
};

const SpaceEditForm = ({
  name,
  setName,
  capacity,
  setCapacity,
  location,
  setLocation,
}: Props) => {
  return (
    <Stack spacing={2}>
      <TextField
        label="Nombre"
        fullWidth
        value={name}
        onChange={(e) => setName(e.target.value)}
      />

      <TextField
        label="Capacidad"
        type="number"
        fullWidth
        value={capacity}
        onChange={(e) => setCapacity(e.target.value)}
      />

      <TextField
        label="Ubicación"
        fullWidth
        value={location}
        onChange={(e) => setLocation(e.target.value)}
      />
    </Stack>
  );
};

export default SpaceEditForm;

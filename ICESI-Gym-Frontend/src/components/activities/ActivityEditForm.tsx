import FormControl from "@mui/material/FormControl";
import InputLabel from "@mui/material/InputLabel";
import MenuItem from "@mui/material/MenuItem";
import Select from "@mui/material/Select";
import Stack from "@mui/material/Stack";
import TextField from "@mui/material/TextField";
import type { SpaceResponse } from "../../types/api.types";

type Props = {
  name: string;
  setName: (value: string) => void;
  description: string;
  setDescription: (value: string) => void;
  startDate: string;
  setStartDate: (value: string) => void;
  endDate: string;
  setEndDate: (value: string) => void;
  spaceId: string;
  setSpaceId: (value: string) => void;
  spaces: SpaceResponse[];
};

const ActivityEditForm = ({
  name,
  setName,
  description,
  setDescription,
  startDate,
  setStartDate,
  endDate,
  setEndDate,
  spaceId,
  setSpaceId,
  spaces,
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
        label="Descripción"
        fullWidth
        multiline
        minRows={3}
        value={description}
        onChange={(e) => setDescription(e.target.value)}
      />

      <FormControl fullWidth>
        <InputLabel>Espacio</InputLabel>
        <Select
          value={spaceId}
          label="Espacio"
          onChange={(e) => setSpaceId(e.target.value)}
        >
          <MenuItem value="">Sin espacio</MenuItem>
          {spaces.map((space) => (
            <MenuItem key={space.idSpace} value={String(space.idSpace)}>
              {space.name}
            </MenuItem>
          ))}
        </Select>
      </FormControl>

      <TextField
        label="Fecha inicio"
        type="date"
        fullWidth
        value={startDate}
        onChange={(e) => setStartDate(e.target.value)}
        slotProps={{ inputLabel: { shrink: true } }}
      />

      <TextField
        label="Fecha fin"
        type="date"
        fullWidth
        value={endDate}
        onChange={(e) => setEndDate(e.target.value)}
        slotProps={{ inputLabel: { shrink: true } }}
      />
    </Stack>
  );
};

export default ActivityEditForm;

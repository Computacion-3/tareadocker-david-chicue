import {
  Checkbox,
  FormControl,
  FormControlLabel,
  InputLabel,
  MenuItem,
  Select,
  Stack,
  TextField,
} from "@mui/material";
import Grid from "@mui/material/Grid";

type Props = {
  name: string;
  setName: (v: string) => void;
  type: string;
  setType: (v: string) => void;
  description: string;
  setDescription: (v: string) => void;
  durationMin: string;
  setDurationMin: (v: string) => void;
  difficulty: string;
  setDifficulty: (v: string) => void;
  videoUrl: string;
  setVideoUrl: (v: string) => void;
  isPredefined: boolean;
  setIsPredefined: (v: boolean) => void;
};

const ExerciseEditForm = ({
  name,
  setName,
  type,
  setType,
  description,
  setDescription,
  durationMin,
  setDurationMin,
  difficulty,
  setDifficulty,
  videoUrl,
  setVideoUrl,
  isPredefined,
  setIsPredefined,
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
        label="Tipo"
        fullWidth
        value={type}
        onChange={(e) => setType(e.target.value)}
      />
      <TextField
        label="Descripción"
        fullWidth
        multiline
        minRows={4}
        value={description}
        onChange={(e) => setDescription(e.target.value)}
      />

      <Grid container spacing={2}>
        <Grid size={{ xs: 12, sm: 6 }}>
          <TextField
            label="Duración (min)"
            type="number"
            fullWidth
            value={durationMin}
            onChange={(e) => setDurationMin(e.target.value)}
          />
        </Grid>

        <Grid size={{ xs: 12, sm: 6 }}>
          <FormControl fullWidth>
            <InputLabel>Dificultad</InputLabel>
            <Select
              value={difficulty}
              label="Dificultad"
              onChange={(e) => setDifficulty(e.target.value)}
            >
              <MenuItem value="">Sin especificar</MenuItem>
              <MenuItem value="Baja">Baja</MenuItem>
              <MenuItem value="Media">Media</MenuItem>
              <MenuItem value="Alta">Alta</MenuItem>
            </Select>
          </FormControl>
        </Grid>
      </Grid>

      <TextField
        label="URL del video"
        fullWidth
        value={videoUrl}
        onChange={(e) => setVideoUrl(e.target.value)}
      />

      <FormControlLabel
        control={
          <Checkbox
            checked={isPredefined}
            onChange={(e) => setIsPredefined(e.target.checked)}
          />
        }
        label="Es predefinido (Sistema)"
      />
    </Stack>
  );
};

export default ExerciseEditForm;

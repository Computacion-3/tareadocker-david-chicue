import { Box, Button, Divider, Stack, Typography } from "@mui/material";
import Grid from "@mui/material/Grid";
import type { ExerciseResponse } from "../../types/api.types";

const InfoRow = ({ label, value }: { label: string; value: string }) => (
  <Box>
    <Typography
      variant="caption"
      color="text.secondary"
      sx={{ display: "block", mb: 0.5 }}
    >
      {label}
    </Typography>
    <Typography variant="body1" sx={{ fontWeight: 500 }}>
      {value}
    </Typography>
  </Box>
);

type Props = {
  exercise: ExerciseResponse;
  hasPreviewUrl: boolean;
};

const ExerciseReadOnlyDetails = ({ exercise, hasPreviewUrl }: Props) => {
  return (
    <Stack spacing={2.5}>
      <Box>
        <Typography
          variant="subtitle2"
          color="text.secondary"
          sx={{ mb: 0.75 }}
        >
          Descripción
        </Typography>
        <Typography variant="body1">
          {exercise.description || "Sin descripción registrada."}
        </Typography>
      </Box>

      <Divider />

      <Grid container spacing={3}>
        <Grid size={{ xs: 12, sm: 6 }}>
          <InfoRow
            label="Duración"
            value={
              exercise.durationMin
                ? `${exercise.durationMin} min`
                : "No especificada"
            }
          />
        </Grid>
        <Grid size={{ xs: 12, sm: 6 }}>
          <InfoRow
            label="Autor"
            value={exercise.userId ? `Usuario #${exercise.userId}` : "Sistema"}
          />
        </Grid>
        <Grid size={{ xs: 12, sm: 6 }}>
          <InfoRow
            label="Dificultad"
            value={exercise.difficulty || "Sin especificar"}
          />
        </Grid>
        <Grid size={{ xs: 12, sm: 6 }}>
          <InfoRow label="Tipo" value={exercise.type || "Sin tipo"} />
        </Grid>
      </Grid>

      {exercise.videoUrl && !hasPreviewUrl && (
        <Box>
          <Button
            href={exercise.videoUrl}
            target="_blank"
            rel="noopener noreferrer"
            variant="outlined"
          >
            Abrir video
          </Button>
        </Box>
      )}
    </Stack>
  );
};

export default ExerciseReadOnlyDetails;

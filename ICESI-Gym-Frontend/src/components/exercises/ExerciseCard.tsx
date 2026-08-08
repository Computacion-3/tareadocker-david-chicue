import { Chip, Stack, Typography, Box } from "@mui/material";
import FitnessCenterIcon from "@mui/icons-material/FitnessCenter";
import TimerIcon from "@mui/icons-material/Timer";
import type { ExerciseResponse } from "../../types/api.types";
import EntityCard from "../common/EntityCard";

type Props = {
  exercise: ExerciseResponse;
  onClick?: (exercise: ExerciseResponse) => void;
};

const getDifficultyColor = (value?: string) => {
  if (value === "Alta") {return "error";}
  if (value === "Media") {return "warning";}
  if (value === "Baja" || value === "Beginner") {return "success";}
  return "default";
};

const ExerciseCard = ({ exercise, onClick }: Props) => {
  return (
    <EntityCard onClick={onClick ? () => onClick(exercise) : undefined}>
      <Stack direction="row" spacing={1} sx={{ flexWrap: "wrap", mb: 2 }}>
        <Chip
          size="small"
          icon={<FitnessCenterIcon sx={{ fontSize: '1rem !important' }} />}
          label={exercise.type}
          color="primary"
          variant="outlined"
          sx={{ fontWeight: 700, borderRadius: 1.5 }}
        />

        <Chip
          size="small"
          label={exercise.isPredefined ? "Sistema" : "Usuario"}
          color={exercise.isPredefined ? "secondary" : "default"}
          sx={{ fontWeight: 700, borderRadius: 1.5 }}
        />

        {exercise.difficulty && (
          <Chip
            size="small"
            label={exercise.difficulty}
            color={getDifficultyColor(exercise.difficulty)}
            sx={{ fontWeight: 700, borderRadius: 1.5 }}
          />
        )}
      </Stack>

      <Typography variant="h6" sx={{ fontWeight: 800, mb: 1, letterSpacing: '-0.3px' }}>
        {exercise.name}
      </Typography>

      <Typography variant="body2" color="text.secondary" sx={{ flexGrow: 1, fontWeight: 500 }}>
        {exercise.description || 'Sin descripción detallada.'}
      </Typography>

      {exercise.durationMin && (
        <Box sx={{ mt: 2, display: 'flex', alignItems: 'center', gap: 0.5 }}>
          <TimerIcon fontSize="small" sx={{ color: 'text.disabled' }} />
          <Typography variant="caption" sx={{ fontWeight: 700, color: 'text.disabled' }}>
            {exercise.durationMin} MIN
          </Typography>
        </Box>
      )}
    </EntityCard>
  );
};

export default ExerciseCard;

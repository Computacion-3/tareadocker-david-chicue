import Stack from "@mui/material/Stack";
import Typography from "@mui/material/Typography";
import type { SpaceResponse } from "../../types/api.types";

type Props = {
  space: SpaceResponse;
};

const SpaceReadOnlyDetails = ({ space }: Props) => {
  return (
    <Stack spacing={2}>
      <Stack spacing={0.5}>
        <Typography variant="subtitle2" color="text.secondary">
          Nombre
        </Typography>
        <Typography variant="body1">{space.name}</Typography>
      </Stack>

      <Stack spacing={0.5}>
        <Typography variant="subtitle2" color="text.secondary">
          Capacidad
        </Typography>
        <Typography variant="body1">
          {space.capacity ?? "No definida"}
        </Typography>
      </Stack>

      <Stack spacing={0.5}>
        <Typography variant="subtitle2" color="text.secondary">
          Ubicación
        </Typography>
        <Typography variant="body1">
          {space.location || "Sin ubicación"}
        </Typography>
      </Stack>
    </Stack>
  );
};

export default SpaceReadOnlyDetails;

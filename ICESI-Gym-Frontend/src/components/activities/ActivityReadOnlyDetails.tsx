import Stack from "@mui/material/Stack";
import Typography from "@mui/material/Typography";
import type { ActivityResponse, SpaceResponse } from "../../types/api.types";

type Props = {
  activity: ActivityResponse;
  spaceMap: Map<number, SpaceResponse>;
};

const ActivityReadOnlyDetails = ({ activity, spaceMap }: Props) => {
  const spaceName = activity.spaceId
    ? (spaceMap.get(activity.spaceId)?.name ?? `Espacio #${activity.spaceId}`)
    : "Sin espacio";

  return (
    <Stack spacing={2}>
      <Stack spacing={0.5}>
        <Typography variant="subtitle2" color="text.secondary">
          Nombre
        </Typography>
        <Typography variant="body1">{activity.name}</Typography>
      </Stack>

      <Stack spacing={0.5}>
        <Typography variant="subtitle2" color="text.secondary">
          Descripción
        </Typography>
        <Typography variant="body1">
          {activity.description || "Sin descripción"}
        </Typography>
      </Stack>

      <Stack spacing={0.5}>
        <Typography variant="subtitle2" color="text.secondary">
          Espacio
        </Typography>
        <Typography variant="body1">{spaceName}</Typography>
      </Stack>

      <Stack spacing={0.5}>
        <Typography variant="subtitle2" color="text.secondary">
          Fecha inicio
        </Typography>
        <Typography variant="body1">{activity.startDate}</Typography>
      </Stack>

      <Stack spacing={0.5}>
        <Typography variant="subtitle2" color="text.secondary">
          Fecha fin
        </Typography>
        <Typography variant="body1">{activity.endDate}</Typography>
      </Stack>
    </Stack>
  );
};

export default ActivityReadOnlyDetails;

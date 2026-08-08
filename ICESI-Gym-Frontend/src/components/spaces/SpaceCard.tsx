import { Chip, Stack, Typography, Box } from "@mui/material";
import MeetingRoomIcon from "@mui/icons-material/MeetingRoom";
import LocationOnIcon from "@mui/icons-material/LocationOn";
import PeopleIcon from "@mui/icons-material/People";
import type { SpaceResponse } from "../../types/api.types";
import EntityCard from "../common/EntityCard";

type Props = {
  space: SpaceResponse;
  onClick?: (space: SpaceResponse) => void;
};

const SpaceCard = ({ space, onClick }: Props) => {
  return (
    <EntityCard onClick={onClick ? () => onClick(space) : undefined}>
      <Stack direction="row" spacing={2} sx={{ alignItems: 'center', mb: 1 }}>
        <Box 
          sx={{ 
            p: 1.5, 
            borderRadius: 3, 
            bgcolor: 'primary.main', 
            color: 'white',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center'
          }}
        >
          <MeetingRoomIcon />
        </Box>
        <Typography variant="h6" sx={{ fontWeight: 800, letterSpacing: '-0.5px' }}>
          {space.name}
        </Typography>
      </Stack>

      <Stack spacing={1.5} sx={{ mt: 1 }}>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
          <LocationOnIcon fontSize="small" color="primary" />
          <Typography variant="body2" sx={{ fontWeight: 500, color: 'text.secondary' }}>
            {space.location || 'Ubicación no especificada'}
          </Typography>
        </Box>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
          <PeopleIcon fontSize="small" color="primary" />
          <Typography variant="body2" sx={{ fontWeight: 500, color: 'text.secondary' }}>
            Capacidad: <strong>{space.capacity || 'N/A'}</strong> personas
          </Typography>
        </Box>
      </Stack>

      <Box sx={{ mt: 2 }}>
        <Chip 
          label="Disponible" 
          color="success" 
          size="small" 
          sx={{ fontWeight: 700, borderRadius: 1.5 }} 
        />
      </Box>
    </EntityCard>
  );
};

export default SpaceCard;

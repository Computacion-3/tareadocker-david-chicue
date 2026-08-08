import { Stack, Typography, Box, Button, Chip } from "@mui/material";
import CalendarMonthIcon from "@mui/icons-material/CalendarMonth";
import MeetingRoomIcon from "@mui/icons-material/MeetingRoom";
import HowToRegIcon from '@mui/icons-material/HowToReg';
import CancelIcon from '@mui/icons-material/Cancel';
import type { ActivityWithSpace } from "../../services/ActivityService";
import EntityCard from "../common/EntityCard";

type Props = {
  activity: ActivityWithSpace;
  onClickDetails?: (activity: ActivityWithSpace) => void;
  onEnroll?: (activity: ActivityWithSpace) => void;
  onCancelEnroll?: (activity: ActivityWithSpace) => void;
  isAdmin?: boolean;
  isEnrolled?: boolean;
};

const ActivityCard = ({ activity, onClickDetails, onEnroll, onCancelEnroll, isAdmin, isEnrolled }: Props) => {
    return (
        <Box sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
            <EntityCard onClick={onClickDetails ? () => onClickDetails(activity) : undefined}>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', mb: 1 }}>
                    <Typography variant="h6" sx={{ fontWeight: 800, color: 'primary.main', letterSpacing: '-0.3px' }}>
                        {activity.name}
                    </Typography>
                    {isEnrolled && (
                        <Chip 
                            label="Inscrito" 
                            color="success" 
                            size="small" 
                            icon={<HowToRegIcon />}
                            sx={{ fontWeight: 700, borderRadius: 1.5 }} 
                        />
                    )}
                </Box>

                <Typography variant="body2" color="text.secondary" sx={{ flexGrow: 1, fontWeight: 500, minHeight: '3em' }}>
                    {activity.description || 'Sin descripción disponible.'}
                </Typography>

                <Stack spacing={1.5} sx={{ mt: 2 }}>
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                        <MeetingRoomIcon fontSize="small" color="primary" />
                        <Typography variant="body2" sx={{ fontWeight: 600 }}>
                            {activity.space?.name || 'Espacio no asignado'}
                        </Typography>
                    </Box>
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                        <CalendarMonthIcon fontSize="small" color="primary" />
                        <Typography variant="body2" sx={{ fontWeight: 600 }}>
                            {new Date(activity.startDate).toLocaleDateString()} - {new Date(activity.endDate).toLocaleDateString()}
                        </Typography>
                    </Box>
                </Stack>
            </EntityCard>

            {!isAdmin && (
                isEnrolled ? (
                    <Button
                        fullWidth
                        variant="outlined"
                        color="error"
                        startIcon={<CancelIcon />}
                        sx={{ mt: 1, fontWeight: 700, borderRadius: 2, textTransform: 'none' }}
                        onClick={() => onCancelEnroll?.(activity)}
                    >
                        Cancelar Inscripción
                    </Button>
                ) : (
                    <Button
                        fullWidth
                        variant="contained"
                        color="success"
                        startIcon={<HowToRegIcon />}
                        sx={{ mt: 1, fontWeight: 700, borderRadius: 2, textTransform: 'none' }}
                        onClick={() => onEnroll?.(activity)}
                    >
                        Inscribirme
                    </Button>
                )
            )}
        </Box>
    );
};

export default ActivityCard;

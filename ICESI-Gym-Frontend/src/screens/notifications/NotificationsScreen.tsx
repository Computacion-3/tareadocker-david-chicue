import React, { useEffect, useState } from 'react';
import {
    Box,
    Typography,
    List,
    ListItem,
    ListItemText,
    ListItemAvatar,
    Avatar,
    Paper,
    Divider,
    IconButton,
    Tooltip,
    CircularProgress,
    Alert,
    Button
} from '@mui/material';
import NotificationsIcon from '@mui/icons-material/Notifications';
import DeleteIcon from '@mui/icons-material/Delete';
import MarkEmailReadIcon from '@mui/icons-material/MarkEmailRead';
import { useAppSelector } from '../../hooks/useSelector';
import { useAppDispatch } from '../../hooks/useDispatch';
import { getAllNotifications, updateNotification, deleteNotification } from '../../services/NotificationService';
import { setNotifications, markNotificationAsRead } from '../../store/slices/realTimeSlice';
import { formatLocalDateTime } from '../../utils/date.utils';

const NotificationsScreen: React.FC = () => {
    const { notifications } = useAppSelector((state) => state.realTime);
    const dispatch = useAppDispatch();
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const sortedNotifications = React.useMemo(() => {
        return [...notifications].sort((a, b) => 
            new Date(b.dateSent).getTime() - new Date(a.dateSent).getTime()
        );
    }, [notifications]);

    useEffect(() => {
        let isMounted = true;
        const fetchHistory = async () => {
            const result = await getAllNotifications();
            if (isMounted) {
                if (result.error) {
                    setError(result.message || 'Error al cargar notificaciones');
                } else if (result.data) {
                    dispatch(setNotifications(result.data));
                }
                setLoading(false);
            }
        };

        fetchHistory();
        return () => { isMounted = false; };
    }, [dispatch]);

    const handleMarkAsRead = async (id: number) => {
        const notification = notifications.find(n => n.idNotification === id);
        if (!notification) {return;}

        const result = await updateNotification(id, {
            ...notification,
            isRead: true
        });

        if (!result.error) {
            dispatch(markNotificationAsRead(id));
        }
    };

    const handleDelete = async (id: number) => {
        const result = await deleteNotification(id);
        if (!result.error) {
            const updated = notifications.filter(n => n.idNotification !== id);
            dispatch(setNotifications(updated));
        }
    };

    const handleMarkAllAsRead = async () => {
        const unread = notifications.filter(n => !n.isRead);
        await Promise.all(unread.map(n => handleMarkAsRead(n.idNotification)));
    };

    if (loading) {
        return (
            <Box sx={{ display: 'flex', justifyContent: 'center', py: 8 }}>
                <CircularProgress />
            </Box>
        );
    }

    return (
        <Box>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 4 }}>
                <Typography variant="h4" sx={{ fontWeight: 'bold' }}>
                    Notificaciones
                </Typography>
                <Button 
                    variant="outlined" 
                    size="small" 
                    onClick={handleMarkAllAsRead}
                    disabled={notifications.filter(n => !n.isRead).length === 0}
                >
                    Marcar todas como leídas
                </Button>
            </Box>

            {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

            <Paper sx={{ borderRadius: 4, overflow: 'hidden', boxShadow: 3 }}>
                <List sx={{ p: 0 }}>
                    {sortedNotifications.length === 0 ? (
                        <ListItem sx={{ py: 4, justifyContent: 'center' }}>
                            <Typography color="text.secondary">No tienes notificaciones todavía.</Typography>
                        </ListItem>
                    ) : (
                        sortedNotifications.map((notif, index) => (
                            <React.Fragment key={`${notif.idNotification}-${index}`}>
                                <ListItem
                                    sx={{
                                        bgcolor: notif.isRead ? 'transparent' : 'rgba(83, 83, 238, 0.05)',
                                        py: 2,
                                        transition: 'background-color 0.3s'
                                    }}
                                    secondaryAction={
                                        <Box>
                                            {!notif.isRead && (
                                                <Tooltip title="Marcar como leída">
                                                    <IconButton 
                                                        size="small" 
                                                        color="primary" 
                                                        onClick={() => handleMarkAsRead(notif.idNotification)}
                                                    >
                                                        <MarkEmailReadIcon />
                                                    </IconButton>
                                                </Tooltip>
                                            )}
                                            <Tooltip title="Eliminar">
                                                <IconButton 
                                                    size="small" 
                                                    color="error" 
                                                    onClick={() => handleDelete(notif.idNotification)}
                                                >
                                                    <DeleteIcon />
                                                </IconButton>
                                            </Tooltip>
                                        </Box>
                                    }
                                >
                                    <ListItemAvatar>
                                        <Avatar sx={{ bgcolor: notif.isRead ? 'grey.400' : 'primary.main' }}>
                                            <NotificationsIcon />
                                        </Avatar>
                                    </ListItemAvatar>
                                    <ListItemText
                                        primary={notif.message}
                                        secondary={formatLocalDateTime(notif.dateSent)}
                                        slotProps={{
                                            primary: {
                                                style: { fontWeight: notif.isRead ? 'normal' : 'bold' }
                                            }
                                        }}
                                    />
                                </ListItem>
                                {index < notifications.length - 1 && <Divider />}
                            </React.Fragment>
                        ))
                    )}
                </List>
            </Paper>
        </Box>
    );
};

export default NotificationsScreen;

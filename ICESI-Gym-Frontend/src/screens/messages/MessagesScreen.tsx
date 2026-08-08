import React, {useEffect, useState, useRef, useCallback, useMemo} from 'react';
import {
    Box,
    Typography,
    Paper,
    Divider,
    TextField,
    IconButton,
    CircularProgress,
    List,
    ListItemButton,
    ListItemText,
    ListItemAvatar,
    Avatar
} from '@mui/material';
import SendIcon from '@mui/icons-material/Send';
import PersonIcon from '@mui/icons-material/Person';
import {useAppSelector} from '../../hooks/useSelector';
import {useAppDispatch} from '../../hooks/useDispatch';
import {getAllMessages, sendMessage as sendMessageApi} from '../../services/MessageService';
import {getAssignmentsByTrainer, getAssignmentsByUser} from '../../services/AssignmentService';
import {setMessages, addMessage} from '../../store/slices/realTimeSlice';
import type {AssignmentResponse} from '../../types/api.types';
import useAuth from '../../hooks/useAuth';
import { formatLocalTime } from '../../utils/date.utils';

type Contact = {
    id: number;
    name: string;
};

const MessagesScreen: React.FC = () => {
    const {messages} = useAppSelector((state) => state.realTime);
    const {user} = useAuth();
    const dispatch = useAppDispatch();
    const [loading, setLoading] = useState(true);
    const [, setError] = useState<string | null>(null);
    const [newMessage, setNewMessage] = useState('');
    const [sending, setSending] = useState(false);
    const [contacts, setContacts] = useState<Contact[]>([]);
    const [selectedContact, setSelectedContact] = useState<Contact | null>(null);
    const scrollRef = useRef<HTMLDivElement>(null);

    const fetchContacts = useCallback(async () => {
        if (!user?.idUser) {
            return;
        }

        const isTrainer = user.authorities?.includes('TRAINER') || user.authorities?.includes('ROLE_TRAINER');
        const isTrainee = user.authorities?.includes('TRAINEE') || user.authorities?.includes('ROLE_TRAINEE');

        const fetchedContacts: Contact[] = [];

        try {
            if (isTrainer) {
                const result = await getAssignmentsByTrainer(user.idUser);
                if (result.data) {
                    result.data.forEach((a: AssignmentResponse) => {
                        fetchedContacts.push({id: a.userId, name: `${a.userFirstName} ${a.userLastName}`});
                    });
                }
            } else if (isTrainee) {
                const result = await getAssignmentsByUser(user.idUser);
                if (result.data) {
                    result.data.forEach((a: AssignmentResponse) => {
                        fetchedContacts.push({id: a.trainerId, name: `${a.trainerFirstName} ${a.trainerLastName}`});
                    });
                }
            }
        } catch {
            setError('No se pudieron cargar los contactos.');
        }

        setContacts(fetchedContacts);
        if (fetchedContacts.length > 0 && !selectedContact) {
            setSelectedContact(fetchedContacts[0]);
        }
    }, [user, selectedContact]);

    useEffect(() => {
        let isMounted = true;
        const loadData = async () => {
            setLoading(true);
            await fetchContacts();
            const result = await getAllMessages();
            if (isMounted) {
                if (result.error) {
                    setError(result.message || 'Error al cargar historial de mensajes');
                } else if (result.data) {
                    dispatch(setMessages(result.data));
                }
                setLoading(false);
            }
        };

        loadData();
        return () => {
            isMounted = false;
        };
        // Only fetch history once on component mount to prevent wiping real-time updates
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [dispatch]);

    useEffect(() => {
        if (scrollRef.current) {
            scrollRef.current.scrollTop = scrollRef.current.scrollHeight;
        }
    }, [messages, selectedContact]);

    const filteredMessages = useMemo(() => {
        if (!selectedContact || !user?.idUser) {return [];}

        const myId = Number(user.idUser);
        const contactId = Number(selectedContact.id);

        return messages.filter(m => {
            const sId = Number(m.senderId);
            const rId = m.receiverId ? Number(m.receiverId) : 0;

            const isFromMeToContact = sId === myId && rId === contactId;
            const isFromContactToMe = sId === contactId && rId === myId;

            return isFromMeToContact || isFromContactToMe;
        });
    }, [messages, selectedContact, user?.idUser]);

    const handleSend = async () => {
        if (!newMessage.trim() || !user || !selectedContact || sending) {
            return;
        }

        setSending(true);
        const result = await sendMessageApi({
            content: newMessage,
            senderId: user.idUser,
            receiverId: selectedContact.id,
            sentAt: new Date().toISOString()
        });

        if (result.error) {
            alert(result.message);
        } else if (result.data) {
            // Instant feedback for the sender: Add the message to the state manually
            dispatch(addMessage(result.data));
            setNewMessage('');
        }
        setSending(false);
    };

    const handleKeyPress = (e: React.KeyboardEvent) => {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            void handleSend();
        }
    };

    if (loading) {
        return (
            <Box sx={{display: 'flex', justifyContent: 'center', py: 8}}>
                <CircularProgress/>
            </Box>
        );
    }

    return (
        <Box sx={{height: 'calc(100vh - 250px)', display: 'flex', gap: 2}}>
            {/* Sidebar Contacts */}
            <Paper sx={{
                width: 280,
                display: 'flex',
                flexDirection: 'column',
                borderRadius: 4,
                overflow: 'hidden',
                boxShadow: '0 4px 20px 0 rgba(0,0,0,0.05)'
            }}>
                <Box sx={{p: 2, bgcolor: 'primary.main', color: 'white'}}>
                    <Typography variant="h6" sx={{fontWeight: 700}}>Contactos</Typography>
                </Box>
                <List sx={{overflowY: 'auto', flexGrow: 1}}>
                    {contacts.length === 0 ? (
                        <Box sx={{p: 3, textAlign: 'center'}}>
                            <Typography variant="body2" color="text.secondary">No tienes contactos
                                asignados.</Typography>
                        </Box>
                    ) : (
                        contacts.map((contact) => (
                            <ListItemButton
                                key={contact.id}
                                selected={selectedContact?.id === contact.id}
                                onClick={() => setSelectedContact(contact)}
                                sx={{
                                    borderLeft: selectedContact?.id === contact.id ? '4px solid' : '4px solid transparent',
                                    borderLeftColor: 'primary.main'
                                }}
                            >
                                <ListItemAvatar>
                                    <Avatar sx={{bgcolor: 'primary.light'}}>
                                        <PersonIcon/>
                                    </Avatar>
                                </ListItemAvatar>
                                <ListItemText
                                    primary={
                                        <Typography variant="body1"
                                                    sx={{fontWeight: selectedContact?.id === contact.id ? 700 : 500}}>
                                            {contact.name}
                                        </Typography>
                                    }
                                    secondary="Mensaje directo"
                                />
                            </ListItemButton>
                        ))
                    )}
                </List>
            </Paper>

            {/* Chat Area */}
            <Paper sx={{
                flexGrow: 1,
                display: 'flex',
                flexDirection: 'column',
                borderRadius: 4,
                overflow: 'hidden',
                boxShadow: '0 4px 20px 0 rgba(0,0,0,0.1)'
            }}>
                {selectedContact ? (
                    <>
                        <Box sx={{
                            p: 2,
                            borderBottom: '1px solid',
                            borderColor: 'divider',
                            display: 'flex',
                            alignItems: 'center',
                            gap: 2
                        }}>
                            <Avatar sx={{bgcolor: 'primary.main', width: 32, height: 32}}>
                                <PersonIcon fontSize="small"/>
                            </Avatar>
                            <Typography variant="h6" sx={{fontWeight: 700}}>{selectedContact.name}</Typography>
                        </Box>

                        <Box
                            ref={scrollRef}
                            sx={{
                                flexGrow: 1,
                                overflowY: 'auto',
                                p: 3,
                                display: 'flex',
                                flexDirection: 'column',
                                gap: 2,
                                bgcolor: '#f8f9fa'
                            }}
                        >
                            {filteredMessages.length === 0 ? (
                                <Box sx={{
                                    display: 'flex',
                                    justifyContent: 'center',
                                    alignItems: 'center',
                                    height: '100%'
                                }}>
                                    <Typography color="text.secondary">No hay mensajes en esta
                                        conversación.</Typography>
                                </Box>
                            ) : (
                                filteredMessages.map((msg, index) => {
                                    const isOwn = Number(msg.senderId) === Number(user?.idUser);
                                    return (
                                        <Box
                                            key={index}
                                            sx={{
                                                alignSelf: isOwn ? 'flex-end' : 'flex-start',
                                                maxWidth: '70%',
                                                bgcolor: isOwn ? 'primary.main' : 'white',
                                                color: isOwn ? 'white' : 'text.primary',
                                                p: 2,
                                                borderRadius: 3,
                                                borderBottomLeftRadius: isOwn ? 3 : 0,
                                                borderBottomRightRadius: isOwn ? 0 : 3,
                                                boxShadow: '0 2px 8px 0 rgba(0,0,0,0.05)'
                                            }}
                                        >
                                            <Typography variant="body1"
                                                        sx={{fontWeight: 500}}>{msg.content}</Typography>
                                            <Typography
                                                variant="caption"
                                                sx={{
                                                    display: 'block',
                                                    mt: 0.5,
                                                    textAlign: 'right',
                                                    opacity: 0.7
                                                }}
                                            >
                                                {formatLocalTime(msg.sentAt)}
                                            </Typography>
                                        </Box>
                                    );
                                })
                            )}
                        </Box>

                        <Divider/>
                        <Box sx={{p: 2, display: 'flex', gap: 2, bgcolor: 'white'}}>
                            <TextField
                                fullWidth
                                placeholder={`Escribe a ${selectedContact.name}...`}
                                variant="outlined"
                                size="small"
                                value={newMessage}
                                onChange={(e) => setNewMessage(e.target.value)}
                                onKeyPress={handleKeyPress}
                                disabled={sending}
                            />
                            <IconButton
                                color="primary"
                                onClick={handleSend}
                                disabled={!newMessage.trim() || sending}
                            >
                                {sending ? <CircularProgress size={24}/> : <SendIcon/>}
                            </IconButton>
                        </Box>
                    </>
                ) : (
                    <Box sx={{display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100%'}}>
                        <Typography color="text.secondary">Selecciona un contacto para chatear.</Typography>
                    </Box>
                )}
            </Paper>
        </Box>
    );
};

export default MessagesScreen;

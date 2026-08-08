import {useMemo, useState} from "react";
import {
    Box,
    Button,
    Chip,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    Stack,
} from "@mui/material";
import DeleteOutlineRoundedIcon from "@mui/icons-material/DeleteOutlineRounded";
import EditOutlinedIcon from "@mui/icons-material/EditOutlined";

import type {ExerciseRequest, ExerciseResponse} from "../../types/api.types";
import {
    buildExercisePayload,
    getDifficultyColor,
    getEmbedUrl,
} from "../../utils/exercise.utils";
import ExerciseVideoPreview from "./ExerciseVideoPreview";
import ExerciseReadOnlyDetails from "./ExerciseReadOnlyDetails";
import ExerciseEditForm from "./ExerciseEditForm";
import {
    getCurrentUserId,
    getRoutineRoleFlags,
} from "../../utils/routine.utils";
import useAuth from "../../hooks/useAuth";

type Props = {
    open: boolean;
    exercise: ExerciseResponse | null;
    onClose: () => void;
    onSave: (id: number, payload: ExerciseRequest) => Promise<void>;
    onDelete: (id: number) => Promise<void>;
};

const ExerciseDetailsDialog = ({
                                   open,
                                   exercise,
                                   onClose,
                                   onSave,
                                   onDelete,
                               }: Props) => {
    const [isEditing, setIsEditing] = useState(false);

    const [name, setName] = useState("");
    const [type, setType] = useState("");
    const [description, setDescription] = useState("");
    const [durationMin, setDurationMin] = useState("");
    const [difficulty, setDifficulty] = useState("");
    const [videoUrl, setVideoUrl] = useState("");
    const [isPredefined, setIsPredefined] = useState(false);

    const {user: authUser} = useAuth();

    const currentUserId = useMemo(
        () => getCurrentUserId(authUser),
        [authUser],
    );

    const {isTrainer} = useMemo(
        () => getRoutineRoleFlags(authUser),
        [authUser],
    );

    const [prevExerciseId, setPrevExerciseId] = useState<number | null>(null);

    if (exercise && exercise.idExercise !== prevExerciseId) {
        setPrevExerciseId(exercise.idExercise);
        setName(exercise.name ?? "");
        setType(exercise.type ?? "");
        setDescription(exercise.description ?? "");
        setDurationMin(exercise.durationMin ? String(exercise.durationMin) : "");
        setDifficulty(exercise.difficulty ?? "");
        setVideoUrl(exercise.videoUrl ?? "");
        setIsPredefined(Boolean(exercise.isPredefined));
        setIsEditing(false);
    }

    const currentVideoUrl = videoUrl || exercise?.videoUrl || "";
    const previewUrl = useMemo(
        () => getEmbedUrl(currentVideoUrl),
        [currentVideoUrl],
    );
    const isDirectVideo = /\.(mp4|webm|ogg)(\?.*)?$/i.test(currentVideoUrl);

    if (!exercise) {
        return null;
    }

    const handleSave = async () => {
        const payload = buildExercisePayload({
            name,
            type,
            description,
            durationMin,
            difficulty,
            videoUrl,
            currentUserId,
            isTrainer,
        });

        await onSave(exercise.idExercise, payload);
        setIsEditing(false);
    };

    const isAdmin = authUser?.authorities?.includes('ROLE_ADMIN') || authUser?.authorities?.includes('ADMIN');

    return (
        <Dialog
            open={open}
            onClose={onClose}
            fullWidth
            maxWidth="md"
            scroll="paper"
            sx={{'& .MuiDialog-paper': {borderRadius: 3}}}
        >
            <DialogTitle sx={{pb: 1, fontWeight: 'bold'}}>
                {isEditing ? "Editar ejercicio" : exercise.name}
            </DialogTitle>

            <DialogContent dividers sx={{p: 3}}>
                <Stack spacing={3}>
                    {!isEditing && (
                        <Stack direction="row" spacing={1} sx={{flexWrap: "wrap"}}>
                            <Chip label={exercise.type} variant="outlined" sx={{fontWeight: 600}}/>
                            <Chip
                                label={exercise.difficulty || "Sin dificultad"}
                                color={getDifficultyColor(exercise.difficulty) as "default" | "success" | "warning" | "error"}
                                variant="outlined"
                                sx={{fontWeight: 600}}
                            />
                            <Chip
                                label={exercise.isPredefined ? "Predefinido" : "Personalizado"}
                                color={exercise.isPredefined ? "primary" : "default"}
                                variant={exercise.isPredefined ? "filled" : "outlined"}
                                sx={{fontWeight: 600}}
                            />
                        </Stack>
                    )}

                    <ExerciseVideoPreview
                        previewUrl={previewUrl}
                        isDirectVideo={isDirectVideo}
                        exerciseName={exercise.name}
                    />

                    {isEditing ? (
                        <ExerciseEditForm
                            name={name}
                            setName={setName}
                            type={type}
                            setType={setType}
                            description={description}
                            setDescription={setDescription}
                            durationMin={durationMin}
                            setDurationMin={setDurationMin}
                            difficulty={difficulty}
                            setDifficulty={setDifficulty}
                            videoUrl={videoUrl}
                            setVideoUrl={setVideoUrl}
                            isPredefined={isPredefined}
                            setIsPredefined={setIsPredefined}
                        />
                    ) : (
                        <ExerciseReadOnlyDetails
                            exercise={exercise}
                            hasPreviewUrl={!!previewUrl}
                        />
                    )}
                </Stack>
            </DialogContent>

            <DialogActions sx={{justifyContent: "space-between", px: 3, py: 2}}>
                {isAdmin ? (
                    <Button
                        color="error"
                        startIcon={<DeleteOutlineRoundedIcon/>}
                        onClick={() => onDelete(exercise.idExercise)}
                        sx={{fontWeight: 600}}
                    >
                        Eliminar
                    </Button>
                ) : <Box/>}

                <Stack direction="row" spacing={1}>
                    <Button onClick={onClose} sx={{fontWeight: 600}}>Cerrar</Button>

                    {isAdmin && (
                        isEditing ? (
                            <Button variant="contained" onClick={handleSave} sx={{fontWeight: 700, borderRadius: 2}}>
                                Guardar cambios
                            </Button>
                        ) : (
                            <Button
                                variant="contained"
                                startIcon={<EditOutlinedIcon/>}
                                onClick={() => setIsEditing(true)}
                                sx={{fontWeight: 700, borderRadius: 2}}
                            >
                                Editar
                            </Button>
                        )
                    )}
                </Stack>
            </DialogActions>
        </Dialog>
    );
};

export default ExerciseDetailsDialog;

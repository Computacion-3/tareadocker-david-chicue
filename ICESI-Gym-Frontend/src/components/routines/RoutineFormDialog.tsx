import { useMemo, useState } from "react";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogTitle from "@mui/material/DialogTitle";
import Stack from "@mui/material/Stack";
import TextField from "@mui/material/TextField";

import type {
  ExerciseRequest,
  ExerciseResponse,
  RoutineRequest,
  RoutineResponse,
  RoutineExerciseResponse,
} from "../../types/api.types";
import type { RoutineExerciseDraft } from "../../utils/routine.utils";
import {
  buildRoutinePayload,
  sortExercisesByName,
} from "../../utils/routine.utils";
import RoutineExercisesBuilder from "./RoutineExercisesBuilder";
import CustomExerciseDialog from "./CustomExerciseDialog";

type Props = {
  open: boolean;
  routine: RoutineResponse | null;
  exercises: ExerciseResponse[];
  routineExercises: RoutineExerciseResponse[];
  currentUserId: number | null;
  isTrainer: boolean;
  onClose: () => void;
  onSave: (
    payload: RoutineRequest,
    drafts: RoutineExerciseDraft[],
    id?: number,
  ) => Promise<void>;
  onCreateCustomExercise: (payload: ExerciseRequest) => Promise<void>;
};

const RoutineFormDialog = ({
  open,
  routine,
  exercises,
  routineExercises,
  currentUserId,
  isTrainer,
  onClose,
  onSave,
  onCreateCustomExercise,
}: Props) => {
  const [selectedExerciseId, setSelectedExerciseId] = useState("");
  const [openCustomExercise, setOpenCustomExercise] = useState(false);

  const sortedExercises = useMemo(
    () => sortExercisesByName(exercises),
    [exercises],
  );

  const initialDrafts = useMemo(() => {
    if (!routine?.idRoutine) {
      return [];
    }

    return routineExercises
      .filter((item) => item.routineId === routine.idRoutine)
      .sort((a, b) => (a.exerciseOrder ?? 9999) - (b.exerciseOrder ?? 9999))
      .map((item) => ({
        exerciseId: item.exerciseId,
        sets: item.sets,
        targetReps: item.targetReps,
        exerciseOrder: item.exerciseOrder,
      }));
  }, [routine, routineExercises]);

  const [name, setName] = useState(routine?.name ?? "");
  const [description, setDescription] = useState(routine?.description ?? "");
  const [creationDate, setCreationDate] = useState(
    routine?.creationDate ?? new Date().toISOString().split("T")[0],
  );
  const [drafts, setDrafts] = useState<RoutineExerciseDraft[]>(initialDrafts);

  const handleSave = async () => {
    if (!name.trim() || !creationDate.trim()) {
      alert("El nombre y la fecha son obligatorios.");
      return;
    }

    const payload = buildRoutinePayload({
      name,
      description,
      creationDate,
      currentUserId,
      isTrainer,
    });

    await onSave(payload, drafts, routine?.idRoutine);
  };

  return (
    <>
      <Dialog 
        open={open} 
        onClose={onClose} 
        fullWidth 
        maxWidth="md"
        sx={{ '& .MuiDialog-paper': { borderRadius: 3 } }}
      >
        <DialogTitle sx={{ fontWeight: 'bold' }}>{routine ? "Editar rutina" : "Crear rutina"}</DialogTitle>

        <DialogContent dividers>
          <Stack spacing={3} sx={{ mt: 1 }}>
            <TextField
              label="Nombre de la rutina"
              fullWidth
              value={name}
              onChange={(e) => setName(e.target.value)}
            />

            <TextField
              label="Descripción / Objetivo"
              fullWidth
              multiline
              minRows={3}
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />

            <TextField
              label="Fecha"
              type="date"
              fullWidth
              value={creationDate}
              onChange={(e) => setCreationDate(e.target.value)}
              slotProps={{ inputLabel: { shrink: true } }}
            />

            <RoutineExercisesBuilder
              exercises={sortedExercises}
              drafts={drafts}
              selectedExerciseId={selectedExerciseId}
              onSelectedExerciseChange={setSelectedExerciseId}
              onDraftsChange={setDrafts}
              onCreateCustomExercise={() => setOpenCustomExercise(true)}
            />
          </Stack>
        </DialogContent>

        <DialogActions sx={{ p: 2.5 }}>
          <Button onClick={onClose} sx={{ fontWeight: 600 }}>Cancelar</Button>
          <Button variant="contained" onClick={handleSave} sx={{ borderRadius: 2, fontWeight: 700, px: 3 }}>
            {routine ? "Guardar cambios" : "Crear rutina"}
          </Button>
        </DialogActions>
      </Dialog>

      <CustomExerciseDialog
        open={openCustomExercise}
        currentUserId={currentUserId}
        isTrainer={isTrainer}
        onClose={() => setOpenCustomExercise(false)}
        onSave={onCreateCustomExercise}
      />
    </>
  );
};

export default RoutineFormDialog;

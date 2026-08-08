import { useState } from "react";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogTitle from "@mui/material/DialogTitle";
import Stack from "@mui/material/Stack";
import DeleteOutlineRoundedIcon from "@mui/icons-material/DeleteOutlineRounded";
import EditOutlinedIcon from "@mui/icons-material/EditOutlined";
import type {
  ActivityRequest,
  ActivityResponse,
  SpaceResponse,
} from "../../types/api.types";
import ActivityEditForm from "./ActivityEditForm";
import ActivityReadOnlyDetails from "./ActivityReadOnlyDetails";

type Props = {
  open: boolean;
  activity: ActivityResponse | null;
  spaces: SpaceResponse[];
  spaceMap: Map<number, SpaceResponse>;
  onClose: () => void;
  onSave: (id: number, payload: ActivityRequest) => Promise<void>;
  onDelete: (id: number) => Promise<void>;
};

const ActivityDetailsDialog = ({
  open,
  activity,
  spaces,
  spaceMap,
  onClose,
  onSave,
  onDelete,
}: Props) => {
  const [isEditing, setIsEditing] = useState(false);
  const [name, setName] = useState(activity?.name ?? "");
  const [description, setDescription] = useState(activity?.description ?? "");
  const [startDate, setStartDate] = useState(activity?.startDate ?? "");
  const [endDate, setEndDate] = useState(activity?.endDate ?? "");
  const [spaceId, setSpaceId] = useState(
    activity?.spaceId ? String(activity.spaceId) : "",
  );

  if (!activity) {
    return null;
  }

  const handleSave = async () => {
    if (!name.trim() || !startDate.trim() || !endDate.trim()) {
      return;
    }

    const payload: ActivityRequest = {
      name: name.trim(),
      description: description.trim() || undefined,
      startDate,
      endDate,
      spaceId: spaceId ? Number(spaceId) : undefined,
    };

    await onSave(activity.idActivity, payload);
    setIsEditing(false);
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <DialogTitle>
        {isEditing ? "Editar actividad" : activity.name}
      </DialogTitle>

      <DialogContent dividers sx={{ pt: 2 }}>
        {isEditing ? (
          <ActivityEditForm
            name={name}
            setName={setName}
            description={description}
            setDescription={setDescription}
            startDate={startDate}
            setStartDate={setStartDate}
            endDate={endDate}
            setEndDate={setEndDate}
            spaceId={spaceId}
            setSpaceId={setSpaceId}
            spaces={spaces}
          />
        ) : (
          <ActivityReadOnlyDetails activity={activity} spaceMap={spaceMap} />
        )}
      </DialogContent>

      <DialogActions sx={{ justifyContent: "space-between" }}>
        <Button
          color="error"
          startIcon={<DeleteOutlineRoundedIcon />}
          onClick={() => onDelete(activity.idActivity)}
        >
          Eliminar
        </Button>

        <Stack direction="row" spacing={1}>
          <Button onClick={onClose}>Cerrar</Button>

          {isEditing ? (
            <Button variant="contained" onClick={handleSave}>
              Guardar cambios
            </Button>
          ) : (
            <Button
              variant="contained"
              startIcon={<EditOutlinedIcon />}
              onClick={() => setIsEditing(true)}
            >
              Editar
            </Button>
          )}
        </Stack>
      </DialogActions>
    </Dialog>
  );
};

export default ActivityDetailsDialog;

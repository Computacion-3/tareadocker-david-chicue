import { useState } from "react";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogTitle from "@mui/material/DialogTitle";
import Stack from "@mui/material/Stack";
import type { SpaceRequest, SpaceResponse } from "../../types/api.types";
import SpaceEditForm from "./SpaceEditForm";
import SpaceReadOnlyDetails from "./SpaceReadOnlyDetails";

import DeleteOutlineRoundedIcon from "@mui/icons-material/DeleteOutlineRounded";
import EditOutlinedIcon from "@mui/icons-material/EditOutlined";

type Props = {
  open: boolean;
  space: SpaceResponse | null;
  onClose: () => void;
  onSave: (id: number, payload: SpaceRequest) => Promise<void>;
  onDelete: (id: number) => Promise<void>;
};

const SpaceDetailsDialog = ({
  open,
  space,
  onClose,
  onSave,
  onDelete,
}: Props) => {
  const [isEditing, setIsEditing] = useState(false);
  const [name, setName] = useState(space?.name ?? "");
  const [capacity, setCapacity] = useState(
    space?.capacity ? String(space.capacity) : "",
  );
  const [location, setLocation] = useState(space?.location ?? "");

  if (!space) {
    return null;
  }

  const handleSave = async () => {
    if (!name.trim()) {
      return;
    }

    const payload: SpaceRequest = {
      name: name.trim(),
      capacity: capacity.trim() ? Number(capacity) : undefined,
      location: location.trim() || undefined,
    };

    await onSave(space.idSpace, payload);
    setIsEditing(false);
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <DialogTitle>{isEditing ? "Editar espacio" : space.name}</DialogTitle>

      <DialogContent dividers sx={{ pt: 2 }}>
        {isEditing ? (
          <SpaceEditForm
            name={name}
            setName={setName}
            capacity={capacity}
            setCapacity={setCapacity}
            location={location}
            setLocation={setLocation}
          />
        ) : (
          <SpaceReadOnlyDetails space={space} />
        )}
      </DialogContent>

      <DialogActions sx={{ justifyContent: "space-between" }}>
        <Button
          color="error"
          startIcon={<DeleteOutlineRoundedIcon />}
          onClick={() => onDelete(space.idSpace)}
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

export default SpaceDetailsDialog;

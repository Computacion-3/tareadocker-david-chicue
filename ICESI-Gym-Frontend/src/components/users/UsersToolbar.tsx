import {
  Button,
  InputAdornment,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import SearchIcon from "@mui/icons-material/Search";

type Props = {
  search: string;
  onSearchChange: (value: string) => void;
  onCreateUser: () => void;
};

const UsersToolbar = ({ search, onSearchChange, onCreateUser }: Props) => {
  return (
    <Stack
      direction={{ xs: "column", md: "row" }}
      sx={{
        justifyContent: "space-between",
        alignItems: { xs: "stretch", md: "center" },
        gap: 2,
      }}
    >
      <Typography variant="h5" sx={{ fontWeight: "bold" }}>
        Gestión de usuarios
      </Typography>

      <Stack direction={{ xs: "column", md: "row" }} spacing={2}>
        <TextField
          placeholder="Buscar por nombre o correo"
          value={search}
          onChange={(e) => onSearchChange(e.target.value)}
          sx={{ minWidth: { xs: "100%", md: 320 } }}
          slotProps={{
            input: {
              startAdornment: (
                <InputAdornment position="start">
                  <SearchIcon />
                </InputAdornment>
              ),
            },
          }}
        />

        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={onCreateUser}
          sx={{ borderRadius: 2, fontWeight: 700 }}
        >
          Crear usuario
        </Button>
      </Stack>
    </Stack>
  );
};

export default UsersToolbar;

import { Checkbox, FormControlLabel, Stack, Typography, Box } from "@mui/material";
import type { RoleResponse } from "../../types/api.types";

type Props = {
  roles: RoleResponse[];
  selectedRoleIds: number[];
  onToggleRole: (roleId: number) => void;
};

const RolesSelector = ({ roles, selectedRoleIds, onToggleRole }: Props) => {
  return (
    <Box>
      <Typography variant="subtitle1" sx={{ fontWeight: "bold", mb: 1 }}>
        Roles disponibles
      </Typography>

      <Stack>
        {roles.map((role) => (
          <FormControlLabel
            key={role.id}
            control={
              <Checkbox
                checked={selectedRoleIds.includes(Number(role.id))}
                onChange={() => onToggleRole(Number(role.id))}
              />
            }
            label={role.name}
          />
        ))}
      </Stack>
    </Box>
  );
};

export default RolesSelector;

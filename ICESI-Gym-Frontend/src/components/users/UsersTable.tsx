import {
  Button,
  Chip,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  Typography,
  useMediaQuery,
  useTheme,
  Card,
  CardContent,
  CardActions,
  Box,
} from "@mui/material";
import ManageAccountsIcon from "@mui/icons-material/ManageAccounts";
import type { RoleResponse, UserResponse } from "../../types/api.types";

type Props = {
  users: UserResponse[];
  roleMap: Map<number, RoleResponse>;
  userRoleIdsMap: Map<number, number[]>;
  onAssignRoles: (user: UserResponse) => void;
};

const UsersTable = ({
  users,
  roleMap,
  userRoleIdsMap,
  onAssignRoles,
}: Props) => {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down("md"));

  if (isMobile) {
    return (
      <Box>
        {users.map((user) => {
          const assignedRoleIds = userRoleIdsMap.get(user.idUser) ?? [];
          return (
            <Card key={user.idUser} sx={{ mb: 2, border: '1px solid', borderColor: 'divider' }} elevation={0}>
              <CardContent>
                <Typography variant="h6" sx={{ fontWeight: 'bold', mb: 1 }}>
                  {user.firstName} {user.lastName}
                </Typography>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                  {user.institutionalEmail}
                </Typography>
                <Stack direction="row" spacing={1} sx={{ flexWrap: "wrap", mb: 2 }}>
                  {assignedRoleIds.length > 0 ? (
                    assignedRoleIds.map((roleId) => {
                      const role = roleMap.get(roleId);
                      return (
                        <Chip
                          key={roleId}
                          label={role ? role.name : `Rol ${roleId}`}
                          size="small"
                          sx={{ mb: 1 }}
                        />
                      );
                    })
                  ) : (
                    <Typography variant="body2" color="text.secondary">
                      Sin roles
                    </Typography>
                  )}
                </Stack>
              </CardContent>
              <CardActions sx={{ justifyContent: 'flex-end', p: 2, pt: 0 }}>
                <Button
                  variant="outlined"
                  startIcon={<ManageAccountsIcon />}
                  onClick={() => onAssignRoles(user)}
                  sx={{ borderRadius: 2 }}
                >
                  Asignar roles
                </Button>
              </CardActions>
            </Card>
          );
        })}
      </Box>
    );
  }

  return (
    <Table>
      <TableHead>
        <TableRow>
          <TableCell>
            <strong>Nombre</strong>
          </TableCell>
          <TableCell>
            <strong>Apellido</strong>
          </TableCell>
          <TableCell>
            <strong>Correo institucional</strong>
          </TableCell>
          <TableCell>
            <strong>Edad</strong>
          </TableCell>
          <TableCell>
            <strong>Roles</strong>
          </TableCell>
          <TableCell align="right">
            <strong>Acciones</strong>
          </TableCell>
        </TableRow>
      </TableHead>

      <TableBody>
        {users.map((user) => {
          const assignedRoleIds = userRoleIdsMap.get(user.idUser) ?? [];

          return (
            <TableRow key={user.idUser} hover>
              <TableCell>{user.firstName}</TableCell>
              <TableCell>{user.lastName}</TableCell>
              <TableCell>{user.institutionalEmail}</TableCell>
              <TableCell>{user.age ?? "-"}</TableCell>

              <TableCell>
                <Stack direction="row" spacing={1} sx={{ flexWrap: "wrap" }}>
                  {assignedRoleIds.length > 0 ? (
                    assignedRoleIds.map((roleId) => {
                      const role = roleMap.get(roleId);

                      return (
                        <Chip
                          key={roleId}
                          label={role ? role.name : `Rol ${roleId}`}
                          size="small"
                          sx={{ mb: 1 }}
                        />
                      );
                    })
                  ) : (
                    <Typography variant="body2" color="text.secondary">
                      Sin roles asignados
                    </Typography>
                  )}
                </Stack>
              </TableCell>

              <TableCell align="right">
                <Button
                  variant="outlined"
                  startIcon={<ManageAccountsIcon />}
                  onClick={() => onAssignRoles(user)}
                  sx={{ borderRadius: 2 }}
                >
                  Asignar roles
                </Button>
              </TableCell>
            </TableRow>
          );
        })}
      </TableBody>
    </Table>
  );
};

export default UsersTable;

import DeleteIcon from "@mui/icons-material/Delete";
import {
  Box,
  Card,
  CardActions,
  CardContent,
  IconButton,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  Typography,
  useMediaQuery,
  useTheme,
} from "@mui/material";
import type { AssignmentResponse, UserResponse } from "../../types/api.types";

type Props = {
  assignments: AssignmentResponse[];
  userMap: Map<number, UserResponse>;
  onDelete: (userId: number, trainerId: number) => void;
};

const AssignmentsTable = ({ assignments, userMap, onDelete }: Props) => {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down("md"));

  const getUserName = (id: number) => {
    const user = userMap.get(id);
    return user ? `${user.firstName} ${user.lastName}` : `ID: ${id}`;
  };

  if (isMobile) {
    return (
      <Box>
        {assignments.length === 0 ? (
          <Typography variant="body2" color="text.secondary" align="center" sx={{ py: 3 }}>
            No hay asignaciones registradas.
          </Typography>
        ) : (
          assignments.map((assignment, index) => (
            <Card key={`${assignment.userId}-${assignment.trainerId}-${index}`} sx={{ mb: 2 }}>
              <CardContent>
                <Typography variant="h6" sx={{ fontWeight: 600 }}>
                  Usuario: {getUserName(assignment.userId)}
                </Typography>
                <Typography variant="body1" color="text.secondary">
                  Entrenador: {getUserName(assignment.trainerId)}
                </Typography>
                <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
                  Fecha: {new Date(assignment.assignmentDate).toLocaleDateString()}
                </Typography>
              </CardContent>
              <CardActions>
                <IconButton
                  color="error"
                  onClick={() => onDelete(assignment.userId, assignment.trainerId)}
                >
                  <DeleteIcon />
                </IconButton>
              </CardActions>
            </Card>
          ))
        )}
      </Box>
    );
  }

  return (
    <Table>
      <TableHead>
        <TableRow>
          <TableCell>
            <strong>Usuario</strong>
          </TableCell>
          <TableCell>
            <strong>Entrenador</strong>
          </TableCell>
          <TableCell>
            <strong>Fecha de Asignación</strong>
          </TableCell>
          <TableCell align="right">
            <strong>Acciones</strong>
          </TableCell>
        </TableRow>
      </TableHead>

      <TableBody>
        {assignments.length === 0 ? (
          <TableRow>
            <TableCell colSpan={4} align="center" sx={{ py: 3 }}>
              <Typography variant="body2" color="text.secondary">
                No hay asignaciones registradas.
              </Typography>
            </TableCell>
          </TableRow>
        ) : (
          assignments.map((assignment, index) => (
            <TableRow key={`${assignment.userId}-${assignment.trainerId}-${index}`} hover>
              <TableCell sx={{ fontWeight: 600 }}>{getUserName(assignment.userId)}</TableCell>
              <TableCell>{getUserName(assignment.trainerId)}</TableCell>
              <TableCell>{new Date(assignment.assignmentDate).toLocaleDateString()}</TableCell>
              <TableCell align="right">
                <IconButton
                  color="error"
                  onClick={() => onDelete(assignment.userId, assignment.trainerId)}
                >
                  <DeleteIcon />
                </IconButton>
              </TableCell>
            </TableRow>
          ))
        )}
      </TableBody>
    </Table>
  );
};

export default AssignmentsTable;

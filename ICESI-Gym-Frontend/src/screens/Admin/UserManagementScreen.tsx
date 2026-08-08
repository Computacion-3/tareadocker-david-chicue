import { useCallback, useEffect, useMemo, useState } from "react";
import { Box, CircularProgress, Paper, Stack, Typography } from "@mui/material";
import { getAllRoles } from "../../services/RoleService";
import { createUser, getAllUsers } from "../../services/UserService";
import {
  createUserRole,
  deleteUserRole,
  getAllUserRoles,
} from "../../services/UserRoleService";
import type {
  RoleResponse,
  UserRequest,
  UserResponse,
  UserRoleResponse,
} from "../../types/api.types";
import { useAppDispatch } from "../../hooks/useDispatch";
import { showSnackbar } from "../../store/slices/uiSlice";
import UsersToolbar from "../../components/users/UsersToolbar";
import UsersTable from "../../components/users/UsersTable";
import AssignRolesDialog from "../../components/users/AssignRolesDialog";
import UserCreateDialog from "../../components/users/UserCreateDialog";

const UsersManagementScreen = () => {
  const dispatch = useAppDispatch();

  const [users, setUsers] = useState<UserResponse[]>([]);
  const [roles, setRoles] = useState<RoleResponse[]>([]);
  const [userRoles, setUserRoles] = useState<UserRoleResponse[]>([]);
  const [loading, setLoading] = useState(true);

  const [search, setSearch] = useState("");

  const [openAssignRoles, setOpenAssignRoles] = useState(false);
  const [selectedUser, setSelectedUser] = useState<UserResponse | null>(null);
  const [selectedRoleIds, setSelectedRoleIds] = useState<number[]>([]);

  const [openCreateUser, setOpenCreateUser] = useState(false);
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [institutionalEmail, setInstitutionalEmail] = useState("");
  const [password, setPassword] = useState("");
  const [age, setAge] = useState("");

  const notify = useCallback(
    (message: string, severity: "success" | "error") => {
      dispatch(showSnackbar({ message, severity }));
    },
    [dispatch],
  );

  const loadData = useCallback(async () => {
    try {
      setLoading(true);

      const [usersRes, rolesRes, userRolesRes] = await Promise.all([
        getAllUsers(),
        getAllRoles(),
        getAllUserRoles(),
      ]);

      if (usersRes.error || rolesRes.error || userRolesRes.error) {
        notify("No se pudo cargar la información de usuarios", "error");
        return;
      }

      setUsers(usersRes.data ?? []);
      setRoles(rolesRes.data ?? []);
      setUserRoles(userRolesRes.data ?? []);
    } catch {
      notify("No se pudo cargar la información de usuarios", "error");
    } finally {
      setLoading(false);
    }
  }, [notify]);

  useEffect(() => {
    let isMounted = true;

    const fetchInitialData = async () => {
      try {
        setLoading(true);

        const [usersRes, rolesRes, userRolesRes] = await Promise.all([
          getAllUsers(),
          getAllRoles(),
          getAllUserRoles(),
        ]);

        if (!isMounted) {
          return;
        }

        if (usersRes.error || rolesRes.error || userRolesRes.error) {
          notify("No se pudo cargar la información de usuarios", "error");
          return;
        }

        setUsers(usersRes.data ?? []);
        setRoles(rolesRes.data ?? []);
        setUserRoles(userRolesRes.data ?? []);
      } catch {
        if (isMounted) {
          notify("No se pudo cargar la información de usuarios", "error");
        }
      } finally {
        if (isMounted) {
          setLoading(false);
        }
      }
    };

    void fetchInitialData();

    return () => {
      isMounted = false;
    };
  }, [notify]);

  const roleMap = useMemo(() => {
    return new Map(roles.map((role) => [Number(role.id), role]));
  }, [roles]);

  const userRoleIdsMap = useMemo(() => {
    const map = new Map<number, number[]>();

    userRoles.forEach((assignment) => {
      const userId = assignment.userId;
      const roleId = Number(assignment.roleId);
      const current = map.get(userId) ?? [];
      current.push(roleId);
      map.set(userId, current);
    });

    return map;
  }, [userRoles]);

  const filteredUsers = useMemo(() => {
    const term = search.trim().toLowerCase();

    const sortedUsers = [...users].sort((a, b) =>
      `${a.firstName} ${a.lastName}`.localeCompare(
        `${b.firstName} ${b.lastName}`,
      ),
    );

    if (!term) {
      return sortedUsers;
    }

    return sortedUsers.filter((user) => {
      const fullName = `${user.firstName} ${user.lastName}`.toLowerCase();
      const email = user.institutionalEmail.toLowerCase();

      return fullName.includes(term) || email.includes(term);
    });
  }, [search, users]);

  const resetCreateUserForm = useCallback(() => {
    setFirstName("");
    setLastName("");
    setInstitutionalEmail("");
    setPassword("");
    setAge("");
  }, []);

  const handleOpenCreateUser = useCallback(() => {
    resetCreateUserForm();
    setOpenCreateUser(true);
  }, [resetCreateUserForm]);

  const handleCloseCreateUser = useCallback(() => {
    setOpenCreateUser(false);
    resetCreateUserForm();
  }, [resetCreateUserForm]);

  const handleOpenAssignRoles = useCallback(
    (user: UserResponse) => {
      setSelectedUser(user);
      setSelectedRoleIds(userRoleIdsMap.get(user.idUser) ?? []);
      setOpenAssignRoles(true);
    },
    [userRoleIdsMap],
  );

  const handleCloseAssignRoles = useCallback(() => {
    setOpenAssignRoles(false);
    setSelectedUser(null);
    setSelectedRoleIds([]);
  }, []);

  const toggleRole = useCallback((roleId: number) => {
    setSelectedRoleIds((prev) =>
      prev.includes(roleId)
        ? prev.filter((id) => id !== roleId)
        : [...prev, roleId],
    );
  }, []);

  const handleCreateUser = useCallback(async () => {
    if (
      !firstName.trim() ||
      !lastName.trim() ||
      !institutionalEmail.trim() ||
      !age.trim()
    ) {
      notify("Todos los campos del usuario son obligatorios", "error");
      return;
    }

    const ageNumber = Number(age);

    if (Number.isNaN(ageNumber) || ageNumber <= 0) {
      notify("La edad debe ser un número válido", "error");
      return;
    }

    const payload: UserRequest = {
      firstName: firstName.trim(),
      lastName: lastName.trim(),
      institutionalEmail: institutionalEmail.trim(),
      password: password.trim(),
      age: ageNumber,
    };

    try {
      const res = await createUser(payload);

      if (res.error) {
        notify(res.message || "No se pudo crear el usuario", "error");
        return;
      }

      notify("Usuario creado correctamente", "success");
      handleCloseCreateUser();
      await loadData();
    } catch {
      notify("No se pudo crear el usuario", "error");
    }
  }, [
    firstName,
    lastName,
    institutionalEmail,
    password,
    age,
    notify,
    handleCloseCreateUser,
    loadData,
  ]);

  const handleSaveRoles = useCallback(async () => {
    if (!selectedUser) {
      return;
    }

    const currentRoleIds = userRoleIdsMap.get(selectedUser.idUser) ?? [];

    const rolesToAdd = selectedRoleIds.filter(
      (roleId) => !currentRoleIds.includes(roleId),
    );

    const rolesToRemove = currentRoleIds.filter(
      (roleId) => !selectedRoleIds.includes(roleId),
    );

    try {
      const results = await Promise.all([
        ...rolesToAdd.map((roleId) =>
          createUserRole({
            userId: selectedUser.idUser,
            roleId,
          }),
        ),
        ...rolesToRemove.map((roleId) =>
          deleteUserRole(selectedUser.idUser, roleId),
        ),
      ]);

      const hasError = results.some((res) => res.error);

      if (hasError) {
        notify("Algunos roles no pudieron ser actualizados", "error");
      } else {
        notify("Roles asignados correctamente", "success");
      }

      handleCloseAssignRoles();
      await loadData();
    } catch {
      notify("No se pudieron actualizar los roles del usuario", "error");
    }
  }, [
    selectedRoleIds,
    selectedUser,
    userRoleIdsMap,
    notify,
    handleCloseAssignRoles,
    loadData,
  ]);

  if (loading) {
    return (
      <Stack sx={{ alignItems: "center", justifyContent: "center", py: 8 }}>
        <CircularProgress thickness={5} />
      </Stack>
    );
  }

  return (
    <Box>
      <Paper 
        elevation={0} 
        sx={{ 
          p: { xs: 2, md: 3 }, 
          borderRadius: 4, 
          border: '1px solid',
          borderColor: 'divider',
          boxShadow: '0 4px 20px 0 rgba(0,0,0,0.05)'
        }}
      >
        <Stack spacing={3}>
          <UsersToolbar
            search={search}
            onSearchChange={setSearch}
            onCreateUser={handleOpenCreateUser}
          />

          <UsersTable
            users={filteredUsers}
            roleMap={roleMap}
            userRoleIdsMap={userRoleIdsMap}
            onAssignRoles={handleOpenAssignRoles}
          />

          {filteredUsers.length === 0 && (
            <Typography variant="body2" color="text.secondary" sx={{ textAlign: 'center', py: 4 }}>
              No se encontraron usuarios con ese criterio de búsqueda.
            </Typography>
          )}
        </Stack>
      </Paper>

      <UserCreateDialog
        open={openCreateUser}
        firstName={firstName}
        setFirstName={setFirstName}
        lastName={lastName}
        setLastName={setLastName}
        institutionalEmail={institutionalEmail}
        setInstitutionalEmail={setInstitutionalEmail}
        password={password}
        setPassword={setPassword}
        age={age}
        setAge={setAge}
        onClose={handleCloseCreateUser}
        onSave={handleCreateUser}
      />

      <AssignRolesDialog
        open={openAssignRoles}
        selectedUser={selectedUser}
        roles={roles}
        selectedRoleIds={selectedRoleIds}
        onToggleRole={toggleRole}
        onClose={handleCloseAssignRoles}
        onSave={handleSaveRoles}
      />
    </Box>
  );
};

export default UsersManagementScreen;

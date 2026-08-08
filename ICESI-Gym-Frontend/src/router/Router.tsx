import { createBrowserRouter } from "react-router";
import LoginScreen from "../screens/auth/LoginScreen";
import RealTimeWrapper from "../components/wrapper/RealTimeWrapper";
import ProtectedRoute from "../components/wrapper/ProtectedRoute";
import RegisterScreen from "../screens/auth/RegisterScreen";
import MainLayout from "../screens/auth/MainLayout";
import HomeScreen from "../screens/auth/HomeScreen";
import ProgressScreen from "../screens/progress/ProgressScreen";
import ProfileScreen from "../screens/profile/ProfileScreen";
import RolesScreen from "../screens/Admin/RoleManagementScreen";
import PoliciesScreen from "../screens/Admin/PolicyManagementScreen";
import UsersManagementScreen from "../screens/Admin/UserManagementScreen";
import EditProfileScreen from "../screens/profile/EditProfileScreen";
import ActivitiesScreen from "../screens/activities/ActivitiesScreen";
import ActivitieDetailScreen from "../screens/activities/ActivitieDetailScreen";
import AssignmentsManagementScreen from "../screens/Admin/AssignmentsManagementScreen";
import ExerciseManagementScreen from "../screens/Admin/ExerciseManagementScreen";
import MessagesScreen from "../screens/messages/MessagesScreen";
import NotificationsScreen from "../screens/notifications/NotificationsScreen";
import RecommendationsScreen from "../screens/recommendations/RecommendationsScreen";
import RoutineDetailScreen from "../screens/routines/RoutineDetailScreen";
import SchedulesScreen from "../screens/schedules/SchedulesScreen";
import SpacesScreen from "../screens/spaces/SpacesScreen";
import TrainerTraineesScreen from "../screens/trainer/TrainerTraineesScreen";
import HistoryScreen from "../screens/profile/HistoryScreen";
import NotFoundScreen from "../screens/common/NotFoundScreen";
import UnauthorizedScreen from "../screens/common/UnauthorizedScreen";
import RoutinesScreen from "../screens/routines/RoutinesScreen";

const router = createBrowserRouter(
  [
    {
      path: "/login",
      element: <LoginScreen />,
    },
    {
      path: "/register",
      element: <RegisterScreen />,
    },
    {
      path: "/unauthorized",
      element: <UnauthorizedScreen />,
    },
    {
      path: "/",
      element: <ProtectedRoute />,
      children: [
        {
          element: (
            <RealTimeWrapper>
              <MainLayout />
            </RealTimeWrapper>
          ),
          children: [
            { index: true, element: <HomeScreen /> },
            { path: "/profile", element: <ProfileScreen /> },
            { path: "/profile/edit", element: <EditProfileScreen /> },
            { path: "/routines", element: <RoutinesScreen /> },
            { path: "/routines/:id", element: <RoutineDetailScreen /> },
            { path: "/activities", element: <ActivitiesScreen /> },
            { path: "/activities/:id", element: <ActivitieDetailScreen /> },
            { path: "/progress", element: <ProgressScreen /> },
            { path: "/messages", element: <MessagesScreen /> },
            { path: "/notifications", element: <NotificationsScreen /> },
            { path: "/recommendations", element: <RecommendationsScreen /> },
            { path: "/schedules", element: <SchedulesScreen /> },
            { path: "/spaces", element: <SpacesScreen /> },
            { path: "/exercises", element: <ExerciseManagementScreen /> },
            { path: "/trainees", element: <TrainerTraineesScreen /> },
            { path: "/history", element: <HistoryScreen /> },
            // Admin Routes
            {
              element: <ProtectedRoute requiredRoles={["ADMIN"]} />,
              children: [
                { path: "/roles", element: <RolesScreen /> },
                { path: "/policies", element: <PoliciesScreen /> },
                { path: "/users", element: <UsersManagementScreen /> },
                { path: "/assignments", element: <AssignmentsManagementScreen /> },
              ]
            },
          ],
        },
      ],
    },
    {
      path: "*",
      element: <NotFoundScreen />,
    },
  ]
);

export default router;

import type {
  ExerciseRequest,
  ExerciseResponse,
  RoutineExerciseRequest,
  RoutineExerciseResponse,
  RoutineRequest,
  RoutineResponse,
} from "../types/api.types";

export type AuthRoutineUser = {
  idUser?: number;
  authorities?: string[];
};

export type RoutineExerciseDraft = {
  exerciseId: number;
  sets: number;
  targetReps: number;
  exerciseOrder: number;
};

export const getUserAuthorities = (user: AuthRoutineUser | null): string[] => {
  return user?.authorities ?? [];
};

export const hasAuthority = (
  authorities: string[],
  values: string[],
): boolean => {
  return values.some((value) => authorities.includes(value));
};

export const getRoutineRoleFlags = (user: AuthRoutineUser | null) => {
  const authorities = getUserAuthorities(user);

  const isAdmin = hasAuthority(authorities, ["ADMIN", "ROLE_ADMIN"]);
  const isTrainer = hasAuthority(authorities, ["TRAINER", "ROLE_TRAINER"]);
  const isTrainee = hasAuthority(authorities, ["TRAINEE", "ROLE_TRAINEE"]);

  return {
    isAdmin,
    isTrainer,
    isTrainee,
    authorities,
  };
};

export const getCurrentUserId = (
  user: AuthRoutineUser | null,
): number | null => {
  return user?.idUser ?? null;
};

export const getVisibleRoutines = (
  routines: RoutineResponse[],
  currentUserId: number | null,
  isAdmin: boolean,
): RoutineResponse[] => {
  if (isAdmin) {
    return routines;
  }

  if (!currentUserId) {
    return [];
  }

  return routines.filter((routine) => routine.userId === currentUserId);
};

export const getTemplateRoutines = (
  routines: RoutineResponse[],
): RoutineResponse[] => {
  return routines.filter((routine) => routine.isPredesigned === true);
};

export const buildRoutinePayload = ({
  name,
  description,
  creationDate,
  currentUserId,
  isTrainer,
}: {
  name: string;
  description: string;
  creationDate: string;
  currentUserId: number | null;
  isTrainer: boolean;
}): RoutineRequest => {
  return {
    name: name.trim(),
    description: description.trim() || undefined,
    creationDate,
    isPredesigned: isTrainer,
    userId: currentUserId ?? undefined,
  };
};

export const buildCustomExercisePayload = ({
  name,
  type,
  description,
  durationMin,
  difficulty,
  videoUrl,
  currentUserId,
  isTrainer,
}: {
  name: string;
  type: string;
  description: string;
  durationMin: string;
  difficulty: string;
  videoUrl: string;
  currentUserId: number | null;
  isTrainer: boolean;
}): ExerciseRequest => {
  return {
    name: name.trim(),
    type: type.trim(),
    description: description.trim() || undefined,
    durationMin: durationMin.trim() ? Number(durationMin) : undefined,
    difficulty: difficulty.trim() || undefined,
    videoUrl: videoUrl.trim() || undefined,
    isPredefined: isTrainer,
    userId: currentUserId ?? undefined,
  };
};

export const createDraftFromExercise = (
  exerciseId: number,
  currentCount: number,
): RoutineExerciseDraft => {
  return {
    exerciseId,
    sets: 3,
    targetReps: 10,
    exerciseOrder: currentCount + 1,
  };
};

export const mapDraftToRoutineExerciseRequest = (
  routineId: number,
  draft: RoutineExerciseDraft,
): RoutineExerciseRequest => {
  return {
    routineId,
    exerciseId: draft.exerciseId,
    sets: draft.sets,
    targetReps: draft.targetReps,
    exerciseOrder: draft.exerciseOrder,
  };
};

export const getRoutineTemplateExercises = (
  routineExercises: RoutineExerciseResponse[],
  templateId: number,
): RoutineExerciseResponse[] => {
  return routineExercises
    .filter((item) => item.routineId === templateId)
    .sort((a, b) => (a.exerciseOrder ?? 9999) - (b.exerciseOrder ?? 9999));
};

export const buildClonedRoutinePayload = ({
  template,
  currentUserId,
}: {
  template: RoutineResponse;
  currentUserId: number | null;
}): RoutineRequest => {
  return {
    name: template.name,
    description: template.description,
    creationDate: new Date().toISOString().split("T")[0],
    isPredesigned: false,
    userId: currentUserId ?? undefined,
  };
};

export const sortRoutinesByName = (
  routines: RoutineResponse[],
): RoutineResponse[] => {
  return [...routines].sort((a, b) => a.name.localeCompare(b.name));
};

export const sortExercisesByName = (
  exercises: ExerciseResponse[],
): ExerciseResponse[] => {
  return [...exercises].sort((a, b) => a.name.localeCompare(b.name));
};

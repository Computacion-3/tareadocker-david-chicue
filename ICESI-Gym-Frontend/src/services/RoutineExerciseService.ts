import axiosClient, { safeRequest } from '../lib/axios/axiosClient';
import type { RoutineExerciseRequest, RoutineExerciseResponse } from '../types/api.types';

export const getAllRoutineExercises = async () => {
    return safeRequest<RoutineExerciseResponse[]>(
        axiosClient.get('/api/v1/routine-exercises')
    );
};

export const createRoutineExercise = async (request: RoutineExerciseRequest) => {
    return safeRequest<RoutineExerciseResponse>(
        axiosClient.post('/api/v1/routine-exercises', request)
    );
};


// Based on typical join table patterns
export const deleteRoutineExercise = async (id: number) => {
    return safeRequest<void>(
        axiosClient.delete(`/api/v1/routine-exercises/${id}`)
    );
};

export const assignExercisesToRoutine = async (routineId: number, exerciseIds: number[]) => {
    return safeRequest<RoutineExerciseResponse[]>(
        axiosClient.post(`/api/v1/routine-exercises/routine/${routineId}/exercises`, exerciseIds)
    );
};
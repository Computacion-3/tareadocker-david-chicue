import axiosClient, { safeRequest } from '../lib/axios/axiosClient';
import type { RoutineRequest, RoutineResponse, RoutineExerciseResponse, ExerciseResponse } from '../types/api.types';
import { getAllRoutineExercises } from './RoutineExerciseService';
import { getExerciseById } from './ExerciseService';

export interface RoutineExerciseWithExercise extends RoutineExerciseResponse {
    exercise: ExerciseResponse;
}

export interface RoutineWithExercises extends RoutineResponse {
    routineExercises: RoutineExerciseWithExercise[];
}

export const getAllRoutines = async () => {
    return safeRequest<RoutineResponse[]>(
        axiosClient.get('/api/v1/routines')
    );
};

export const getRoutineById = async (id: number) => {
    const result = await safeRequest<RoutineResponse>(
        axiosClient.get(`/api/v1/routines/${id}`)
    );

    if (result.error || !result.data) {
        return result;
    }

    // Fetch all exercises for this routine
    const exercisesResult = await getAllRoutineExercises();
    if (exercisesResult.error || !exercisesResult.data) {
        return { ...result, data: { ...result.data, routineExercises: [] } as RoutineWithExercises };
    }

    const relevantExercises = exercisesResult.data.filter(re => re.routineId === id);
    
    // Enrich each link with exercise details
    const enrichedExercises: RoutineExerciseWithExercise[] = await Promise.all(
        relevantExercises.map(async (re) => {
            const exResult = await getExerciseById(re.exerciseId);
            return { ...re, exercise: exResult.data as ExerciseResponse };
        })
    );

    const enrichedRoutine: RoutineWithExercises = {
        ...result.data,
        routineExercises: enrichedExercises
    };

    return { ...result, data: enrichedRoutine };
};

export const createRoutine = async (request: RoutineRequest) => {
    return safeRequest<RoutineResponse>(
        axiosClient.post('/api/v1/routines', request)
    );
};

export const updateRoutine = async (id: number, request: RoutineRequest) => {
    return safeRequest<RoutineResponse>(
        axiosClient.put(`/api/v1/routines/${id}`, request)
    );
};

export const adoptRoutine = async (id: number) => {
    return safeRequest<RoutineResponse>(
        axiosClient.post(`/api/v1/routines/${id}/adopt`)
    );
};

export const deleteRoutine = async (id: number) => {
    return safeRequest<void>(
        axiosClient.delete(`/api/v1/routines/${id}`)
    );
};
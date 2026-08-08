import axiosClient, { safeRequest } from '../lib/axios/axiosClient';
import type { AssignmentRequest, AssignmentResponse } from '../types/api.types';

export const getAllAssignments = async () => {
    return safeRequest<AssignmentResponse[]>(
        axiosClient.get('/api/v1/assignments')
    );
};

export const getAssignmentsByTrainer = async (trainerId: number) => {
    return safeRequest<AssignmentResponse[]>(
        axiosClient.get(`/api/v1/assignments/trainer/${trainerId}`)
    );
};

export const getAssignmentsByUser = async (userId: number) => {
    return safeRequest<AssignmentResponse[]>(
        axiosClient.get(`/api/v1/assignments/user/${userId}`)
    );
};

export const createAssignment = async (request: AssignmentRequest) => {
    return safeRequest<AssignmentResponse>(
        axiosClient.post('/api/v1/assignments', request)
    );
};

export const deleteAssignment = async (userId: number, trainerId: number) => {
    return safeRequest<void>(
        axiosClient.delete(`/api/v1/assignments/${userId}/${trainerId}`)
    );
};
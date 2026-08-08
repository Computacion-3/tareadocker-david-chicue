import axiosClient, { safeRequest } from '../lib/axios/axiosClient';
import type { EnrollmentRequest, EnrollmentResponse } from '../types/api.types';

export const getAllEnrollments = async () => {
    return safeRequest<EnrollmentResponse[]>(
        axiosClient.get('/api/v1/enrollments')
    );
};

export const getEnrollmentsByUserId = async (userId: number) => {
    return safeRequest<EnrollmentResponse[]>(
        axiosClient.get(`/api/v1/enrollments/user/${userId}`)
    );
};

export const createEnrollment = async (request: EnrollmentRequest) => {
    return safeRequest<EnrollmentResponse>(
        axiosClient.post('/api/v1/enrollments', request)
    );
};

export const deleteEnrollment = async (userId: number, activityId: number) => {
    return safeRequest<void>(
        axiosClient.delete(`/api/v1/enrollments/${userId}/${activityId}`)
    );
};

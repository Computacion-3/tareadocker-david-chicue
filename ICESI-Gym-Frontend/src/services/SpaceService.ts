import axiosClient, { safeRequest } from '../lib/axios/axiosClient';
import type { SpaceRequest, SpaceResponse } from '../types/api.types';

export const getAllSpaces = async () => {
    return safeRequest<SpaceResponse[]>(
        axiosClient.get('/api/v1/spaces')
    );
};

export const getSpaceById = async (id: number) => {
    return safeRequest<SpaceResponse>(
        axiosClient.get(`/api/v1/spaces/${id}`)
    );
};

export const createSpace = async (request: SpaceRequest) => {
    return safeRequest<SpaceResponse>(
        axiosClient.post('/api/v1/spaces', request)
    );
};

export const updateSpace = async (id: number, request: SpaceRequest) => {
    return safeRequest<SpaceResponse>(
        axiosClient.put(`/api/v1/spaces/${id}`, request)
    );
};

export const deleteSpace = async (id: number) => {
    return safeRequest<void>(
        axiosClient.delete(`/api/v1/spaces/${id}`)
    );
};
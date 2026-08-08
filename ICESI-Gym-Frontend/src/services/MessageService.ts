import axiosClient, { safeRequest } from '../lib/axios/axiosClient';
import type { MessageRequest, MessageResponse } from '../types/api.types';

export const getAllMessages = async () => {
    return safeRequest<MessageResponse[]>(
        axiosClient.get('/api/v1/messages')
    );
};

export const getMessageById = async (id: number) => {
    return safeRequest<MessageResponse>(
        axiosClient.get(`/api/v1/messages/${id}`)
    );
};

export const sendMessage = async (request: MessageRequest) => {
    return safeRequest<MessageResponse>(
        axiosClient.post('/api/v1/messages', request)
    );
};

export const updateMessage = async (id: number, request: MessageRequest) => {
    return safeRequest<MessageResponse>(
        axiosClient.put(`/api/v1/messages/${id}`, request)
    );
};

export const deleteMessage = async (id: number) => {
    return safeRequest<void>(
        axiosClient.delete(`/api/v1/messages/${id}`)
    );
};
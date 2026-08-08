import axiosClient, { safeRequest } from '../lib/axios/axiosClient';
import type { NotificationRequest, NotificationResponse } from '../types/api.types';

export const getAllNotifications = async () => {
    return safeRequest<NotificationResponse[]>(
        axiosClient.get('/api/v1/notifications')
    );
};

export const getNotificationById = async (id: number) => {
    return safeRequest<NotificationResponse>(
        axiosClient.get(`/api/v1/notifications/${id}`)
    );
};

export const createNotification = async (request: NotificationRequest) => {
    return safeRequest<NotificationResponse>(
        axiosClient.post('/api/v1/notifications', request)
    );
};

export const updateNotification = async (id: number, request: NotificationRequest) => {
    return safeRequest<NotificationResponse>(
        axiosClient.put(`/api/v1/notifications/${id}`, request)
    );
};

export const deleteNotification = async (id: number) => {
    return safeRequest<void>(
        axiosClient.delete(`/api/v1/notifications/${id}`)
    );
};
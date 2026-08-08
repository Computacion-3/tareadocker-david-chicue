import axiosClient, { safeRequest } from '../lib/axios/axiosClient';
import type { ScheduleRequest, ScheduleResponse } from '../types/api.types';
import { getActivityById, type ActivityWithSpace } from './ActivityService';

export interface ScheduleWithActivity extends ScheduleResponse {
    activity?: ActivityWithSpace;
}

export const getAllSchedules = async () => {
    const result = await safeRequest<ScheduleResponse[]>(
        axiosClient.get('/api/v1/schedules')
    );

    if (result.error || !result.data) {
        return result;
    }

    const enrichedSchedules: ScheduleWithActivity[] = await Promise.all(
        result.data.map(async (schedule) => {
            if (schedule.activityId) {
                const activityResult = await getActivityById(schedule.activityId);
                return { ...schedule, activity: activityResult.data };
            }
            return schedule;
        })
    );

    return { ...result, data: enrichedSchedules };
};

export const getScheduleById = async (id: number) => {
    const result = await safeRequest<ScheduleResponse>(
        axiosClient.get(`/api/v1/schedules/${id}`)
    );

    if (result.error || !result.data) {
        return result;
    }

    const enrichedSchedule: ScheduleWithActivity = { ...result.data };
    if (result.data.activityId) {
        const activityResult = await getActivityById(result.data.activityId);
        enrichedSchedule.activity = activityResult.data;
    }

    return { ...result, data: enrichedSchedule };
};

export const createSchedule = async (request: ScheduleRequest) => {
    return safeRequest<ScheduleResponse>(
        axiosClient.post('/api/v1/schedules', request)
    );
};

export const updateSchedule = async (id: number, request: ScheduleRequest) => {
    return safeRequest<ScheduleResponse>(
        axiosClient.put(`/api/v1/schedules/${id}`, request)
    );
};

export const deleteSchedule = async (id: number) => {
    return safeRequest<void>(
        axiosClient.delete(`/api/v1/schedules/${id}`)
    );
};
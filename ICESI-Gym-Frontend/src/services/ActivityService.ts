import axiosClient, { safeRequest } from '../lib/axios/axiosClient';
import type { ActivityRequest, ActivityResponse, SpaceResponse } from '../types/api.types';
import { getSpaceById } from './SpaceService';

export interface ActivityWithSpace extends ActivityResponse {
    space?: SpaceResponse;
}

export const getAllActivities = async () => {
    const result = await safeRequest<ActivityResponse[]>(
        axiosClient.get('/api/v1/activities')
    );
    
    if (result.error || !result.data) {
        return result;
    }

    // Enrich with space data
    const enrichedActivities: ActivityWithSpace[] = await Promise.all(
        result.data.map(async (activity) => {
            if (activity.spaceId) {
                const spaceResult = await getSpaceById(activity.spaceId);
                return { ...activity, space: spaceResult.data };
            }
            return activity;
        })
    );

    return { ...result, data: enrichedActivities };
};

export const getActivityById = async (id: number) => {
    const result = await safeRequest<ActivityResponse>(
        axiosClient.get(`/api/v1/activities/${id}`)
    );

    if (result.error || !result.data) {
        return result;
    }

    const enrichedActivity: ActivityWithSpace = { ...result.data };
    if (result.data.spaceId) {
        const spaceResult = await getSpaceById(result.data.spaceId);
        enrichedActivity.space = spaceResult.data;
    }

    return { ...result, data: enrichedActivity };
};

export const createActivity = async (request: ActivityRequest) => {
    return safeRequest<ActivityResponse>(
        axiosClient.post('/api/v1/activities', request)
    );
};

export const updateActivity = async (id: number, request: ActivityRequest) => {
    return safeRequest<ActivityResponse>(
        axiosClient.put(`/api/v1/activities/${id}`, request)
    );
};

export const deleteActivity = async (id: number) => {
    return safeRequest<void>(
        axiosClient.delete(`/api/v1/activities/${id}`)
    );
};
import { createSlice } from '@reduxjs/toolkit';
import type { PayloadAction } from '@reduxjs/toolkit';
import type { NotificationResponse, MessageResponse } from '../../types/api.types';

interface RealTimeState {
  notifications: NotificationResponse[];
  messages: MessageResponse[];
}

const initialState: RealTimeState = {
  notifications: [],
  messages: [],
};

const realTimeSlice = createSlice({
  name: 'realTime',
  initialState,
  reducers: {
    setNotifications: (state, action: PayloadAction<NotificationResponse[]>) => {
      const unique = action.payload.filter((v, i, a) => a.findIndex(t => t.idNotification === v.idNotification) === i);
      state.notifications = unique;
    },
    setMessages: (state, action: PayloadAction<MessageResponse[]>) => {
      const unique = action.payload.filter((v, i, a) => a.findIndex(t => t.id === v.id) === i);
      state.messages = unique;
    },
    addNotification: (state, action: PayloadAction<NotificationResponse>) => {
      const exists = state.notifications.some(n => n.idNotification === action.payload.idNotification);
      if (!exists) {
        state.notifications.unshift(action.payload);
      }
    },
    addMessage: (state, action: PayloadAction<MessageResponse>) => {
      const exists = state.messages.some(m => m.id === action.payload.id);
      if (!exists) {
        state.messages.push(action.payload);
      }
    },
    clearNotifications: (state) => {
      state.notifications = [];
    },
    markNotificationAsRead: (state, action: PayloadAction<number>) => {
      const notification = state.notifications.find(n => n.idNotification === action.payload);
      if (notification) {
        notification.isRead = true;
      }
    }
  },
});

export const { setNotifications, setMessages, addNotification, addMessage, clearNotifications, markNotificationAsRead } = realTimeSlice.actions;
export default realTimeSlice.reducer;
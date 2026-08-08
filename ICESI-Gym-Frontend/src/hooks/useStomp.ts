import { useEffect, useRef } from "react";
import { useAppDispatch } from "./useDispatch";
import useAuth from "./useAuth";
import createStompClient from "../lib/stomp/stompClient";
import type { SubscriptionConfig } from "../lib/stomp/stompClient";
import { addNotification, addMessage } from "../store/slices/realTimeSlice";
import type { NotificationResponse, MessageResponse } from "../types/api.types";

export default function useStomp() {
  const { user, isAuthenticated } = useAuth();
  const dispatch = useAppDispatch();
  const clientRef = useRef<ReturnType<typeof createStompClient> | null>(null);

  useEffect(() => {
    if (!isAuthenticated || !user) {
      if (clientRef.current) {
        clientRef.current.deactivate();
        clientRef.current = null;
      }
      return;
    }

    // Using a common interface or union to avoid 'any' and resolve assignment errors
    type RealTimeData = NotificationResponse | MessageResponse;

    const subscriptions: SubscriptionConfig<RealTimeData>[] = [
      {
        topic: "/topic/notifications",
        callback: (data) => {
          dispatch(addNotification(data as NotificationResponse));
        },
      },
      {
        topic: "/user/queue/notifications",
        callback: (data) => {
          dispatch(addNotification(data as NotificationResponse));
        },
      },
      {
        topic: "/user/queue/messages",
        callback: (data) => {
          dispatch(addMessage(data as MessageResponse));
        },
      },
    ];

    const client = createStompClient(subscriptions);
    client.activate();
    clientRef.current = client;

    return () => {
      if (clientRef.current) {
        clientRef.current.deactivate();
        clientRef.current = null;
      }
    };
  }, [isAuthenticated, user, dispatch]);
}

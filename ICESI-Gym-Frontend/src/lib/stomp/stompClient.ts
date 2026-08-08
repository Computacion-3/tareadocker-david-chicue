import { Client } from "@stomp/stompjs";
import type { Message } from "@stomp/stompjs";

export interface SubscriptionConfig<T> {
  topic: string;
  callback: (data: T) => void;
}

export default function createStompClient<T>(subscriptions: SubscriptionConfig<T>[]) {
  const token = localStorage.getItem('token');

  const isSecure = window.location.protocol === 'https:';
  const protocol = isSecure ? 'wss://' : 'ws://';
  const defaultBrokerURL = protocol + window.location.host + "/ws";

  const client = new Client({
    brokerURL: import.meta.env.VITE_WS_URL || defaultBrokerURL,
    connectHeaders: {
      Authorization: token ? `Bearer ${token}` : "",
    },
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
  });

  client.onConnect = () => {
    subscriptions.forEach(({ topic, callback }) => {
      client.subscribe(topic, (message: Message) => {
        try {
          const data = JSON.parse(message.body) as T;
          callback(data);
        } catch {
          /* Parse errors are handled silently */
        }
      });
    });
  };

  return client;
}

import { messageCallbackType, StompHeaders, StompSubscription } from "@stomp/stompjs";
import { useContext, useEffect, useRef } from "react";
import StompSocketContext from "./StompSocketContext";

export function useStompSubscription(
  destination: string,
  onMessage: messageCallbackType,
  headers?: StompHeaders
) {
  const stompContext = useContext(StompSocketContext);
  const subscription = useRef<StompSubscription>();

  if (!stompContext) {
    throw new Error("No StompSocketContext found!");
  }

  useEffect(() => {
    if (!stompContext.connected) {
      return;
    }
    subscription.current = stompContext.client?.subscribe(destination, onMessage, headers);

    return () => {
      if (subscription.current) {
        try {
          subscription.current?.unsubscribe();
        } catch (e) {}
        subscription.current = undefined;
      }
    };
  }, [destination, stompContext.connected]);
}

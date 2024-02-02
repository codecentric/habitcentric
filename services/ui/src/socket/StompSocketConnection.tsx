import { Client, StompConfig } from "@stomp/stompjs";
import StompSocketContext from "./StompSocketContext";
import React, { useEffect, useState } from "react";

export interface StompSocketConnectionProps extends StompConfig {
  children: React.JSX.Element;
}

function StompSocketConnection(props: StompSocketConnectionProps) {
  const { children, ...stompConfig } = props;
  const [client, setClient] = useState<Client>();
  const [connected, setConnected] = useState(false);

  useEffect(() => {
    const newClient = new Client({
      ...stompConfig,
      onWebSocketError: (e) => {
        if (props.onWebSocketError) {
          props.onWebSocketError(e);
        }
        console.log("ws error:", e);
      },
      onConnect: (frame) => {
        if (props.onConnect) {
          props.onConnect(frame);
        }
        setConnected(true);
      },
      onDisconnect: (frame) => {
        if (props.onDisconnect) {
          props.onDisconnect(frame);
        }
        setConnected(false);
      },
      onWebSocketClose: (frame) => {
        if (props.onWebSocketClose) {
          props.onWebSocketClose(frame);
        }
        console.log("ws close:", frame);
        setConnected(false);
      },
      onStompError: (frame) => {
        if (props.onStompError) {
          props.onStompError(frame);
        }
        console.log("Broker reported error: ", frame);
      },
    });
    newClient.activate();
    setClient(newClient);

    return () => {
      setClient((c) => {
        c?.deactivate();
        return undefined;
      });
      console.log("socket cleanup!!yyyay adasdasdasdas");
    };
  }, [...Object.values(stompConfig)]);

  return (
    <StompSocketContext.Provider
      value={{
        client,
        connected,
      }}
    >
      {children}
    </StompSocketContext.Provider>
  );
}

export default StompSocketConnection;

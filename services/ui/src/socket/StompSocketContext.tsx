import { createContext } from "react";
import { Client } from "@stomp/stompjs";

export interface StompSocketProviderContext {
  connected: boolean;
  client: Client | undefined;
}

const StompSocketContext = createContext<StompSocketProviderContext | undefined>(undefined);

export default StompSocketContext;

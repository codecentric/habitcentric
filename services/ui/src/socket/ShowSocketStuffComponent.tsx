import { useContext, useState } from "react";
import { useStompSubscription } from "./useStompSubscription";
import StompSocketContext from "./StompSocketContext";

function ShowSocketStuffComponent() {
  const [output, setOutput] = useState("");
  const socketContext = useContext(StompSocketContext);

  useStompSubscription("/user/queue/streak-updates", (frame) => {
    setOutput((prev) => prev + "\n" + frame.body);
  });

  return (
    <div>
      Socket State: {socketContext?.connected ? "connected" : "NOT connected"}
      <h1>Output</h1>
      <pre>{output}</pre>
    </div>
  );
}

export default ShowSocketStuffComponent;

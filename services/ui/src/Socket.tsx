import { useEffect, useState } from "react";
import { ActivationState, Client } from "@stomp/stompjs";

function Socket() {
  const [wsOutput, setWsOutput] = useState("");

  useEffect(() => {
    // let accessToken = auth.user?.access_token || "";
    // console.log(auth);
    // console.log("Token", accessToken);

    const client = new Client({
      brokerURL: "ws://localhost:9005/streak-socket",
      connectHeaders: {
        User: "asd",
      },
      onWebSocketError: (e) => {
        console.log("ws error:", e);
      },
      onWebSocketClose: (e) => {
        console.log("ws close:", e);
      },
      onDisconnect: (frame) => {
        console.log("disconnect", frame);
      },
      debug: (str) => {
        // console.log("debug:", str);
      },
      onConnect: () => {
        client.subscribe("/user/queue/streak-updates", (message) => {
          console.log(`Received streak update: ${message.body} ${message.headers}`);
        });

        client.subscribe("/app/ticker", (message) =>
          console.log(`Received app ticker: ${message.body}`)
        );
        client.subscribe("/topic/ticker", (message) =>
          console.log(`Received ticker: ${message.body}`)
        );
        client.subscribe("/topic/hello", (message) =>
          console.log(`Received hello: ${message.body}`)
        );

        client.publish({
          destination: "/app/helloApp",
          body: "Hi from stomp!",
        });

        // client.publish({ destination: "/topic/test01", body: "First Message" });
      },
      onStompError: (frame) => {
        console.log("Broker reported error: " + frame.headers["message"]);
        console.log("Additional details: " + frame.body);
      },
      onChangeState: (s) => {
        ActivationState;
        console.log("connection state: ", s);
      },
    });
    client.activate();
    setWsOutput("hi!");

    // client.subscribe("/topic", (message) => {
    //   setWsOutput(message.body);
    // });
    //
    return () => {
      console.trace();
      console.log("destroy?");
      client.deactivate();
    };
  }, []);

  return <pre>{wsOutput}</pre>;
}

export default Socket;

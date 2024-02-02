import StompSocketConnection from "./StompSocketConnection";
import ShowSocketStuffComponent from "./ShowSocketStuffComponent";

function SocketReactPage() {
  return (
    <StompSocketConnection
      brokerURL="ws://localhost:9005/streak-socket"
      connectHeaders={{
        User: "asd",
      }}
    >
      <ShowSocketStuffComponent />
    </StompSocketConnection>
  );
}

export default SocketReactPage;

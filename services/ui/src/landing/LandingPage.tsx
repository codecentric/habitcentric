import { AuthContextProps, useAuth } from "react-oidc-context";
import { useState } from "react";
import PrimaryButton from "../overview/components/PrimaryButton";

function LandingPage() {
  const [redirectError, setRedirectError] = useState(false);
  const auth = useAuth();
  const error = redirectError || auth.error;

  return (
    <div className="mx-auto max-w-md px-4">
      <div className="pt-20 text-center">
        <h1 className="mb-4 text-5xl font-semibold leading-tight tracking-tight text-gray-800">
          Track your habits.
          <br />
          <span className="bg-gradient-to-r from-cc-primary-500 to-centric-mint-500 bg-clip-text text-transparent">
            Feel good.
          </span>
        </h1>
        <div className="max-w-md pb-8">
          <p className="pb-8 text-lg text-gray-600">
            Even a journey of a thousand miles begins with a single step. Track
            your habits now and start your adventure.
          </p>
          <div className="mx-auto max-w-xs">
            <LoginButton {...auth} onError={() => setRedirectError(true)} />
            {error && <div>Oops... Something went wrong!</div>}
          </div>
        </div>
      </div>
    </div>
  );
}

type LoginButtonProps = AuthContextProps & { onError: () => void };

function LoginButton({
  activeNavigator,
  signinRedirect,
  onError,
}: LoginButtonProps) {
  const waitingForRedirect = activeNavigator === "signinRedirect";

  return (
    <PrimaryButton
      disabled={waitingForRedirect}
      onClick={() => void signinRedirect().catch((_) => onError())}
    >
      Log in
    </PrimaryButton>
  );
}

export default LandingPage;

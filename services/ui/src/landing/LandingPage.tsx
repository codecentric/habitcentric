import { AuthContextProps, useAuth } from "react-oidc-context";
import { useState } from "react";
import PrimaryButton from "../overview/components/PrimaryButton";
import { oidcIsEnabled } from "../auth/config/oidc";

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
          <span className="from-cc-primary-500 to-centric-mint-500 bg-gradient-to-r bg-clip-text text-transparent">
            Feel good.
          </span>
        </h1>
        <div className="max-w-md pb-8">
          <p className="pb-8 text-lg text-gray-600">
            Even a journey of a thousand miles begins with a single step. Track
            your habits now and start your adventure.
          </p>
          <div className="mx-auto max-w-xs">
            {oidcIsEnabled() ? (
              <OidcLoginButton
                {...auth}
                onError={() => setRedirectError(true)}
              />
            ) : (
              <BackendLoginLink />
            )}
            {error && <div>Oops... Something went wrong!</div>}
          </div>
        </div>
      </div>
    </div>
  );
}

type LoginButtonProps = AuthContextProps & { onError: () => void };

function OidcLoginButton({
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

function BackendLoginLink() {
  return (
    <a
      className={`bg-cc-primary-500 hover:bg-cc-primary-600 active:bg-cc-primary-700 w-full rounded-lg px-4 py-2 text-sm font-semibold tracking-wider text-gray-900 shadow-sm sm:text-base`}
      href={`${process.env.PUBLIC_URL}/overview`}
    >
      Log in
    </a>
  );
}

export default LandingPage;

import { useEffect } from "react";
import { useSearchParams } from "react-router-dom";

export default function useClearAuthQueryParams(
  authenticationCompleted: boolean
) {
  const [searchParams, setSearchParams] = useSearchParams();

  useEffect(() => {
    if (
      authenticationCompleted &&
      searchParams.has("state") &&
      searchParams.has("code")
    ) {
      setSearchParams({});
    }
  }, [authenticationCompleted, searchParams, setSearchParams]);
}

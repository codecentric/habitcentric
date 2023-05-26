import useSWR, { Fetcher } from "swr";
import { useAuth } from "react-oidc-context";
import { fetchWithToken } from "../../../auth/fetchWithToken";

export type Achievement = {
  week: number;
  month: number;
};

const achievementFetcher: Fetcher<Achievement, [string, string]> = ([url, token]) =>
  fetchWithToken(url, {}, token).then((res) => res.json());

export function useAchievement() {
  const auth = useAuth();
  const { data, error } = useSWR(
    ["/report/achievement", auth.user?.access_token],
    achievementFetcher
  );

  return {
    achievement: data,
    error: error,
  };
}

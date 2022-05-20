import useSWR, { Fetcher } from "swr";
import { Habit } from "./habit";
import { useAuth } from "react-oidc-context";
import { fetchWithToken } from "../../../auth/fetchWithToken";

const habitFetcher: Fetcher<Habit[], [string, string]> = (url, token) =>
  fetchWithToken(url, {}, token).then((res) => res.json());

export function useHabits() {
  const auth = useAuth();
  const { data, error } = useSWR(
    ["/habits", auth.user?.access_token],
    habitFetcher
  );

  return {
    habits: data,
    error: error,
  };
}

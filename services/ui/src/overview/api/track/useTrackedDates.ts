import useSWR, { Fetcher } from "swr";
import { parseISO } from "date-fns";
import { useAuth } from "react-oidc-context";
import { fetchWithToken } from "../../../auth/fetchWithToken";

const trackedDatesFetcher: Fetcher<string[], [string, string, string]> = ([url, habitId, token]) =>
  fetchWithToken(`${url}/${habitId}`, {}, token).then((res) => res.json());

export function useTrackedDatesOfHabit(habitId: number) {
  const auth = useAuth();
  const { data, error } = useSWR(
    ["/track/habits", habitId, auth.user?.access_token],
    trackedDatesFetcher
  );

  const trackedDates = data?.map((dateString) => parseISO(dateString));
  return {
    trackedDates: trackedDates,
    error: error,
  };
}

import useSWR, { Fetcher } from "swr";
import { TrackedDates } from "./model";
import { parseISO } from "date-fns";

const trackedDatesFetcher: Fetcher<string[], [string, string]> = (
  url,
  habitId
) => fetch(`${url}/${habitId}`).then((res) => res.json());

export function useTrackedDatesOfHabit(habitId: number) {
  const { data, error } = useSWR(
    ["/track/habits", habitId],
    trackedDatesFetcher
  );

  const trackedDates = data?.map((dateString) => parseISO(dateString));
  return {
    trackedDates: trackedDates,
    error: error,
  };
}

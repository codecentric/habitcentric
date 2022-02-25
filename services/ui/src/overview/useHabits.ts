import useSWR, { Fetcher } from "swr";
import { Habit } from "./habit";

const habitFetcher: Fetcher<Habit[], string> = (url) =>
  fetch(url).then((res) => res.json());

export function useHabits() {
  const { data, error } = useSWR("/habits", habitFetcher);

  return {
    habits: data,
    error: error,
  };
}

import useSWR, { Fetcher } from "swr";

export type Achievement = {
  week: number;
  month: number;
};

const achievementFetcher: Fetcher<Achievement, string> = (url) =>
  fetch(url).then((res) => res.json());

export function useAchievement() {
  const { data, error } = useSWR("/report/achievement", achievementFetcher);

  return {
    achievement: data,
    error: error,
  };
}

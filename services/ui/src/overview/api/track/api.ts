import { formatISO } from "date-fns";
import { ScopedMutator } from "swr/dist/types";

export async function putTrackedDates(
  habitId: number,
  trackedDates: Date[],
  mutate: ScopedMutator
) {
  const formattedTrackedDates = trackedDates.map((date) =>
    formatISO(date, { representation: "date" })
  );
  await fetch(`/track/habits/${habitId}`, {
    method: "PUT",
    body: JSON.stringify(formattedTrackedDates),
  });
  await mutate(["/track/habits", habitId]);
}

import { formatISO } from "date-fns";
import { getUser } from "../../../auth/getUser";
import { fetchWithToken } from "../../../auth/fetchWithToken";
import { ScopedMutator } from "swr/_internal";

export async function putTrackedDates(
  habitId: string,
  trackedDates: Date[],
  mutate: ScopedMutator,
) {
  const user = getUser();
  const formattedTrackedDates = trackedDates.map((date) =>
    formatISO(date, { representation: "date" }),
  );
  await fetchWithToken(
    `/track/habits/${habitId}`,
    {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formattedTrackedDates),
    },
    user?.access_token,
  );
  await mutate(["/track/habits", habitId, user?.access_token]);
  await mutate(["/report/achievement", user?.access_token]);
}

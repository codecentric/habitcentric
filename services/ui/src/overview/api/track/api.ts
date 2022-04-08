import { formatISO } from "date-fns";
import { ScopedMutator } from "swr/dist/types";
import { getUser } from "../../../auth/getUser";
import { fetchWithToken } from "../../../auth/fetchWithToken";

export async function putTrackedDates(
  habitId: number,
  trackedDates: Date[],
  mutate: ScopedMutator
) {
  const user = getUser();
  const formattedTrackedDates = trackedDates.map((date) =>
    formatISO(date, { representation: "date" })
  );
  await fetchWithToken(
    `/track/habits/${habitId}`,
    {
      method: "PUT",
      body: JSON.stringify(formattedTrackedDates),
    },
    user?.access_token
  );
  await mutate(["/track/habits", habitId, user?.access_token]);
}

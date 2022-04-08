import { ScopedMutator } from "swr/dist/types";
import { getUser } from "../../../auth/getUser";
import { fetchWithToken } from "../../../auth/fetchWithToken";

export async function createHabit(
  name: string,
  repetitions: number,
  frequency: string,
  mutate: ScopedMutator
) {
  const user = getUser();
  await fetchWithToken(
    "/habits",
    {
      method: "POST",
      body: JSON.stringify({
        name,
        schedule: {
          repetitions,
          frequency,
        },
      }),
    },
    user?.access_token
  );
  await mutate(["/habits", user?.access_token]);
  await mutate(["/report/achievement", user?.access_token]);
}

export async function deleteHabit(id: number, mutate: ScopedMutator) {
  const user = getUser();
  await fetchWithToken(
    `/habits/${id}`,
    {
      method: "DELETE",
    },
    user?.access_token
  );
  await mutate(["/habits", user?.access_token]);
  await mutate(["/report/achievement", user?.access_token]);
}

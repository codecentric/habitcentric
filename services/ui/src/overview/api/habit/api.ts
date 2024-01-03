import { getUser } from "../../../auth/getUser";
import { fetchWithToken } from "../../../auth/fetchWithToken";
import { Frequency, Habit } from "./habit";
import { ScopedMutator } from "swr/_internal";

export type CreateHabitRequest = Omit<Habit, "id">;

export async function createHabit(
  name: string,
  repetitions: number,
  frequency: Frequency,
  mutate: ScopedMutator,
) {
  const user = getUser();
  const request: CreateHabitRequest = {
    name,
    schedule: {
      repetitions,
      frequency,
    },
  };
  await fetchWithToken(
    "/habits",
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(request),
    },
    user?.access_token,
  );
  await mutate(["/habits", user?.access_token]);
  await mutate(["/report/achievement", user?.access_token]);
}

export async function deleteHabit(id: string, mutate: ScopedMutator) {
  const user = getUser();
  await fetchWithToken(
    `/habits/${id}`,
    {
      method: "DELETE",
    },
    user?.access_token,
  );
  await mutate(["/habits", user?.access_token]);
  await mutate(["/report/achievement", user?.access_token]);
}

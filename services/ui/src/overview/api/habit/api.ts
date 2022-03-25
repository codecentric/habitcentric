import { ScopedMutator } from "swr/dist/types";

export async function createHabit(
  name: string,
  repetitions: number,
  frequency: string,
  mutate: ScopedMutator
) {
  await fetch("/habits", {
    method: "POST",
    body: JSON.stringify({
      name,
      schedule: {
        repetitions,
        frequency,
      },
    }),
  });
  await mutate("/habits");
  await mutate("/report/achievement");
}

export async function deleteHabit(id: number, mutate: ScopedMutator) {
  await fetch(`/habits/${id}`, { method: "DELETE" });
  await mutate("/habits");
  await mutate("/report/achievement");
}

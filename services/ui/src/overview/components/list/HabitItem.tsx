import React from "react";
import { Schedule, scheduleToString } from "../../api/habit/habit";
import { TrashIcon } from "@heroicons/react/solid";
import { useSWRConfig } from "swr";
import { deleteHabit } from "../../api/habit/api";

export type HabitItemProps = {
  id: number;
  name: string;
  schedule: Schedule;
};

export function HabitItem({ id, name, schedule }: HabitItemProps) {
  return (
    <li className="flex items-center justify-between py-2 pl-2">
      <div>
        <h3 className="text-gray-800 sm:text-lg">{name}</h3>
        <p className="text-sm text-gray-400">{scheduleToString(schedule)}</p>
      </div>
      <div className="flex items-center gap-2 pr-4">
        <TrackButton />
        <DeleteButton id={id} name={name} />
      </div>
    </li>
  );
}

function TrackButton() {
  return (
    <button className="h-8 rounded-md bg-cc-primary-500 px-2 text-xs font-semibold tracking-wider text-gray-900 hover:bg-cc-primary-600 active:bg-cc-primary-700 sm:text-sm">
      Track
    </button>
  );
}

function DeleteButton({ id, name }: { id: number; name: string }) {
  const { mutate } = useSWRConfig();

  return (
    <button
      className="h-8 rounded-md bg-cc-secondary-500 px-2 text-xs font-semibold tracking-wider text-white hover:bg-cc-secondary-600 active:bg-cc-secondary-700 sm:text-sm"
      aria-label={`Delete ${name} habit`}
      onClick={() => deleteHabit(id, mutate)}
    >
      <TrashIcon className="w-5" aria-hidden="true" />
    </button>
  );
}

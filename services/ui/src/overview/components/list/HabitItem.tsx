import React from "react";
import { Schedule, scheduleToString } from "../../api/habit/habit";
import { TrashIcon } from "@heroicons/react/solid";
import { useSWRConfig } from "swr";
import { deleteHabit } from "../../api/habit/api";
import CardPopover from "../CardPopover";
import TrackDatePicker from "./TrackDatePicker";

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
        <TrackPopover habitId={id} name={name} />
        <DeleteButton id={id} name={name} />
      </div>
    </li>
  );
}

type TrackPopoverProps = { habitId: number; name: string };

function TrackPopover({ habitId, name }: TrackPopoverProps) {
  return (
    <CardPopover title="Track" ariaLabel={`Track ${name} habit`}>
      <TrackDatePicker habitId={habitId} />
    </CardPopover>
  );
}

type DeleteButtonProps = { id: number; name: string };

function DeleteButton({ id, name }: DeleteButtonProps) {
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

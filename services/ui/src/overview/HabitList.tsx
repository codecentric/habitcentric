import { SearchIcon } from "@heroicons/react/solid";
import React from "react";
import { Habit } from "./habit";
import { HabitItem } from "./HabitItem";

export type HabitListProps = {
  habits: Array<Habit>;
};

function HabitList({ habits }: HabitListProps) {
  return (
    <div className="mt-4">
      <SearchInput />
      <ul className="mt-4 flex flex-col divide-y">
        {habits.map((habit) => (
          <HabitItem {...habit} key={habit.id} />
        ))}
      </ul>
    </div>
  );
}

function SearchInput() {
  return (
    <div className="relative text-gray-500">
      <SearchIcon className="absolute top-2 left-2 flex w-8 items-center pl-2" />
      <input
        id="habit-search"
        className="w-full rounded-md border-0 bg-gray-100 pl-12 text-sm shadow-sm focus:bg-white focus:text-gray-800 focus:outline-none sm:text-base"
        type="search"
        placeholder="Search..."
      />
    </div>
  );
}

export default HabitList;

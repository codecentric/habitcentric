import { SearchIcon } from "@heroicons/react/solid";
import React, { useState } from "react";
import { Habit, scheduleToString } from "./habit";
import { HabitItem } from "./HabitItem";
import { useHabits } from "./useHabits";

function HabitList() {
  const [query, setQuery] = useState("");
  const { habits, error } = useHabits();

  if (error) return <div>Failed to load</div>;
  if (!habits) return <div>Loading...</div>;

  const filteredHabits = habits.filter(
    (habit) =>
      nameStartsWithQuery(habit, query) || scheduleStartsWithQuery(habit, query)
  );

  return (
    <div className="mt-4">
      <SearchInput setQuery={setQuery} />
      <ul className="mt-4 flex flex-col divide-y" aria-label="Habits">
        {filteredHabits.map((habit) => (
          <HabitItem {...habit} key={habit.id} />
        ))}
      </ul>
    </div>
  );
}

function nameStartsWithQuery(habit: Habit, query: string) {
  return habit.name.toLowerCase().includes(query.toLowerCase());
}

function scheduleStartsWithQuery(habit: Habit, query: string) {
  return scheduleToString(habit.schedule)
    .toLowerCase()
    .includes(query.toLowerCase());
}

type SearchInputProps = {
  setQuery: (query: string) => void;
};

function SearchInput({ setQuery }: SearchInputProps) {
  return (
    <div className="relative text-gray-500">
      <SearchIcon className="absolute top-2 left-2 flex w-8 items-center pl-2" />
      <input
        id="habit-search"
        className="w-full rounded-md border-0 bg-gray-100 pl-12 text-sm shadow-sm focus:bg-white focus:text-gray-800 focus:outline-none sm:text-base"
        type="search"
        placeholder="Search..."
        onChange={(event) => setQuery(event.target.value)}
      />
    </div>
  );
}

export default HabitList;

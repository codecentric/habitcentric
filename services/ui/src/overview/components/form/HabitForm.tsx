import React from "react";

export function HabitForm() {
  return (
    <>
      <form className="mb-6 space-y-6">
        <div>
          <label className="text-sm font-medium sm:text-base" htmlFor="name">
            Name
          </label>
          <div className="mt-1">
            <input
              className="w-full rounded-md border-0 bg-gray-100 text-sm shadow-sm sm:text-base"
              id="name"
              type="text"
              placeholder="Jogging"
            />
          </div>
        </div>
        <div>
          <label
            className="text-sm font-medium sm:text-base"
            htmlFor="repetitions"
          >
            Repetitions
          </label>
          <div className="mt-1">
            <input
              className="w-full rounded-md border-0 bg-gray-100 text-sm shadow-sm sm:text-base"
              id="repetitions"
              type="number"
              placeholder="2"
            />
          </div>
        </div>
        <div>
          <label
            className="text-sm font-medium sm:text-base"
            htmlFor="frequency"
          >
            Frequency
          </label>
          <div className="mt-1">
            <select
              className="w-full rounded-md border-0 bg-gray-100 text-sm shadow-sm sm:text-base"
              id="frequency"
            >
              <option>daily</option>
              <option>weekly</option>
              <option>monthly</option>
            </select>
          </div>
        </div>
      </form>
      <button className="w-full rounded-lg bg-cc-primary-500 px-4 py-2 text-sm font-semibold tracking-wider text-gray-900 shadow-sm hover:bg-cc-primary-600 active:bg-cc-primary-700 sm:text-base">
        Create Habit
      </button>
    </>
  );
}

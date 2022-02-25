import React from "react";
import Progress from "./Progress";
import Card from "./Card";
import { Habit } from "./habit";
import HabitList from "./HabitList";

const habits: Array<Habit> = [
  {
    id: 1,
    name: "Jogging",
    schedule: {
      frequency: "WEEKLY",
      repetitions: 2,
    },
  },
  {
    id: 2,
    name: "Programming",
    schedule: {
      frequency: "WEEKLY",
      repetitions: 1,
    },
  },
  {
    id: 3,
    name: "Working out",
    schedule: {
      frequency: "MONTHLY",
      repetitions: 6,
    },
  },
];

function OverviewPage() {
  return (
    <div className="mb-10 flex justify-center pt-10 md:mb-20">
      <div className="grid w-full max-w-md grid-cols-1 justify-items-center gap-x-12 gap-y-10 sm:max-w-lg lg:max-w-xl xl:max-w-7xl xl:grid-cols-2">
        <Card title="Completion Rate">
          <div className="mt-5 space-y-5">
            <Progress title="7 days" percentage={23} />
            <Progress title="30 days" percentage={89} />
          </div>
        </Card>

        <Card className="row-span-2" title="Your Habits">
          <HabitList habits={habits} />
        </Card>

        <Card>
          <form className="mb-6 space-y-6">
            <div>
              <label
                className="text-sm font-medium sm:text-base"
                htmlFor="name"
              >
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
        </Card>
      </div>
    </div>
  );
}

export default OverviewPage;

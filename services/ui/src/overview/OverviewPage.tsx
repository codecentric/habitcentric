import React from "react";
import Card from "./components/Card";
import HabitList from "./components/list/HabitList";
import Report from "./components/report/Report";
import { HabitForm } from "./components/form/HabitForm";

function OverviewPage() {
  return (
    <div className="mb-10 flex justify-center pt-10 md:mb-20">
      <div className="grid w-full max-w-md grid-cols-1 justify-items-center gap-x-12 gap-y-10 sm:max-w-lg lg:max-w-xl xl:max-w-7xl xl:grid-cols-2">
        <Card title="Completion Rate">
          <Report />
        </Card>

        <Card className="row-span-2" title="Your Habits">
          <HabitList />
        </Card>

        <Card>
          <HabitForm />
        </Card>
      </div>
    </div>
  );
}

export default OverviewPage;

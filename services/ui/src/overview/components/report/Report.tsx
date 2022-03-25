import Progress from "./Progress";
import React from "react";
import { useAchievement } from "../../api/report/achievement";

function Report() {
  const { achievement, error } = useAchievement();

  if (!achievement) {
    return <div>Loading...</div>;
  }

  return (
    <div className="mt-5 space-y-5">
      <Progress title="7 days" percentage={achievement.week} />
      <Progress title="30 days" percentage={achievement.month} />
    </div>
  );
}

export default Report;

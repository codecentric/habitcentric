import Progress from "./Progress";
import React from "react";

function Report() {
  return (
    <div className="mt-5 space-y-5">
      <Progress title="7 days" percentage={23} />
      <Progress title="30 days" percentage={89} />
    </div>
  );
}

export default Report;

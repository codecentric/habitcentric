import React from "react";

export type ProgressProps = {
  title: string;
  percentage: number;
};

function Progress({ title, percentage }: ProgressProps) {
  return (
    <div className="space-y-2">
      <h3 className="text-sm uppercase text-gray-400">{title}</h3>
      <p className="text-2xl text-gray-800 sm:text-3xl">{percentage}%</p>
      <div className="h-1 w-full bg-gray-200">
        <div
          className="h-1 bg-cc-primary-500"
          role="progressbar"
          aria-valuemin={0}
          aria-valuenow={percentage}
          aria-valuemax={100}
          style={{ width: `${percentage}%` }}
        />
      </div>
    </div>
  );
}

Progress.defaultProps = {
  percentage: 0,
};

export default Progress;

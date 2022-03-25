import React from "react";
import DatePicker from "react-datepicker";
import "./TrackDatePicker.css";
import { ChevronLeftIcon, ChevronRightIcon } from "@heroicons/react/solid";
import { format } from "date-fns";
import { useTrackedDatesOfHabit } from "../api/track/useTrackedDates";
import { putTrackedDates } from "../api/track/api";
import { useSWRConfig } from "swr";

export type TrackDatePickerProps = {
  habitId: number;
};

function TrackDatePicker({ habitId }: TrackDatePickerProps) {
  const { trackedDates, error } = useTrackedDatesOfHabit(habitId);
  const { mutate } = useSWRConfig();

  if (error) return <div>Failed to load</div>;
  if (!trackedDates) return <div>Loading...</div>;

  async function updateTrackedDates(selectedDate: Date) {
    const updatedTrackedDates =
      trackedDates!.findIndex(
        (trackedDate) => trackedDate.valueOf() === selectedDate.valueOf()
      ) >= 0
        ? trackedDates!.filter(
            (trackedDate) => trackedDate.valueOf() !== selectedDate.valueOf()
          )
        : [...trackedDates!, selectedDate];
    await putTrackedDates(habitId, updatedTrackedDates, mutate);
  }

  return (
    <DatePicker
      inline
      onChange={updateTrackedDates}
      highlightDates={trackedDates}
      renderCustomHeader={({ date, decreaseMonth, increaseMonth }) => (
        <DatePickerHeader
          date={date}
          increaseOnClick={decreaseMonth}
          decreaseOnClick={increaseMonth}
        />
      )}
    />
  );
}

function DatePickerHeader(props: {
  date: Date;
  increaseOnClick: () => void;
  decreaseOnClick: () => void;
}) {
  return (
    <div className="flex items-center justify-between px-2 py-2">
      <span className="text-md font-medium text-gray-700 sm:text-lg xl:text-xl">
        {format(props.date, "MMMM yyyy")}
      </span>
      <div className="space-x-2">
        <button
          onClick={props.increaseOnClick}
          type="button"
          className="inline-flex rounded border-gray-300 bg-gray-200 p-1 text-sm font-medium text-gray-800 shadow-sm hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-cc-primary-500 focus:ring-offset-0"
        >
          <ChevronLeftIcon className="h-4 w-4 text-gray-600 sm:h-5 sm:w-5" />
        </button>

        <button
          onClick={props.decreaseOnClick}
          type="button"
          className="inline-flex rounded border-gray-300 bg-gray-200 p-1 text-sm font-medium text-gray-800 shadow-sm hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-cc-primary-500 focus:ring-offset-0"
        >
          <ChevronRightIcon className="h-4 w-4 text-gray-600 sm:h-5 sm:w-5" />
        </button>
      </div>
    </div>
  );
}

export default TrackDatePicker;

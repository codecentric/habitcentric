import React, { FormEvent, useState } from "react";
import { useSWRConfig } from "swr";
import { createHabit } from "../../api/habit/api";
import { Frequency } from "../../api/habit/habit";
import PrimaryButton from "../PrimaryButton";

type TextFieldProps = {
  label: string;
  placeholder: string;
  value: string | number | undefined;
  type: string;
  onChange: (value: string) => void;
};

function TextField({
  label,
  placeholder,
  value,
  type,
  onChange,
}: TextFieldProps) {
  const id = label.toLowerCase().replace(/\s+/, "-");
  return (
    <div>
      <label className="text-sm font-medium sm:text-base" htmlFor={id}>
        {label}
      </label>
      <div className="mt-1">
        <input
          className="w-full rounded-md border-0 bg-gray-100 text-sm shadow-sm sm:text-base"
          id={id}
          type={type}
          placeholder={placeholder}
          value={value}
          onChange={(e) => onChange(e.target.value)}
        />
      </div>
    </div>
  );
}

TextField.defaultProps = {
  type: "text",
};

export function HabitForm() {
  const [name, setName] = useState("");
  const [repetitions, setRepetitions] = useState("1");
  const [frequency, setFrequency] = useState<Frequency>("DAILY");

  const { mutate } = useSWRConfig();

  async function submit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    await createHabit(name, parseInt(repetitions), frequency, mutate);
  }

  return (
    <>
      <form onSubmit={submit} className="mb-6 space-y-6">
        <TextField
          label="Name"
          placeholder="Jogging"
          value={name}
          onChange={setName}
        />
        <TextField
          label="Repetitions"
          placeholder="2"
          value={repetitions}
          type="number"
          onChange={setRepetitions}
        />

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
              value={frequency}
              onChange={(e) => setFrequency(e.target.value as Frequency)}
            >
              <option value="DAILY">daily</option>
              <option value="WEEKLY">weekly</option>
              <option value="MONTHLY">monthly</option>
              <option value="YEARLY">yearly</option>
            </select>
          </div>
        </div>
        <PrimaryButton type="submit">Create Habit</PrimaryButton>
      </form>
    </>
  );
}

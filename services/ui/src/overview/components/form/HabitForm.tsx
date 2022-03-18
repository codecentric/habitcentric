import React, { FormEvent, useState } from "react";
import { ScopedMutator } from "swr/dist/types";
import { useSWRConfig } from "swr";

type TextFieldProps = {
  label: string;
  placeholder: string;
  value: string | number | undefined;
  type: string;
  onChange: (value: string) => void
}

function TextField({ label, placeholder, value, type, onChange }: TextFieldProps) {
  const id = label.toLowerCase().replace(/\s+/, "-");
  return <div>
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
  </div>;
}

TextField.defaultProps = {
  type: "text"
};

async function createHabit(name: string, repetitions: string, frequency: string, mutate: ScopedMutator) {
  await fetch("/habits", {
    method: "POST",
    body: JSON.stringify({
      name,
      schedule: {
        repetitions,
        frequency,
      }
    })
  });
  await mutate("/habits");
}

export function HabitForm() {
  const [name, setName] = useState("");
  const [repetitions, setRepetitions] = useState("");
  const [frequency, setFrequency] = useState("daily");

  const { mutate } = useSWRConfig();

  async function submit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    await createHabit(name, repetitions, frequency, mutate);
  }

  return (
      <>
        <form onSubmit={submit} className="mb-6 space-y-6">
          <TextField label="Name" placeholder="Jogging" value={name} onChange={setName}/>
          <TextField label="Repetitions" placeholder="2" value={repetitions} type="number"
                     onChange={setRepetitions}/>

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
                  onChange={(e) => setFrequency(e.target.value)}
              >
                <option value="DAILY">daily</option>
                <option value="WEEKLY">weekly</option>
                <option value="MONTHLY">monthly</option>
                <option value="YEARLY">yearly</option>
              </select>
            </div>
          </div>
          <button type="submit"
                  className="w-full rounded-lg bg-cc-primary-500 px-4 py-2 text-sm font-semibold tracking-wider text-gray-900 shadow-sm hover:bg-cc-primary-600 active:bg-cc-primary-700 sm:text-base">
            Create Habit
          </button>
        </form>
      </>
  );
}

import React from "react";
import Header from "./Header";

function App() {
  return (
    <div className="">
      <Header />

      <div className="mb-10 flex justify-center">
        <div className="grid w-full grid-cols-1 justify-items-center xl:max-w-7xl xl:grid-cols-2">
          <div className="mt-10 w-full max-w-md rounded-xl bg-white px-8 py-8 shadow-lg sm:max-w-xl">
            <h1 className="text-xl font-semibold text-gray-800 sm:text-2xl">
              Completion Rate
            </h1>
            <div className="mt-5 space-y-5">
              <div className="space-y-2">
                <h3 className="text-sm uppercase text-gray-400">7 days</h3>
                <p className="text-2xl text-gray-800 sm:text-3xl">23%</p>
                <div className="h-1 w-full bg-gray-200">
                  <div
                    className="h-1 bg-cc-primary-500"
                    style={{ width: "23%" }}
                  />
                </div>
              </div>
              <div className="space-y-2">
                <h3 className="text-sm uppercase text-gray-400">30 days</h3>
                <p className="text-2xl text-gray-800 sm:text-3xl">89%</p>
                <div className="h-1 w-full bg-gray-200">
                  <div
                    className="h-1 bg-cc-primary-500"
                    style={{ width: "89%" }}
                  />
                </div>
              </div>
            </div>
          </div>

          <div className="mt-10 w-full max-w-md rounded-xl bg-white px-8 py-8 shadow-lg sm:max-w-xl xl:row-span-2">
            <h1 className="text-xl font-semibold text-gray-800 sm:text-2xl">
              Your Habits
            </h1>
            <div className="relative mt-4 text-gray-500">
              <span className="absolute inset-y-0 left-2 flex items-center pl-2">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  className="h-5 w-5"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
                  />
                </svg>
              </span>
              <input
                id="habit-search"
                className="w-full rounded-md border-0 bg-gray-100 pl-12 text-sm shadow-sm focus:bg-white focus:text-gray-800 focus:outline-none sm:text-base"
                type="search"
                placeholder="Search..."
              />
            </div>
            <div className="mt-4 flex flex-col divide-y">
              <div className="flex items-center justify-between py-2">
                <div>
                  <h3 className="text-gray-800 sm:text-lg">Jogging</h3>
                  <p className="text-sm text-gray-400">twice per week</p>
                </div>
                <div className="mr-5 flex items-center gap-2">
                  <button className="h-8 rounded-md bg-cc-primary-500 px-2 text-xs font-semibold tracking-wider text-gray-900 hover:bg-cc-primary-600 active:bg-cc-primary-700 sm:text-sm">
                    Track
                  </button>
                  <button className="h-8 rounded-md bg-cc-secondary-500 px-2 text-xs font-semibold tracking-wider text-white hover:bg-cc-secondary-600 active:bg-cc-secondary-700 sm:text-sm">
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      className="h-5 w-5"
                      viewBox="0 0 20 20"
                      fill="currentColor"
                    >
                      <path
                        fill-rule="evenodd"
                        d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z"
                        clip-rule="evenodd"
                      />
                    </svg>
                  </button>
                </div>
              </div>
              <div className="flex items-center justify-between py-2">
                <div>
                  <h3 className="text-gray-800 sm:text-lg">Programming</h3>
                  <p className="text-sm text-gray-400">daily</p>
                </div>
                <div className="mr-5 flex items-center gap-2">
                  <button className="h-8 rounded-md bg-cc-primary-500 px-2 text-xs font-semibold tracking-wider text-gray-900 hover:bg-cc-primary-600 active:bg-cc-primary-700 sm:text-sm">
                    Track
                  </button>
                  <button className="h-8 rounded-md bg-cc-secondary-500 px-2 text-xs font-semibold tracking-wider text-white hover:bg-cc-secondary-600 active:bg-cc-secondary-700">
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      className="h-5 w-5"
                      viewBox="0 0 20 20"
                      fill="currentColor"
                    >
                      <path
                        fill-rule="evenodd"
                        d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z"
                        clip-rule="evenodd"
                      />
                    </svg>
                  </button>
                </div>
              </div>
              <div className="flex items-center justify-between py-2">
                <div>
                  <h3 className="text-gray-800 sm:text-lg">Working out</h3>
                  <p className="text-sm text-gray-400">6 times a month</p>
                </div>
                <div className="mr-5 flex items-center gap-2">
                  <button className="h-8 rounded-md bg-cc-primary-500 px-2 text-xs font-semibold tracking-wider text-gray-900 hover:bg-cc-primary-600 active:bg-cc-primary-700 sm:text-sm">
                    Track
                  </button>
                  <button className="h-8 rounded-md bg-cc-secondary-500 px-2 text-xs font-semibold tracking-wider text-white hover:bg-cc-secondary-600 active:bg-cc-secondary-700">
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      className="h-5 w-5"
                      viewBox="0 0 20 20"
                      fill="currentColor"
                    >
                      <path
                        fill-rule="evenodd"
                        d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z"
                        clip-rule="evenodd"
                      />
                    </svg>
                  </button>
                </div>
              </div>
            </div>
          </div>

          <div className="mt-10 w-full max-w-md rounded-xl bg-white p-8 shadow-lg sm:max-w-xl">
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
          </div>
        </div>
      </div>

      <footer className="body-font w-full bg-gray-100 text-gray-700">
        <div className="mx-auto flex max-w-lg flex-col flex-wrap px-5 py-10 md:max-w-2xl md:flex-row md:flex-nowrap md:items-center md:py-20 xl:max-w-7xl">
          <div className="mx-auto w-64 flex-shrink-0 text-center md:mx-0 md:text-left">
            <h4 className="flex items-center justify-center font-medium text-gray-900 md:justify-start">
              habitcentric
            </h4>
            <p className="mt-2 text-sm text-gray-500">
              maintained by codecentric
            </p>
            <div className="mt-4">
              <span className="mt-2 inline-flex justify-center sm:ml-auto sm:mt-0 sm:justify-start">
                <a className="cursor-pointer text-gray-500 hover:text-gray-700">
                  <svg
                    fill="currentColor"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    className="h-5 w-5"
                    viewBox="0 0 24 24"
                  >
                    <path d="M18 2h-3a5 5 0 00-5 5v3H7v4h3v8h4v-8h3l1-4h-4V7a1 1 0 011-1h3z"></path>
                  </svg>
                </a>
                <a className="ml-3 cursor-pointer text-gray-500 hover:text-gray-700">
                  <svg
                    fill="currentColor"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    className="h-5 w-5"
                    viewBox="0 0 24 24"
                  >
                    <path d="M23 3a10.9 10.9 0 01-3.14 1.53 4.48 4.48 0 00-7.86 3v1A10.66 10.66 0 013 4s-4 9 5 13a11.64 11.64 0 01-7 2c9 5 20 0 20-11.5a4.5 4.5 0 00-.08-.83A7.72 7.72 0 0023 3z"></path>
                  </svg>
                </a>
                <a className="ml-3 cursor-pointer text-gray-500 hover:text-gray-700">
                  <svg
                    fill="none"
                    stroke="currentColor"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    className="h-5 w-5"
                    viewBox="0 0 24 24"
                  >
                    <rect
                      width="20"
                      height="20"
                      x="2"
                      y="2"
                      rx="5"
                      ry="5"
                    ></rect>
                    <path d="M16 11.37A4 4 0 1112.63 8 4 4 0 0116 11.37zm1.5-4.87h.01"></path>
                  </svg>
                </a>
                <a className="ml-3 cursor-pointer text-gray-500 hover:text-gray-700">
                  <svg
                    fill="currentColor"
                    stroke="currentColor"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="0"
                    className="h-5 w-5"
                    viewBox="0 0 24 24"
                  >
                    <path
                      stroke="none"
                      d="M16 8a6 6 0 016 6v7h-4v-7a2 2 0 00-2-2 2 2 0 00-2 2v7h-4v-7a6 6 0 016-6zM2 9h4v12H2z"
                    ></path>
                    <circle cx="4" cy="4" r="2" stroke="none"></circle>
                  </svg>
                </a>
              </span>
            </div>
          </div>
          <div className="mt-10 -mb-10 flex flex-grow flex-wrap text-center md:mt-0 md:pl-20 md:text-left lg:justify-end xl:self-end">
            <div className="w-full px-4 md:w-1/2 xl:w-1/6">
              <h2 className="title-font mb-3 text-sm font-medium uppercase tracking-widest text-gray-900">
                About
              </h2>
              <nav className="mb-10 list-none">
                <li className="mt-3">
                  <a className="cursor-pointer text-gray-500 hover:text-gray-900">
                    Company
                  </a>
                </li>
                <li className="mt-3">
                  <a className="cursor-pointer text-gray-500 hover:text-gray-900">
                    Careers
                  </a>
                </li>
                <li className="mt-3">
                  <a className="cursor-pointer text-gray-500 hover:text-gray-900">
                    Blog
                  </a>
                </li>
              </nav>
            </div>
            <div className="w-full px-4 md:w-1/2 xl:w-1/6">
              <h2 className="title-font mb-3 text-sm font-medium uppercase tracking-widest text-gray-900">
                Contribute
              </h2>
              <nav className="mb-10 list-none">
                <li className="mt-3">
                  <a className="cursor-pointer text-gray-500 hover:text-gray-900">
                    Pull Request
                  </a>
                </li>
                <li className="mt-3">
                  <a className="cursor-pointer text-gray-500 hover:text-gray-900">
                    Documentation
                  </a>
                </li>
                <li className="mt-3">
                  <a className="cursor-pointer text-gray-500 hover:text-gray-900">
                    Releases
                  </a>
                </li>
              </nav>
            </div>
          </div>
        </div>
      </footer>
    </div>
  );
}

export default App;

import React from "react";

function Footer() {
  return (
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
                  <rect width="20" height="20" x="2" y="2" rx="5" ry="5"></rect>
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
  );
}

export default Footer;

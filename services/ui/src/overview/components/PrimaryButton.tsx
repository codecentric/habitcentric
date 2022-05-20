import React, { PropsWithChildren } from "react";

function PrimaryButton(
  props: PropsWithChildren<
    React.DetailedHTMLProps<
      React.ButtonHTMLAttributes<HTMLButtonElement>,
      HTMLButtonElement
    >
  >
) {
  return (
    <button
      {...props}
      className={`w-full rounded-lg bg-cc-primary-500 px-4 py-2 text-sm font-semibold tracking-wider text-gray-900 shadow-sm hover:bg-cc-primary-600 active:bg-cc-primary-700 sm:text-base ${props.className}`}
    >
      {props.children}
    </button>
  );
}

PrimaryButton.defaultProps = {
  type: "button",
};

export default PrimaryButton;

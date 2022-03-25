import React, { PropsWithChildren } from "react";

export type CardProps = PropsWithChildren<{
  title?: string;
  className?: string;
}>;

function Card({ title, className, children }: CardProps) {
  return (
    <div
      className={`w-full rounded-xl bg-white px-8 py-8 shadow-lg ${className}`}
    >
      {title && (
        <h1 className="text-xl font-semibold text-gray-800 sm:text-2xl">
          {title}
        </h1>
      )}

      {children}
    </div>
  );
}

Card.defaultProps = {
  title: null,
  className: "",
};

export default Card;

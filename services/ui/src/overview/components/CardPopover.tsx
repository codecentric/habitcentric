import React, { PropsWithChildren, useState } from "react";
import { Popover, Transition } from "@headlessui/react";
import Card from "./Card";
import { usePopper } from "react-popper";

export type CardPopoverProps = PropsWithChildren<{
  title: string;
  ariaLabel: string;
}>;

function CardPopover({ title, ariaLabel, children }: CardPopoverProps) {
  const [referenceElement, setReferenceElement] =
    useState<HTMLButtonElement | null>();
  const [popperElement, setPopperElement] = useState<HTMLDivElement | null>();
  const { styles, attributes } = usePopper(referenceElement, popperElement, {
    placement: "bottom-end",
    modifiers: [
      {
        name: "offset",
        options: {
          offset: [0, 10],
        },
      },
    ],
  });

  return (
    <Popover className="relative">
      <Popover.Button
        className="h-8 rounded-md bg-cc-primary-500 px-2 text-xs font-semibold tracking-wider text-gray-900 hover:bg-cc-primary-600 active:bg-cc-primary-700 sm:text-sm"
        aria-label={ariaLabel}
        ref={setReferenceElement}
      >
        {title}
      </Popover.Button>
      <OverlayTransition>
        <Popover.Overlay className="fixed inset-0 bg-black opacity-50" />
      </OverlayTransition>
      <div
        className="z-10"
        ref={setPopperElement}
        style={styles.popper}
        {...attributes.popper}
      >
        <PopoverTransition>
          <Popover.Panel>
            <Card>{children}</Card>
          </Popover.Panel>
        </PopoverTransition>
      </div>
    </Popover>
  );
}

function OverlayTransition({ children }: PropsWithChildren<{}>) {
  return (
    <Transition
      className="isolate"
      enter="transition duration-200 ease-out"
      enterFrom="opacity-0"
      enterTo="opacity-100"
      leave="transition duration-150 ease-in"
      leaveFrom="opacity-100"
      leaveTo="opacity-0"
    >
      {children}
    </Transition>
  );
}

function PopoverTransition({ children }: PropsWithChildren<{}>) {
  return (
    <Transition
      className=""
      enter="transition ease-out duration-200"
      enterFrom="opacity-0 translate-y-1"
      enterTo="opacity-100 translate-y-0"
      leave="transition ease-in duration-150"
      leaveFrom="opacity-100 translate-y-0"
      leaveTo="opacity-0 translate-y-1"
    >
      {children}
    </Transition>
  );
}

export default CardPopover;

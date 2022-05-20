import React from "react";

export type LinkSectionProps = LinkSectionData & {
  className: string;
};

export type LinkSectionData = {
  title: string;
  links: Array<Link>;
};

type Link = {
  text: string;
  href: string;
};

function LinkSection({ title, links, className = "" }: LinkSectionProps) {
  return (
    <div className={className}>
      <h2 className="title-font mb-3 text-sm font-medium uppercase tracking-widest text-gray-900">
        {title}
      </h2>
      <nav className="mb-10 list-none">
        {links?.map((link) => (
          <li className="mt-3" key={link.text}>
            <a
              href={link.href}
              className="cursor-pointer text-gray-500 hover:text-gray-900"
            >
              {link.text}
            </a>
          </li>
        ))}
      </nav>
    </div>
  );
}

LinkSection.defaultProps = {
  className: "",
  links: [],
};

export default LinkSection;

import React from "react";
import LinkSection, { LinkSectionData } from "./LinkSection";
import Codecentric from "../cc-black-logo.svg?react";
import { SiInstagram, SiLinkedin, SiTwitter } from "@icons-pack/react-simple-icons";

const linkSections: Array<LinkSectionData> = [
  {
    title: "About",
    links: [
      { text: "Company", href: "https://codecentric.de" },
      { text: "Careers", href: "https://codecentric.de/karriere" },
      { text: "Blog", href: "https://blog.codecentric.de" },
    ],
  },
  {
    title: "Contribute",
    links: [
      {
        text: "Repository",
        href: "https://github.com/codecentric/habitcentric",
      },
      {
        text: "Pull Request",
        href: "https://github.com/codecentric/habitcentric#-contributing",
      },
      {
        text: "Releases",
        href: "https://github.com/orgs/codecentric/packages?repo_name=habitcentric",
      },
    ],
  },
];

function Footer() {
  return (
    <footer className="body-font w-full bg-gray-100 text-gray-700">
      <div className="mx-auto flex max-w-lg flex-col flex-wrap px-5 py-10 md:max-w-2xl md:flex-row md:flex-nowrap md:items-center md:py-20 xl:max-w-7xl">
        <div className="mx-auto w-64 flex-shrink-0 text-center md:mx-0 md:text-left">
          <BrandWithCaption />
          <div className="mt-4">
            <SocialMediaIcons />
          </div>
        </div>
        <div className="-mb-10 mt-10 flex flex-grow flex-wrap text-center md:mt-0 md:pl-20 md:text-left lg:justify-end">
          {linkSections.map((linkSection) => (
            <LinkSection
              className="w-full px-4 md:w-1/2 md:pl-8 xl:w-1/4 xl:pl-24"
              {...linkSection}
              key={linkSection.title}
            />
          ))}
        </div>
      </div>
    </footer>
  );
}

function BrandWithCaption() {
  return (
    <>
      <h4 className="flex items-center justify-center font-medium text-gray-900 md:justify-start">
        habitcentric
      </h4>
      <p className="mt-2 text-sm text-gray-500">maintained by codecentric</p>
    </>
  );
}

export function SocialMediaIcons() {
  return (
    <span className="mt-2 inline-flex items-center justify-center sm:ml-auto sm:mt-0 sm:justify-start">
      <a href="https://codecentric.de" className="cursor-pointer text-gray-500 hover:text-gray-700">
        <Codecentric className="h-5 w-5 fill-gray-500 hover:fill-gray-700" />
      </a>
      <a
        href="https://twitter.com/codecentric"
        className="ml-3 cursor-pointer text-gray-500 hover:text-gray-700"
      >
        <SiTwitter className="h-5 w-5 fill-gray-500 hover:fill-gray-700" />
      </a>
      <a
        href="https://www.instagram.com/codecentricag"
        className="ml-3 cursor-pointer text-gray-500 hover:text-gray-700"
      >
        <SiInstagram className="h-5 w-5 fill-gray-500 hover:fill-gray-700" />
      </a>
      <a
        href="https://linkedin.com/company/codecentric-ag"
        className="ml-3 cursor-pointer text-gray-500 hover:text-gray-700"
      >
        <SiLinkedin className="h-5 w-5 fill-gray-500 hover:fill-gray-700" />
      </a>
    </span>
  );
}

export default Footer;

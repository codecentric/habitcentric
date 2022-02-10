import codecentricBlackLogo from "../cc-black-logo.svg";
import { Github } from "@icons-pack/react-simple-icons";

function Header() {
  return (
    <div className="flex justify-between bg-white shadow-sm">
      <div className="ml-6 flex items-center">
        <img
          className="h-12 p-3"
          src={codecentricBlackLogo}
          alt="codecentric logo"
        />
        <h1 className="text-xl font-medium text-gray-800">habitcentric</h1>
      </div>
      <div className="mr-6 flex items-center">
        <a href="https://github.com/codecentric/habitcentric">
          <Github
            // @ts-ignore
            title="GitHub repository"
            className="fill-gray-700 hover:fill-gray-500 active:fill-gray-400"
            size={24}
          />
        </a>
      </div>
    </div>
  );
}

export default Header;

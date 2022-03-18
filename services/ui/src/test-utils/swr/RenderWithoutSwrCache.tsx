import { render } from "@testing-library/react";
import { SWRConfig } from "swr";

export function renderWithoutSwrCache(ui: any, options?: any) {
  return render(
    <SWRConfig value={{ provider: () => new Map() }}>{ui}</SWRConfig>,
    options
  );
}

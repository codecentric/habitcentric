import axios from "axios/index";
import DeepMock from "jest-deep-mock";
import { ApiService } from "../../../src/api/ApiService";

jest.mock("axios/index");

describe("ApiService", () => {
  let axiosInstanceMock = new DeepMock();

  beforeEach(() => {
    axios.create.mockReturnValue(axiosInstanceMock);
  });

  afterEach(() => {
    axiosInstanceMock.interceptors.request.use.mockReset();
  });

  it("should add http request interceptor when oidc is enabled", () => {
    process.env.VUE_APP_OIDC_AUTH = "enabled";

    new ApiService("_");

    expect(axiosInstanceMock.interceptors.request.use).toHaveBeenCalledTimes(1);
  });

  it("should not add http request interceptor when oidc is disabled", () => {
    process.env.VUE_APP_OIDC_AUTH = "disabled";

    new ApiService("_");

    expect(axiosInstanceMock.interceptors.request.use).toHaveBeenCalledTimes(0);
  });

  it("should not add http request interceptor when oidc is undefined", () => {
    delete process.env.VUE_APP_OIDC_AUTH;

    new ApiService("_");

    expect(axiosInstanceMock.interceptors.request.use).toHaveBeenCalledTimes(0);
  });
});

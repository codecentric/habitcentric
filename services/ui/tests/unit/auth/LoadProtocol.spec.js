import { loadProtocol } from "../../../src/auth/LoadProtocol";

global.window = Object.create(window);
Object.defineProperty(window, "location", {
  value: { protocol: "" },
  writable: true
});

describe("load protocol", () => {
  const mockLocationProtocol = protocol => {
    window.location.protocol = protocol;
  };

  describe("given actual protocol http", () => {
    it("does not change url if protocol is the same", () => {
      mockLocationProtocol("http:");
      const result = loadProtocol("http://example.com/test?bla=neu");
      expect(result).toEqual("http://example.com/test?bla=neu");
    });

    it("changes url if protocol is not the same", () => {
      mockLocationProtocol("https:");
      const result = loadProtocol("http://example.com/test?bla=neu");
      expect(result).toEqual("https://example.com/test?bla=neu");
    });
  });

  describe("given actual protocol https", () => {
    it("does not change url if protocol is the same", () => {
      mockLocationProtocol("https:");
      const result = loadProtocol("https://example.com/test?bla=neu");
      expect(result).toEqual("https://example.com/test?bla=neu");
    });

    it("changes url if protocol is not the same", () => {
      mockLocationProtocol("http:");
      const result = loadProtocol("https://example.com/test?bla=neu");
      expect(result).toEqual("http://example.com/test?bla=neu");
    });
  });

  it("adds protocol to url if protocol is missing", () => {
    mockLocationProtocol("http:");
    const result = loadProtocol("example.com/test?bla=neu");
    expect(result).toEqual("http://example.com/test?bla=neu");
  });
});

const reactOidcContext = jest.createMockFromModule("react-oidc-context");

function useAuth() {
  return {
    user: {
      access_token: "access_token",
    },
  };
}

reactOidcContext.useAuth = useAuth;

module.exports = reactOidcContext;

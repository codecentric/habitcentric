# Infrastructure Test for Habitcentric Service Mesh Deployment

The idea is to have an automated test suite which verifies that the
mesh deployment delivers everything we need.

The test suite is using [Groovy](https://groovy-lang.org/), [Spock](http://spockframework.org/) and
[Kubernetes Java Client](https://github.com/kubernetes-client/java).

# Specs and scope
What do we want to test?

* Behaviour of certain configurations
* Authorization Rules

## Habitcentric

* Checks that opening `habitcentric.demo/` redirects to `habitcentric.demo/ui`.

## Security
There are certain rules in place to secure the environment:
1. disallow requests without OIDC Tokens to the habitcentric services
2. only allow connections to the database instances from the application
  pods that need the database

To test 1. we just run http requests against the services

To verify 2. we deploy a pod into a namespace running alpine,
install every tools required to run the tests and then run various
cli commands.

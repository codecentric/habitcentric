# Infrastructure Test for Habitcentric Service Mesh Deployment

The idea is to have an automated test suite which verifies that the mesh deployment delivers
everything we need.

The test suite is using [Groovy](https://groovy-lang.org/), [Spock](http://spockframework.org/) and
[Kubernetes Java Client](https://github.com/kubernetes-client/java).

# Testable Deployment Environments

Currently, only the following deployment environments are testable with this test suite:

- istio
- linkerd

**The default environment is `istio`**

# How to run?

You can run the test with gradle (this example will run the istio suite):

```shell
./gradlew test
```

or run them from your IDE.

## Configuration properties

| Property                   | Default | Description                     |
|----------------------------|---------|---------------------------------|
| `habitcentric.environment` | `istio` | Deployment environment to test. |
| `habitcentric.https`       | `false` | If HTTPS should be used.        |

To run the test with a configuration property:

Via gradle: just pass as many `-D` options as needed.

```shell
./gradlew test -Dhabitcentric.environment=linkerd -Dhabitcentric.https=true
```

Via your IDE: add the options to the task it creates.

- IntelliJ delegates to gradle by default, make sure to add the gradle options to the `Arguments`
  setting and not the `VM options`

# Specs and scope

What do we want to test?

* Behaviour general Habitcentric configurations
* Authorization rules

## Habitcentric

* Checks that opening `habitcentric.demo/` redirects to `habitcentric.demo/ui`.

## Security

There are certain rules in place to secure the environment:

1. disallow requests without OIDC Tokens to the habitcentric services
2. only allow connections to the database instances from the application pods that need the database

To test 1. we just run http requests against the services

To verify 2. we deploy a pod into a namespace running alpine, install every tools required to run
the tests and then run various cli commands.

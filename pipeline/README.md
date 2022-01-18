# 🧱 habitcentric Pipeline

Runs the builds for all services in parallel jobs.

## Relevant files

- [`.gitlab-ci.yml`](.gitlab-ci.yml): Central pipeline definition. Includes everything.
- [`pipeline/gitlab-jvm.yml`](pipeline/gitlab-jvm.yml): Hidden jobs for jvm test & build
- [`pipeline/gitlab-docker.yml`](pipeline/gitlab-docker.yml): Hidden jobs for docker
- [`habit/.gitlab-ci.yml`](habit/.gitlab-ci.yml): Pipeline for habit service
- [`report/.gitlab-ci.yml`](report/.gitlab-ci.yml): Pipeline for report service
- [`track/.gitlab-ci.yml`](track/.gitlab-ci.yml): Pipeline for track service
- [`ui/.gitlab-ci.yml`](ui/.gitlab-ci.yml): Pipeline for ui

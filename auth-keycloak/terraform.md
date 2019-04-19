# Terraform

## Setup
A private key for a privileged Google Cloud account (with editor role) needs to be provided. 
The environment variable `GOOGLE_APPLICATION_CREDENTIALS` is used to specify its path (e.g. `/Users/jane/gcp-account-key-terraform.json`).
Then you can call this to initialize Terraform:
```
terraform init
```

## Terraform Lifecycle
1. [**Validate**](https://www.terraform.io/docs/commands/validate.html):
Will fail in case of syntax errors in any Terraform file.
2. [**Plan**](https://www.terraform.io/docs/commands/plan.html):
It's not required, but recommend to analyze changes to the infrastructure before actually performing them.
`plan` will determine which actions have to be taken to create the desired state.
3. [**Apply**](https://www.terraform.io/docs/commands/apply.html):
The infrastructure is changed according to the planned state.
References between resources in the `*.tf` files enable Terraform to figure out dependencies and create resources in the right order.
4. [**Destroy**](https://www.terraform.io/docs/commands/destroy.html):
All specified resources will be deleted.
You should avoid creating resource without Terraform.
Otherwise Terraform might try to deleted resources that are still used and fail due to dependencies that Terraform is not aware of.

## Terraform Commands
* `terraform validate`
* `terraform plan`
* `terraform apply`
* `terraform detroy`

## Terraform Pipeline with GitLab
GitLab CI is used to execute Terraform centrally.
There is a stage that needs to triggered manually to apply changes.
To destroy the cluster, you can run a pipeline with an input variable `TERRAFORM_ACTION` having the value `destroy`.
In order to reduce costs, a scheduled pipeline destroys the database on a daily basis.

## Terraform Remote State
To enable teams to work with Terraform on a shared infrastructure a central state needs to be used.
This remote state is stored in a Google Cloud Storage bucket.
The bucket has been created manually and can be managed with `gsutil`:

| Command                                                         | Description                       |
|-----------------------------------------------------------------|-----------------------------------|
| `gsutil mb -c regional -l europe-west3 gs://keycloak-terraform` | Create bucket                     |
| `gsutil rb gs://keycloak-terraform`                             | Delete bucket (needs to be empty) |
| `gsutil rm gs://keycloak-terraform/**`                          | Delete objects from the bucket    |
| `gsutil rm -r gs://keycloak-terraform`                          | Delete bucket and its objects     |

{{/* vim: set filetype=mustache: */}}
{{/*
Expand the name of the chart.
*/}}
{{- define "track.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
*/}}
{{- define "track.fullname" -}}
{{- if .Values.fullnameOverride -}}
{{- printf .Values.fullnameOverride | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- $name := default .Chart.Name .Values.nameOverride -}}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" -}}
{{- end -}}
{{- end -}}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "track.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Return the proper habitcentric init image name
*/}}
{{- define "track.init.image" -}}
{{- $registryName := .Values.init.image.registry -}}
{{- $repositoryName := .Values.init.image.repository -}}
{{- $tag := .Values.image.tag | toString -}}
{{/*
Helm 2.11 supports the assignment of a value to a variable defined in a different scope,
but Helm 2.9 and 2.10 doesn't support it, so we need to implement this if-else logic.
Also, we can't use a single if because lazy evaluation is not an option
*/}}
{{- if .Values.global }}
    {{- if .Values.global.imageRegistry }}
        {{- printf "%s/%s:%s" .Values.global.imageRegistry $repositoryName $tag -}}
    {{- else -}}
        {{- printf "%s/%s:%s" $registryName $repositoryName $tag -}}
    {{- end -}}
{{- else -}}
    {{- printf "%s/%s:%s" $registryName $repositoryName $tag -}}
{{- end -}}
{{- end -}}

{{/*
Return the proper habitcentric track image name
*/}}
{{- define "track.image" -}}
{{- $registryName := .Values.image.registry -}}
{{- $repositoryName := .Values.image.repository -}}
{{- $tag := .Values.image.tag | toString -}}
{{/*
Helm 2.11 supports the assignment of a value to a variable defined in a different scope,
but Helm 2.9 and 2.10 doesn't support it, so we need to implement this if-else logic.
Also, we can't use a single if because lazy evaluation is not an option
*/}}
{{- if .Values.global }}
    {{- if .Values.global.imageRegistry }}
        {{- printf "%s/%s:%s" .Values.global.imageRegistry $repositoryName $tag -}}
    {{- else -}}
        {{- printf "%s/%s:%s" $registryName $repositoryName $tag -}}
    {{- end -}}
{{- else -}}
    {{- printf "%s/%s:%s" $registryName $repositoryName $tag -}}
{{- end -}}
{{- end -}}

{{/*
Create a default fully qualified app name for the postgres requirement.
*/}}
{{- define "track.postgresql.fullname" -}}
{{- $postgresContext := dict "Values" .Values.postgresql "Release" .Release "Chart" (dict "Name" "postgresql") -}}
{{ template "postgresql.fullname" $postgresContext }}
{{- end -}}

{{/*
Create environment variables for database configuration
*/}}
{{- define "track.persistence.vars" }}
{{- if .Values.persistence.deployPostgres }}
- name: DB_HOST
  value: {{ template "track.postgresql.fullname" . }}
- name: DB_PORT
  value: "5432"
- name: DB_NAME
  value: {{ .Values.postgresql.postgresqlDatabase | quote }}
- name: DB_USER
  value: {{ .Values.postgresql.postgresqlUsername | quote }}
- name: DB_PASSWORD
  valueFrom:
    secretKeyRef:
      name: {{ template "track.postgresql.fullname" . }}
      key: postgresql-password
{{- else }}
- name: DB_NAME
  value: {{ .Values.persistence.dbName }}
- name: DB_HOST
  value: {{ .Values.persistence.dbHost }}
- name: DB_PORT
  value: {{ .Values.persistence.dbPort }}
- name: DB_USER
  value: {{ .Values.persistence.dbUser }}
- name: DB_PASSWORD
  value: {{ .Values.persistence.dbPassword }}
{{- end }}
{{- end -}}

{{/*
Common labels
*/}}
{{- define "track.labels" -}}
helm.sh/chart: {{ include "track.chart" . }}
{{ include "track.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end -}}

{{/*
Selector labels
*/}}
{{- define "track.selectorLabels" -}}
app.kubernetes.io/name: {{ include "track.name" . }}
app.kubernetes.io/component: track
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end -}}
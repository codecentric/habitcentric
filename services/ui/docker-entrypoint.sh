#!/bin/sh
indexFile="/usr/share/nginx/html/dist/index.html"
appFile="$(ls /usr/share/nginx/html/dist/index*.js)"
envsubst '$OIDC_ENABLED' < ${indexFile} > index.html.tmp
envsubst '$OIDC_ENABLED' < ${appFile} > index.js.tmp
mv index.html.tmp ${indexFile}
mv index.js.tmp ${appFile}
exec "$@"

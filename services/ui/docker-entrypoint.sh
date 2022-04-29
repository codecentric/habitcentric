#!/bin/sh
indexFile="/usr/share/nginx/html/dist/index.html"
appFile="$(ls /usr/share/nginx/html/dist/static/js/main*.js)"
envsubst '$OIDC_ENABLED,$PUBLIC_URL' < ${indexFile} > index.html.tmp
envsubst '$OIDC_ENABLED,$PUBLIC_URL' < ${appFile} > main.js.tmp
mv index.html.tmp ${indexFile}
mv main.js.tmp ${appFile}
exec "$@"

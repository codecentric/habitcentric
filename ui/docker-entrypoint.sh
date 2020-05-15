#!/bin/sh
appFile="$(ls /usr/share/nginx/html/dist/js/app*.js)"
envsubst '$HABIT_HOST,$TRACKING_HOST,$REPORT_HOST,$OIDC_AUTH,$OIDC_AUTHORITY_HOST,$OIDC_CLIENT_ID' < ${appFile} > app.js.tmp
mv app.js.tmp ${appFile}
exec "$@"
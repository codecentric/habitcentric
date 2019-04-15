#!/bin/sh
appFile="$(ls /usr/share/nginx/html/dist/js/app*.js)"
envsubst '$HABIT_SERVICE_HOST,$TRACKING_SERVICE_HOST' < ${appFile} > app.js.tmp
mv app.js.tmp ${appFile}
exec "$@"
#!/bin/sh
appFile="$(ls /usr/share/nginx/html/dist/js/app*.js)"
envsubst '$HABIT_HOST,$TRACKING_HOST' < ${appFile} > app.js.tmp
mv app.js.tmp ${appFile}
exec "$@"
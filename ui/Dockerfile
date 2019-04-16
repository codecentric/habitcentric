FROM node:10.15.0-alpine as build
WORKDIR /app
COPY package.json yarn.lock ./
RUN yarn install
COPY .env.production .eslintrc.js babel.config.js postcss.config.js vue.config.js ./
COPY public public/
COPY src src/
RUN yarn run build

FROM nginx:1.15.8-alpine as run
ENV HABIT_SERVICE_HOST /
ENV TRACKING_SERVICE_HOST /
COPY --from=build /app/dist /usr/share/nginx/html/dist
COPY nginx.conf /etc/nginx/nginx.conf
COPY ./docker-entrypoint.sh /
RUN chmod +x /docker-entrypoint.sh
EXPOSE 80
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]

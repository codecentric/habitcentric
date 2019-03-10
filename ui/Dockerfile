FROM node:10.15.0-alpine as build
WORKDIR /app
COPY package.json yarn.lock ./
RUN yarn install
COPY .env.production .eslintrc.js babel.config.js postcss.config.js vue.config.js ./
COPY public public/
COPY src src/
RUN yarn run build

FROM nginx:1.15.8-alpine as run
COPY --from=build /app/dist /usr/share/nginx/html/dist
COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]

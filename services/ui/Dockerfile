FROM node:14.19.0-alpine@sha256:43ec2ba01c2a245e341489956258afd3535a9ebfdf87ade04b84299ca736607f as build
WORKDIR /app
COPY package.json yarn.lock ./
RUN yarn install
COPY .env.production .eslintrc.js babel.config.js postcss.config.js vue.config.js ./
COPY public public/
COPY src src/
RUN yarn run build

FROM nginx:1.16.0-alpine@sha256:270bea203d2fc3743fb9ce0193325e188b7e6233043487e3d3cf117ea4d3f337 as run
ENV HABIT_HOST /
ENV TRACKING_HOST /
ENV REPORT_HOST /
COPY --from=build /app/dist /usr/share/nginx/html/dist
COPY nginx.conf /etc/nginx/nginx.conf
COPY ./docker-entrypoint.sh /
RUN chmod +x /docker-entrypoint.sh
EXPOSE 9004
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]

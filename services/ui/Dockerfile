FROM node:17.4.0 as build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install
COPY .env.production tailwind.config.js tsconfig.json ./
COPY public public/
COPY src src/
RUN npm run build

FROM nginx:1.23.0 as run
COPY --from=build /app/build /usr/share/nginx/html/dist
COPY nginx.conf /etc/nginx/nginx.conf
COPY ./docker-entrypoint.sh /
RUN chmod +x /docker-entrypoint.sh
EXPOSE 9004
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]

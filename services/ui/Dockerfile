FROM nginx:1.29.4
COPY build/ /usr/share/nginx/html/dist
COPY nginx.conf /etc/nginx/nginx.conf
COPY ./docker-entrypoint.sh /
RUN chmod +x /docker-entrypoint.sh
EXPOSE 9004
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]

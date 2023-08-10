FROM nginx:latest
COPY ./dev-server.conf /etc/nginx/conf.d/dev-server.conf
RUN mkdir -p /var/www/dev-server/html/ && \
chown -R www-data:www-data /var/www/dev-server/html/ && \
apt-get update -y && \
apt-get install -y certbot python3-certbot-nginx
# CMD nginx && SERVER_NAME= certbot --noninteractive --agree-tos -m cliffordhc@itinordic.com --nginx -d dev.hispzw.org --redirect && nginx -s stop && nginx -g "daemon off;"
CMD nginx -g "daemon off;"

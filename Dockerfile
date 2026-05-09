FROM php:8.2-apache

# Cập nhật hệ thống và cài đặt các thư viện cần thiết cho mysqli
RUN apt-get update && apt-get install -y \
    libmariadb-dev \
    && docker-php-ext-install mysqli \
    && docker-php-ext-enable mysqli

COPY . /var/www/html/


RUN chown -R www-data:www-data /var/www/html

EXPOSE 80
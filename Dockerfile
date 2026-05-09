FROM php:8.2-apache

# Cập nhật và cài đặt các phụ thuộc hệ thống cần thiết
RUN apt-get update && apt-get install -js -y \
    libmysqli-dev \
    && docker-php-ext-install mysqli \
    && docker-php-ext-enable mysqli

COPY . /var/www/html/

# Phân quyền cho thư mục web (đảm bảo Apache có quyền đọc)
RUN chown -R www-data:www-data /var/www/html

EXPOSE 80
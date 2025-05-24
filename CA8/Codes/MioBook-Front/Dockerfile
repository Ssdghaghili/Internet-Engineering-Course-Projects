FROM node:20 AS builder
WORKDIR /app
COPY . .
RUN npm install && npm run build

FROM nginx:alpine

COPY --from=builder /app/dist /usr/share/nginx/html

COPY nginx.conf.template /etc/nginx/templates/default.conf.template

EXPOSE 80
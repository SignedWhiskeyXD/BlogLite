FROM node:16-alpine
COPY ./ /bloglite-admin
WORKDIR /bloglite-admin
RUN npm install && npm run build

FROM nginx
RUN mkdir /bloglite-admin-html
COPY --from=0 /bloglite-admin/dist /bloglite-admin-html
COPY nginx.conf /etc/nginx/nginx.conf


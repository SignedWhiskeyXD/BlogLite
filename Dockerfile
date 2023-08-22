FROM node:16-alpine
COPY ./BlogLite-Admin /bloglite-admin
COPY ./BlogLite-View /bloglite-view
WORKDIR /bloglite-admin
RUN npm install && npm run build
WORKDIR /bloglite-view
RUN npm install && npm run build

FROM nginx
RUN mkdir /bloglite-admin-html
RUN mkdir /bloglite-view-html
COPY --from=0 /bloglite-admin/dist /bloglite-admin-html
COPY --from=0 /bloglite-view/dist /bloglite-view-html
COPY nginx.conf /etc/nginx/nginx.conf
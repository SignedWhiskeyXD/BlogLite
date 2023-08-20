FROM node:16-alpine
COPY ./ /bloglite-view
WORKDIR /bloglite-view
RUN npm install && npm run build

FROM nginx
RUN mkdir /bloglite-view-html
COPY --from=0 /bloglite-view/dist /bloglite-view-html
COPY nginx.conf /etc/nginx/nginx.conf


# Build stage
FROM node:lts-alpine as build
ARG frontend_production
ARG backend_url
ARG backend_jwt_client_id
ARG backend_jwt_client_secret
ENV UCM_FRONTEND_PRODUCTION=$frontend_production
ENV UCM_BACKEND_URL=$backend_url
ENV UCM_BACKEND_JWT_CLIENT_ID=$backend_jwt_client_id
ENV UCM_BACKEND_JWT_CLIENT_SECRET=$backend_jwt_client_secret
WORKDIR /usr/src/app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Run stage
FROM nginx:latest
COPY --from=build /usr/src/app/dist/ucm-frontend /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]

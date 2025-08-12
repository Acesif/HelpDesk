# Stage 1: Build the Angular application
FROM node:20 AS build

WORKDIR /usr/src/app

COPY package*.json ./
RUN npm install

COPY . .
RUN npx ng build --base-href /helpdesk/

# Stage 2: Serve the application using http-server
FROM node:20-slim

WORKDIR /usr/src/app

RUN npm install -g http-server

# Copy build output into 'helpdesk' folder
COPY --from=build /usr/src/app/dist/client/browser/ ./helpdesk

EXPOSE 4321

CMD [ "http-server", "-p", "4321" ]

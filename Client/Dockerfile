# Stage 1: Build the Angular application
FROM node:20 AS build

# Set the working directory
WORKDIR /usr/src/app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application source code
COPY . .

# Build the application for production
RUN npx ng build

# Stage 2: Serve the application using http-server
FROM node:20-slim

WORKDIR /usr/src/app

# Install http-server globally
RUN npm install -g http-server

# Copy the built application from the 'build' stage
COPY --from=build /usr/src/app/dist/client/browser/ ./

# Expose the port the app will run on
EXPOSE 4321

# Start the http-server on port 4321
# Note: For full SPA support (to handle deep linking), you may need to configure a rewrite for all routes to index.html.
# A simple way to do this with http-server is to use a proxy fallback, like:
# CMD [ "http-server", "-p", "4321", "-P", "http://localhost:4321" ]
# However, a more robust solution might involve a different server or custom configuration.
CMD [ "http-server", "-p", "4321" ]

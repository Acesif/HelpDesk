# Use official Node.js image as a base
FROM node:18 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json (if available) to the container
COPY package*.json ./

# Install the application dependencies
RUN npm install

# Copy the rest of the application source code
COPY . .

# Build the Angular application for production
RUN npm run build --prod

# Install http-server to serve the Angular app
RUN npm install -g http-server

# Set the working directory to the build output folder
WORKDIR /app/dist/client

# Expose port 4300 for the application
EXPOSE 4300

# Serve the Angular app using http-server
CMD ["http-server", "-p", "4300"]

# Use the official Node.js image as the base image
FROM node

# Set a label for your Docker image description
LABEL description="This is a demo Node.js application image"

# Set the working directory inside the container
WORKDIR /app

# Copy the contents of the current directory to the working directory inside the container
ADD . /app

# Install Node.js dependencies
RUN npm install

# Expose port 3000 to the host
EXPOSE 3000

# Start your Node.js application
CMD npm start

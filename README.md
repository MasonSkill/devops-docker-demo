# Learn DevOps - Build a NodeJs App

CI/CD with Jenkins using Pipelines and Docker

This is the course material for the Jenkins Course on Udemy ([Learn DevOps: CI/CD with Jenkins using Pipelines and Docker](https://www.udemy.com/learn-devops-ci-cd-with-jenkins-using-pipelines-and-docker/?couponCode=JENKINS_GIT))

# Run nodejs demo in Jenksin
## Install nodejs plugin 
Install latest nodejs plugin named "NodeJS" under "Dashboard > Manage Jenkins > Plugins" in Jenkins portal.

## Configure nodejs tool
Enable nodejs Under "Dashboard > Manage Jenkins > Tools"
Make it install automatically
The version should be LTS. By the time it is 18.6.1

## Create a pipeline
Create a pipeline named "nodejs-build-js" and copy jenkins/nodejs-build-js.groovy as the pipeline

## Run the pipeline
Run the pipeline

## Verify build result
1. The console should show the nodejs version
2. Check if content exists under "/var/jenkins_home/workspace/nodejs-build-js/node_modules"
3. Under "/var/jenkins_home/workspace/nodejs-build-js/" run 

    /var/jenkins_home/tools/jenkins.plugins.nodejs.tools.NodeJSInstallation/nodejs/bin/node index.js

4. Access node service

    curl http://localhost:3000

5. You'll see "Hello World!"

# [Docker CLI] Run nodejs demo in docker

## Commands to build docker image

    docker build -t masonskill/node-demo:latest .

    docker tag masonskill/node-demo:latest masonskill/node-demo:latest

    docker push masonskill/node-demo:latest

## Commands to pull and run docker image

    docker pull masonskill/node-demo:latest

    docker run -d -p 3000:3000 --name node-demo masonskill/node-demo:latest

# [Jenkins Pipeline] Run nodejs demo in docker
## Configure docker credential tool
Go to "Dashboard > Manage Jenkins > Credentials > System > Global credentials (unrestricted)"
Add Credential for docker with Username, Password and ID(docker-hub-credentials)

## Create a pipeline
Create a pipeline named "nodejs-build-docker" and copy jenkins/nodejs-build-docker.groovy as the pipeline

## Run the pipeline
Run the pipeline

## Verify build result
1. Go to https://hub.docker.com/repository/docker/masonskill/node-demo/general to see if the image has been uploaded.
2. Pull and run docker

    docker run -d -p 3000:3000 --name node-demo masonskill/node-demo:latest

3. Access node service

    curl http://localhost:3000

4. You'll see "Hello World!"


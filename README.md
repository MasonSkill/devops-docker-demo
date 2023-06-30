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
Create a pipeline named "nodejs-standalone" and copy jenkins/nodejs-standalone.groovy as the pipeline

## Run the pipeline
Run the pipeline to check the two results
1. The counsole should show the nodejs version
2. Check if content existance under "/var/jenkins_home/workspace/nodejs/node_modules"

# Run nodejs demo in docker

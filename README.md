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

## [Script mode] Create a pipeline
Create a pipeline named "nodejs-build-docker-script-mode"

Select the pipeline definition type as "Pipeline script"

Copy the content from [jenkins/nodejs-build-docker-script-mode.groovy](./jenkins/nodejs-build-docker-script-mode.groovy) as the script

## [SCM mode] Create a pipeline
Create a pipeline named "nodejs-build-docker-scm-mode"

Select the pipeline definition type as "Pipeline script from SCM"

Set the following values

    SCM: Git
    Repository URL: https://github.com/MasonSkill/devops-docker-demo
    Credentials: - none -
    Branch Specifier: */mason
    Script Path: ./jenkins/nodejs-build-docker-scm-mode.groovy

## Run the pipeline
Run the pipeline

## Verify build result
1. Go to https://hub.docker.com/repository/docker/masonskill/node-demo/general to see if the image has been uploaded.
2. Pull and run docker

    docker run -d -p 3000:3000 --name node-demo masonskill/node-demo:latest

3. Access node service

    curl http://localhost:3000

4. You'll see "Hello World!"

# Build, test and run in different docker container
Copy the content from [jenkins/nodejs-build-docker-script-mode_v2.groovy](./jenkins/nodejs-build-docker-script-mode_v2.groovy) as the script

[Important] The following two codes are equal in function
Create docker image with docker function.

    stage('Test') {
        steps {
            script {
                def myTestContainer = docker.image('node:16')
                myTestContainer.pull()
                myTestContainer.inside {
                    sh 'npm install --only=dev'
                    sh 'npm test'
                }
            }
        }
    }

Use docker agent function

    stage('Test') {
            agent {
                docker {
                    image 'node:16'
                    reuseNode true
                }
            }
            steps {
                sh 'node -v'
                sh 'npm install --only=dev'
                sh 'npm test'
            }
        }
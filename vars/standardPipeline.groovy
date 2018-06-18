 /* Update this */
 
 /* A global template for doing Jenkinsfile builds, so we dont have to dupe Jenkins file in 
    every project */
 
 def call(body) {

        def config = [:]
        body.resolveStrategy = Closure.DELEGATE_FIRST
        body.delegate = config
        body()

        node {
            // Clean workspace before doing anything
            deleteDir()

            try {
				
				stage ('Monkey') {
					
				}
				
				stage ('Init') {
					sh "echo ************************"
					sh "echo ********* INIT *********"
					sh "env"
					sh "echo ************************"

				}
            
                stage ('Clone') {
                    sh "echo 'STP stevesVar=${config.stevesVar}'"
                    sh "echo 'STP env.BUILD_ID=${env.BUILD_ID}'"
                    checkout scm
                }
                
                stage ('Build') {
                    echo "STP Running ${env.BUILD_ID} on ${env.JENKINS_URL}"
                    sh "echo 'STP building ${config.projectName} ...'"
                }
            
         /*
                
                stage ('Tests') {
                    parallel 'static': {
                        sh "echo 'shell scripts to run static tests...'"
                    },
                    'unit': {
                        sh "echo 'shell scripts to run unit tests...'"
                    },
                    'integration': {
                        sh "echo 'shell scripts to run integration tests...'"
                    }
                }
                stage ('Deploy') {
                    sh "echo 'deploying to server ${config.serverDomain}...'"
                }
                
         */
                
            } catch (err) {
                currentBuild.result = 'FAILED'
                throw err
            }
        }
    }

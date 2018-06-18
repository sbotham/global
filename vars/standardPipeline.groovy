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
					sendNotifications("Monkey is done", "")
				}
				
				stage ('Init') {
					log("Init", "Starts")

					sh "env"
					
					script {
						message="The project ${env.JOB_NAME} was built on ${env.B_SYSTEM_NAME} project=${config.projectName}"
					}
//					echo 'project.name=${project.name}'
					echo 'message=${message}'
					
					log("Init", "Ends")

				}
            
                stage ('Clone') {
                    sh "echo 'STP stevesVar=${config.stevesVar}'"
                    sh "echo 'STP env.BUILD_ID=${env.BUILD_ID}'"
                    checkout scm
                }
                
                stage ('Build') {
                    echo "STP Running ${env.BUILD_ID} on ${env.JENKINS_URL}"
                    sh "echo 'STP building ${config.projectName} ...'"
					
					sendNotifications("Build is done", "")
					

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
		
// Functions here
 
 def sendNotifications(message, envVars) {
		emailext (
	  
			subject: "$message : Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'", to: '231saleln@gmail.com',
	  
			body: '$message : Job ${env.BUILD_URL} ${env.JOB_NAME} envVars=$envVars ${BUILD_LOG}',
			
			//{BUILD_LOG, maxLines=8000, escapeHtml=true}
	  
			recipientProviders: [[$class: 'DevelopersRecipientProvider']]
		)
		
		

		  
		 		  
		  
}



def log(func, message) {
	echo "**************************************"
	echo "********* ${func} ${message} *********"
	echo "**************************************"
}




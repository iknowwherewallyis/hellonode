/*
withCredentials([
    string(credentialsId: 'PHP_REPO', variable: 'PHP_REPO'),
    string(credentialsId: 'REPO_ADDRESS', variable: 'REPO_ADDRESS'),
    string(credentialsId: 'netsuite-token', variable: 'token')
]) {
podTemplate(label: 'docker-test', 
            //serviceAccount: 'jenkins',
            volumes: [hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock')],
        
            containers: [
            containerTemplate(name: 'jnlp', alwaysPullImage: true, image: 'ccthub/jkslave')
            ])

{
node ('docker-test'){

  def secrets = [
      [$class: 'VaultSecret', path: 'secret/hello', secretValues: [
          [$class: 'VaultSecretValue', envVar: 'token', vaultKey: 'netsuite-token']]]
  ]
  def configuration = [$class: 'VaultConfiguration',
                       vaultUrl: 'http://vault.cct.marketing',
                       vaultCredentialId: 'jenkins-cred-id']

       def tokenToUse
wrap([$class: 'VaultBuildWrapper', configuration: configuration, vaultSecrets: secrets]) {

     tokenToUse="${token}"
}
  
  

   docker.withRegistry("${REPO_ADDRESS}", "DOCKERHUB_CREDS"){
   //withKubeConfig([credentialsId: '5b690a2e-c11b-4fa9-941d-08163a13c02c',
   //         serverUrl: 'https://192.168.99.117:8443',
   //         contextName: 'minikube',
   //         clusterName: 'minikube',

	   withKubeConfig([credentialsId: 'netsuite-token',
	   serverUrl: 'https://api.cct.marketing',
	    //contextName: 'netsuite-consumer',
	    //clusterName: 'cct.marketing',

		  ]){
		   

    def app
   

           stage('Clone repository') {

                    checkout scm
                    }
                
            stage ('Build Application image') {
                container('jnlp') {
		    def commit_id = sh(returnStdout: true, script: 'git rev-parse HEAD').trim().take(7)
		    echo "BUILDING IMAGE"
		    //app = docker.build("${PHP_REPO}", "--build-arg some_variable_name=a_value -f Dockerfile.php .")
		    app = docker.build("${PHP_REPO}", "-f ./app/Dockerfile ./")
                    //docker.withRegistry('https://167611661240.dkr.ecr.eu-central-1.amazonaws.com', 'ecr:eu-central-1:581d148d-74b8-42c3-9d28-848c7f174a4f'){ 
		    echo "TAGGING IMAGE"
    		    app.push("$commit_id")
                    }
                }
            }
 	     stage ('Publishing new php image') {
                container('jnlp') {
                    def branch = sh(returnStdout: true, script: 'git name-rev --name-only HEAD|cut -f3 -d/').trim()
                    def commit_id = sh(returnStdout: true, script: 'git rev-parse HEAD').trim().take(7)
			//echo "$commit_id"
                    if(branch == 'master') {
			//sh "kubectl -n default set image cronjob.batch/test hello=${REPO_ADDRESS}/${PHP_REPO}:${commit_id}"
			sh "kubectl -n default set image cronjob.batch/test hello=${REPO_ADDRESS}/${PHP_REPO}:${commit_id}"
			    sh "kubectl -n default get deploy hello-kubernetes -o jsonpath={.spec}"    
                    }
                    if(branch != 'master') {
                        sh "echo 'Unsupported branch.'"
                        sh "exit 1"
                    }
                }
            }
	     stage ('Publish tagged php image') {
                container('jnlp') {
                    def branch = sh(returnStdout: true, script: 'git name-rev --name-only HEAD|cut -f3 -d/').trim()
                    def commit_id = sh(returnStdout: true, script: 'git rev-parse HEAD').trim().take(7)
                    if(branch == 'master') {
                        echo "Pushing image to remote registry with TAG 'production'..."
			app = docker.build("${PHP_REPO}", "-f Dockerfile.php .")
                        app.push("production")
                    }
                }
            }
        }
    }
}
}
*/
withCredentials([
 //   string(credentialsId: 'PHP_REPO', variable: 'PHP_REPO'),
 //   string(credentialsId: 'REPO_ADDRESS', variable: 'REPO_ADDRESS'),
 //   string(credentialsId: 'netsuite-token', variable: 'token')
]) {
podTemplate(label: 'docker-test', 
            //serviceAccount: 'jenkins',
            volumes: [hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock')],
        
            containers: [
            containerTemplate(name: 'jnlp', alwaysPullImage: true, image: 'ccthub/jkslave')
            ])

{
def tokenToUse  
node ('docker-test'){
	
  
  def secrets = [
      [$class: 'VaultSecret', path: 'secret/netsuite', secretValues: [
          [$class: 'VaultSecretValue', envVar: 'token', vaultKey: 'netsuite-token']]]
  ]
  def configuration = [$class: 'VaultConfiguration',
                       vaultUrl: 'http://vault.cct.marketing',
                       vaultCredentialId: 'jenkins-cred-id']
    wrap([$class: 'VaultBuildWrapper', configuration: configuration, vaultSecrets: secrets]) {
    tokenToUse = "${token}"
    }

  
	
    stage('Clone repository') {
        container('jnlp'){
        checkout scm
	def method
	method = load("./changeSecret.groovy")
		method.changeSecretText('netsuite-token', "${tokenToUse}")
    }
    }
    stage('Run script') {
        container('jnlp'){
		   withKubeConfig([credentialsId: 'user-token',
                    serverUrl: 'https://192.168.99.119:8443',
                   ]) {
      sh 'kubectl get po --all-namespaces'

    }
    }
    }
    }
}
}


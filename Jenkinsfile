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

/*
import jenkins.model.*
import com.cloudbees.plugins.credentials.impl.*
import com.cloudbees.plugins.*
import org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl;
import org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl.DescriptorImpl;
import hudson.util.Secret;


def changePassword = { id, new_secret ->
def creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
  com.cloudbees.plugins.credentials.Credentials.class,
  Jenkins.instance,
  null,
  null
);
def secret = Secret.fromString(new_secret)
def c = creds.find {it.id == id}
if (!c) {
  return "Unable to pickup credential from Jenkins"
}

if ( c ) {
  println "found credential ${c.id}"

  def credentials_store = Jenkins.instance.getExtensionList(
    'com.cloudbees.plugins.credentials.SystemCredentialsProvider'
  )[0].getStore()


  def result = credentials_store.updateCredentials(
    com.cloudbees.plugins.credentials.domains.Domain.global(),
    c,
    new StringCredentialsImpl(c.scope, c.id, c.description, secret)
  )

  if (result) {
    println "password changed for ${c.id}"
  } else {
    println "failed to change password for ${c.id}"
  }
} else {
  println "could not find credential for ${c.id}"
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
  def secrets = [
      [$class: 'VaultSecret', path: 'secret/hello', secretValues: [
          [$class: 'VaultSecretValue', envVar: 'token', vaultKey: 'user-token']]]
  ]
  def configuration = [$class: 'VaultConfiguration',
                       vaultUrl: 'http://vault.cct.marketing',
                       vaultCredentialId: 'jenkins-cred-id']

       def tokenToUse

  
  
    def method

node ('docker-test'){	    
    stage('Clone repository') {
        container('jnlp'){
        checkout scm
	wrap([$class: 'VaultBuildWrapper', configuration: configuration, vaultSecrets: secrets]) {
	tokenToUse = "${token}"
	}
	method = load("changeSecret.groovy")
	//changePassword('user-token', "${tokenToUse}")
	method.changePassword('user-token', "${tokenToUse}")
	
    }
    }
    stage('Run script') {
        container('jnlp'){
		   withKubeConfig([credentialsId: 'user-token',
                    serverUrl: 'https://192.168.99.119:8443',
                   ]) {
	//method = load("changeSecret.groovy")
	//externalMethod.changePassword('user-token', "${tokenToUse}")
	//method.changePassword('user-token', "password")
	//def externalMethod = load("changeSecret.groovy")
	//externalMethod.changeSecret('user-token', "${user_token}")
        //app = docker.build("getintodevops/hellonode")
      sh 'kubectl get po --all-namespaces'
      //sh 'kubectl config current-context'
      //sh 'kubectl cluster-info'
      //sh 'kubectl get deployment jenkins-leader --namespace=jenkins'
    }
    }
    }
    }
}
}


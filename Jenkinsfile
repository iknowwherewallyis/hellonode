podTemplate(label: 'docker-test', 
            volumes: [hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock')],        
            containers: [
            containerTemplate(name: 'jnlp', alwaysPullImage: true, image: 'ccthub/jkslave:1.1')
            ])

{
def tokenToUse  
	node ('docker-test'){
	    stage('Clone repository') {
		    container('jnlp'){
			def jobBaseName = "${env.JOB_NAME}".split('/').first()
			container('jnlp'){
			checkout scm
			def method
			sh "ls"
			method = load("./updateVaultTokens.groovy")
				method.updateVaultTokens('vault-token', 'new-token', jobBaseName)
			}
    		}
   	}
}
}


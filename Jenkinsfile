/*podTemplate(label: 'docker-test',
        containers: [
            containerTemplate(name: 'jnlp', alwaysPullImage: true, image: 'ccthub/jkslave')
        ]){
    node ('docker-test'){
    def app

    stage('Clone repository') {
            //container('jnlp'){ 
        checkout scm
        app = docker.build("getintodevops/hellonode")

    //}
    }

    stage('Build image') {
            container('jnlp'){
        sh "hostname"
        sh "pwd"
        sh "ls"
        sh "whoami"
        //sh "kubectl get svc -n kong"
        //sh "kubectl get all --all-namespaces"
        app = docker.build("getintodevops/hellonode")
            }
    }
    }
}

*/

/*

node {
    def app

    stage('Clone repository') {

        checkout scm
    }

    stage('Build image') {

        app = docker.build("getintodevops/hellonode")
    }
}

*/

node {
  stage('List pods') {
          withKubeConfig(){
      sh 'kubectl get pods'
          }
    }
}


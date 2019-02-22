import org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl;
import hudson.util.Secret;


def changeSecretText(id, new_secret){
  
def creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
  com.cloudbees.plugins.credentials.Credentials.class,
  Jenkins.instance,
  null,
  'global'
);
  
def secret = Secret.fromString(new_secret)
def c = creds.find {it.id == id}

println "${c.scope}"

if (!c) {
  println "could not find credential for ${id} in Jenkins credential store"
  return "Unable to pickup credential from Jenkins"
}

if ( c ) {
  println "found credential ${c.id}"

  def credentials_store = Jenkins.instance.getExtensionList(
    'com.cloudbees.plugins.credentials.SystemCredentialsProvider'
  )[0].getStore()
  
  println "${credentials_store}"

  def result = credentials_store.updateCredentials(
    com.cloudbees.plugins.credentials.domains.Domain.getDomainByName('docker-pipeline-test'),
    //com.cloudbees.plugins.credentials.domains.Domain.job(),
    c,
    new StringCredentialsImpl(c.scope, c.id, c.description, secret)
  )

  if (result) {
    println "secret text changed for ${c.id} updated with credentials stored in Vault"
  } else {
    println "failed to change secret for ${c.id}"
  }
} else {
  println "could not find credential for ${c.id} in Jenkins credential store"
}
}
return this;



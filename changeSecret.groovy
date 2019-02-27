import hudson.util.Secret;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.plaincredentials.StringCredentials;
import org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl;
import com.cloudbees.plugins.credentials.CredentialsProvider;

def changeSecretText(id, new_secret){
  
  def hi = Hudson.instance
  def job = hi.getJob('docker-test')

  cred = CredentialsProvider.lookupCredentials(StringCredentials.class, job, null, null);

  def c = cred.find {it.id == id}

if (!c) {
  println "could not find credential for ${id} in Jenkins credential store"
  return "Unable to pickup credential from Jenkins"
}

if ( c ) {
  println "found credential ${c.id}"


  def credentials_store = Jenkins.instance.getExtensionList(
  'com.cloudbees.plugins.credentials.CredentialsProvider'
  )[0].getStore(job)


  println "${credentials_store}"

  
  def secret = Secret.fromString(new_secret)
  def result = credentials_store.updateCredentials(
    com.cloudbees.plugins.credentials.domains.Domain.global(), 
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



import hudson.util.Secret;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.plaincredentials.StringCredentials;
import org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.datapipe.jenkins.vault.credentials.VaultTokenCredential;

def updateVaultTokens(id, new_secret, job_name){

  def job = Jenkins.instance.getJob(job_name)

  def cred = CredentialsProvider.lookupCredentials(
      VaultTokenCredential.class,
      job,
      null,
      null);

  def c = cred.find {it.id == id}
  
  println c;

  if (!c) {
    println "could not find credential for ${id} in Jenkins credential store"
    return "Unable to pickup credential from Jenkins"
  }
  
 
  if ( c ) {
    println "found credential ${c.id}"

    def credentials_store = Jenkins.instance.getExtensionList(
      'com.cloudbees.hudson.plugins.folder.properties.FolderCredentialsProvider'
      )[0].getStore(job)

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


import org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl;
import hudson.util.Secret;


def changePassword(id, new_secret){ //->
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

return this;



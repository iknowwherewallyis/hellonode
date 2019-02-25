import org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl;
import hudson.util.Secret;
import com.cloudbees.plugins.credentials.domains.*

import com.cloudbees.plugins.credentials.CredentialsMatchers;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.CredentialsScope;
import com.cloudbees.plugins.credentials.CredentialsStore;
import com.cloudbees.plugins.credentials.common.AbstractIdCredentialsListBoxModel;
import com.cloudbees.plugins.credentials.common.StandardCredentials;
import com.cloudbees.plugins.credentials.common.StandardListBoxModel;
import com.cloudbees.plugins.credentials.domains.Domain;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import com.cloudbees.plugins.credentials.domains.DomainSpecification;
import com.cloudbees.plugins.credentials.domains.HostnameSpecification;
import com.cloudbees.plugins.credentials.domains.SchemeSpecification;
import com.cloudbees.plugins.credentials.domains.URIRequirementBuilder;
import com.cloudbees.plugins.credentials.impl.BaseStandardCredentials;
import hudson.Util;
import hudson.model.AbstractProject;
import hudson.model.Item;
import hudson.model.Queue.Task;
import hudson.model.queue.Tasks;
import hudson.security.ACL;
import hudson.util.ListBoxModel;
import hudson.util.Secret;
import jenkins.model.Jenkins;

import org.acegisecurity.Authentication;
import org.jenkinsci.plugins.plaincredentials.StringCredentials;
import org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl;

import javax.annotation.CheckForNull;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
  

def changeSecretText(id, new_secret){

def creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
        com.cloudbees.plugins.credentials.Credentials.class,
        Jenkins.instance,
        ACL.SYSTEM, 
        Collections.<DomainRequirement>emptyList()
);
for (c in creds) {
    println(c.id + ": " + c.description)
}  
  
  
/*
//def creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
  //com.cloudbees.plugins.credentials.Credentials.class,
  def creds = CredentialsProvider.lookupCredentials(
  StringCredentials.class,
  Jenkins.instance,
  null,
  null
  );
  
def secret = Secret.fromString(new_secret)
def c = creds.find {it.id == id}

//println "${c.scope}"

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
    com.cloudbees.plugins.credentials.domains.Domain.global,
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
*/
}
return this;



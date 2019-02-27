import hudson.util.Secret;
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.plugins.credentials.*
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
import com.cloudbees.plugins.credentials.impl.BaseStandardCredentials;
import jenkins.model.Jenkins;

import org.acegisecurity.Authentication;
import org.jenkinsci.plugins.plaincredentials.StringCredentials;
import org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import com.cloudbees.plugins.credentials.common.IdCredentials;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;

import hudson.model.Cause;
import hudson.model.Computer;
import hudson.model.ComputerSet;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.DescriptorVisibilityFilter;
import hudson.model.Fingerprint;
import hudson.model.Item;
import hudson.model.ItemGroup;
import hudson.model.Job;
import hudson.model.ModelObject;
import hudson.model.Node;
import hudson.model.ParameterValue;
import hudson.model.ParametersAction;
import hudson.model.Queue;
import hudson.model.Run;
import hudson.model.User;
import hudson.model.queue.Tasks;


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



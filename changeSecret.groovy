import org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl;
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

import com.cloudbees.plugins.credentials.common.IdCredentials;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import com.cloudbees.plugins.credentials.fingerprints.ItemCredentialsFingerprintFacet;
import com.cloudbees.plugins.credentials.fingerprints.NodeCredentialsFingerprintFacet;
import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import hudson.BulkChange;
import hudson.DescriptorExtensionList;
import hudson.ExtensionList;
import hudson.ExtensionPoint;
import hudson.Util;
import hudson.init.InitMilestone;
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
import hudson.security.ACL;
import hudson.security.Permission;
import hudson.security.PermissionGroup;
import hudson.security.PermissionScope;
import hudson.security.SecurityRealm;
import hudson.util.ListBoxModel;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Collator;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import jenkins.model.FingerprintFacet;
import jenkins.model.Jenkins;
import jenkins.util.Timer;
import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.commons.lang.StringUtils;
import org.jenkins.ui.icon.IconSpec;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.DoNotUse;
import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.StaplerRequest;
  

def changeSecretText(id, new_secret){

  def showRow = { credentialType, secretId, username = null, password = null, description = null ->
  println("${credentialType} : ".padLeft(20) + secretId?.padRight(38)+" | " +username?.padRight(20)+" | " +password?.padRight(40) + " | " +description)
}

// set Credentials domain name (null means is it global)
domainName = null

credentialsStore = Jenkins.instance.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0]?.getStore()
domain = new Domain(domainName, null, Collections.<DomainSpecification>emptyList())

credentialsStore?.getCredentials(domain).each{
  if(it instanceof UsernamePasswordCredentialsImpl)
    showRow("user/password", it.id, it.username, it.password?.getPlainText(),it.description)
  else if(it instanceof BasicSSHUserPrivateKey)
    showRow("ssh priv key", it.id, it.passphrase?.getPlainText(), it.privateKeySource?.getPrivateKey(), it.description )
  else if(it instanceof AWSCredentialsImpl)
    showRow("aws", it.id, it.accessKey, it.secretKey?.getPlainText(),it.description )
  else if(it instanceof StringCredentials)
    showRow("secret text", it.id, it.secret?.getPlainText(), it.description, '' )
  else
    showRow("something else", it.id)
}
  /*
  def hi = Hudson.instance
  def job = hi.getJob('docker-test')  
  
  for (CredentialsStore credentialsStore : CredentialsProvider.lookupStores(job) {
    if (credentialsStore instanceof SystemCredentialsProvider.StoreImpl) {
        List<Domain> domains = credentialsStore.getDomains();
      for (d in domains){
        println d
      }
    }
  }
  */
  /*

    
  def creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
  StringCredentials.class,
  //job,
  jenkins.model.Jenkins.instance,
  null,
  null
  );
  for (c in creds){
    println(c.id)
  }

//def c = creds.find {it.id == id}

if (!c) {
  println "could not find credential for ${id} in Jenkins credential store"
  return "Unable to pickup credential from Jenkins"
}

if ( c ) {
  println "found credential ${c.id}"

  def credentials_store = Jenkins.instance.getExtensionList(
  'com.cloudbees.plugins.credentials.CredentialsProvider'
  )[0].getStore()

  //def secret = Secret.fromString(new_secret)
  
  // def credentials_domain = Jenkins.instance.getExtensionList(
  //'com.cloudbees.plugins.credentials.SystemCredentialsProvider'
  //)[0].getStore().getDomains()
    
  def result = credentials_store.updateCredentials(
    //com.cloudbees.plugins.credentials.domains.Domain.global(),
    credentials_domain,
    c,
    new StringCredentialsImpl(c.id, c.description, secret)
  )

  if (result) {
    println "secret text changed for ${c.id} updated with credentials stored in Vault"
  } else {
    println "failed to change secret for ${c.id}"
  }
} else {
  println "could not find credential for ${c.id} in Jenkins credential store"
}
*/
}

return this;



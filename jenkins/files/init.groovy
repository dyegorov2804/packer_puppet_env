#!groovy

import javaposse.jobdsl.dsl.DslScriptLoader
import javaposse.jobdsl.plugin.JenkinsJobManagement
import jenkins.model.Jenkins
import com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey
import com.cloudbees.plugins.credentials.CredentialsScope
import hudson.security.FullControlOnceLoggedInAuthorizationStrategy
import hudson.security.HudsonPrivateSecurityRealm

import com.cloudbees.plugins.credentials.impl.*;
import com.cloudbees.plugins.credentials.*;
import com.cloudbees.plugins.credentials.domains.*;

import com.cloudbees.jenkins.plugins.awscredentials.AWSCredentialsImpl;
import com.cloudbees.jenkins.plugins.awscredentials.AmazonWebServicesCredentials;

Credentials c = (Credentials) new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL, "example_credentials", "Example Credentials", "AAAA", "BBBB")
SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), c)

// AWS Credentials
AmazonWebServicesCredentials awsCredentials = new AWSCredentialsImpl(CredentialsScope.GLOBAL,
				"aws_credentials", "${System.getenv('AWS_ACCESS_KEY')}", "${System.getenv('AWS_SECRET_KEY')}", "AWS Credentials");

SystemCredentialsProvider.getInstance().getCredentials().add(awsCredentials);
SystemCredentialsProvider.getInstance().save();

// Create the configuration job interface from a jobDSL script
def jobDslScript = new File('/var/jenkins_home/init-dsl/Seeder.groovy')
def workspace = new File('.')
def jobManagement = new JenkinsJobManagement(System.out, [:], workspace)
new DslScriptLoader(jobManagement).runScript(jobDslScript.text)

// Disable Setup Wizards
//if(Jenkins.instance.getSecurityRealm().getClass().getSimpleName() == 'None') {
    def instance = Jenkins.getInstance()
    def hudsonRealm = new HudsonPrivateSecurityRealm(false)
    instance.setSecurityRealm(hudsonRealm)
    // oops
    def user = instance.getSecurityRealm().createAccount("admin", "admin")
    user.save()

    def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
    strategy.setAllowAnonymousRead(false)
    instance.setAuthorizationStrategy(strategy)
    instance.save()

    println("SetupWizard Disabled")
//}
ARG jenkins_version
FROM jenkins/jenkins:${jenkins_version}
ARG packer_version

USER root

RUN apt update && apt -yy install lsb-release software-properties-common apt-transport-https ca-certificates curl gnupg2 sudo qemu qemu-system \
    qemu-utils lsof net-tools less nano
RUN apt-key fingerprint 0EBFCD88

RUN curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add -

RUN add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/debian $(lsb_release -cs) stable" && \
   apt update && apt -yy install docker-ce docker-ce-cli containerd.io docker-compose

# get packer
RUN wget -O /tmp/packer.zip https://github.com/hashicorp/packer/releases/download/nightly/packer_linux_amd64.zip && cd /usr/local/bin && unzip /tmp/packer.zip && mv pkg/packer_linux_amd64 packer && rm -f /tmp/packer.zip

RUN packer --version

COPY files/plugins.txt /usr/share/jenkins/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/plugins.txt


COPY files/init.groovy  /var/jenkins_home/init.groovy.d/
COPY files/Seeder.groovy /var/jenkins_home/init-dsl/

CMD groupdel kvm && groupdel docker && \
    DOCKER_GID=$(stat -c '%g' /var/run/docker.sock) && \
    groupadd -for -g ${DOCKER_GID} docker && \
    usermod -aG docker jenkins && \
    export PATH=/usr/local/openjdk-8/bin/java:$PATH && \
    ln -sf /usr/local/openjdk-8/bin/java /usr/local/bin/java && \
    chown -R jenkins:jenkins /var/jenkins_home/ && \
    export JAVA_OPTS='-Dpermissive-script-security.enabled=true' && \
    sudo -E -H -u jenkins bash -c "/usr/local/bin/jenkins.sh"
#!/usr/bin/env bash
workdir=/opt/screw
# 更新国内源
mv CentOS-8-reg.repo /etc/yum.repos.d/CentOS-Base.repo \
  && rm -rf /etc/yum.repos.d/CentOS-Linux-AppStream.repo \
  && rm -rf /etc/yum.repos.d/CentOS-Linux-BaseOS.repo \
  && yum clean all \
  && yum makecache \
  && yum -y install wget
mkdir -p $workdir && cd $workdir
# 下载jdk17
yum install -y java-17-openjdk
# 下载node12.22.0
wget https://npm.taobao.org/mirrors/node/v12.22.0/node-v12.22.0-linux-x64.tar.xz
tar -xvf node-v12.22.0-linux-x64.tar.xz
ln -sf ${workdir}/node-v12.22.0-linux-x64/bin/node /usr/local/bin/node
ln -sf ${workdir}/node-v12.22.0-linux-x64/bin/npm /usr/local/bin/npm
# 下载maven3.9.1
wget --no-check-certificate https://dlcdn.apache.org/maven/maven-3/3.9.1/binaries/apache-maven-3.9.1-bin.tar.gz
tar -xzvf apache-maven-3.9.1-bin.tar.gz
cat >> /etc/profile << EOF
MAVEN_HOME=${workdir}/apache-maven-3.9.1
PATH=$PATH:\$MAVEN_HOME/bin
export PATH
EOF

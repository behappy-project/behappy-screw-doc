#!/usr/bin/env bash
workdir=/opt/screw
# 更新国内源
yum -y install wget \
  && wget http://mirrors.aliyun.com/repo/Centos-7.repo \
  && mv Centos-7.repo CentOS-Base.repo \
  && yum clean all \
  && yum makecache
mkdir -p $workdir && cd $workdir
# 下载jdk11
yum install -y java-11-openjdk
# 下载node12.22.0
wget https://npm.taobao.org/mirrors/node/v12.22.0/node-v12.22.0-linux-x64.tar.xz
tar -xvf node-v12.22.0-linux-x64.tar.xz
ln -sf ${workdir}/node-v12.22.0-linux-x64/bin/node /usr/local/bin/node
ln -sf ${workdir}/node-v12.22.0-linux-x64/bin/npm /usr/local/bin/npm
# 下载maven3.8.6
wget --no-check-certificate https://dlcdn.apache.org/maven/maven-3/3.8.6/binaries/apache-maven-3.8.6-bin.tar.gz
tar -xzvf apache-maven-3.8.6-bin.tar.gz
cat >> /etc/profile << EOF
MAVEN_HOME=${workdir}/apache-maven-3.8.6
PATH=$PATH:\$MAVEN_HOME/bin
export PATH
EOF
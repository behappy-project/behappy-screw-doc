#!/usr/bin/env bash
# 仓库地址
REGISTRY_ADDRESS="docker.io"
# 仓库账户名
REGISTRY_USERNAME=wangxiaowu950330
# 仓库密码
REGISTRY_PASSWORD=
IMAGE=${REGISTRY_USERNAME}/behappy-screw-doc:latest
# 登陆
echo "$REGISTRY_PASSWORD" | sudo docker login -u "${REGISTRY_USERNAME}" --password-stdin "${REGISTRY_ADDRESS}"
# 构建镜像
sudo docker build --build-arg BUILDKIT_INLINE_CACHE=1 -t "${REGISTRY_ADDRESS}"/"${IMAGE}" .
echo "构建镜像成功"
# push镜像
sudo docker push "${REGISTRY_ADDRESS}"/"${IMAGE}"
echo "push镜像成功"

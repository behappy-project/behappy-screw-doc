#!/usr/bin/env bash
# 配置环境变量: $REGISTRY_PASSWORD,$REGISTRY_USERNAME,$REGISTRY_ADDRESS
IMAGE=${REGISTRY_USERNAME}/behappy-screw-doc:latest
# 登陆
echo "$REGISTRY_PASSWORD" | sudo docker login -u "${REGISTRY_USERNAME}" --password-stdin "${REGISTRY_ADDRESS}"
# 构建镜像
sudo docker build --build-arg BUILDKIT_INLINE_CACHE=1 -t "${REGISTRY_ADDRESS}"/"${IMAGE}" .
echo "构建镜像成功"
# push镜像
sudo docker push "${REGISTRY_ADDRESS}"/"${IMAGE}"
echo "push镜像成功"

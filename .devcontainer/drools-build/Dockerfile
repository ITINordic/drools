FROM mcr.microsoft.com/devcontainers/base:ubuntu
SHELL ["/bin/bash", "-c"]
RUN sudo apt-get -y update && \
sudo apt-get -y install unzip zip curl git && \
curl -s "https://get.sdkman.io" | bash && \
source "/$HOME/.sdkman/bin/sdkman-init.sh" && \
sdk install java 17.0.8-oracle && \
sdk use java 17.0.8-oracle && \
sdk install maven 3.9.4 && \
sdk use maven 3.9.4

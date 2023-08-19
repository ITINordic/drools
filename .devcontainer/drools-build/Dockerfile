FROM mcr.microsoft.com/devcontainers/base:ubuntu
SHELL ["/bin/bash", "-c"]
RUN sudo apt-get -y update && \
sudo apt-get -y install unzip zip curl git mkcert && \
curl -s "https://get.sdkman.io" | bash && \
source "/$HOME/.sdkman/bin/sdkman-init.sh" && \
sdk install java 17.0.8-oracle && \
sdk use java 17.0.8-oracle && \
sdk install maven 3.9.4 && \
sdk use maven 3.9.4
RUN cp -r /root/.sdkman /home/vscode/ && chown vscode: -R /home/vscode/.sdkman && \
cp -r /root/.bashrc /home/vscode/ && chown vscode: -R /home/vscode/.bashrc && \
cp -r /root/.zshrc /home/vscode/ && chown vscode: -R /home/vscode/.zshrc
COPY ./rootCA.pem /home/vscode/.local/share/mkcert/rootCA.pem
COPY ./post-install-setup-maven.sh /usr/local/bin/post-install-setup-maven.sh
USER vscode
RUN mkcert -install
USER root
RUN chmod 755 /usr/local/bin/post-install-setup-maven.sh

read -s -p "Enter master password: " MASTER_PASSWORD
echo
read -s -p "Enter repo user: " REPO_USER
echo
read -s -p "Enter repo password: " REPO_PASSWORD
echo

# MASTER_PASSWORD=test
# REPO_USER=test
# REPO_PASSWORD=test

ENCRYPT_MASTER_PASSWORD="$(mvn --encrypt-master-password "$MASTER_PASSWORD")"

SEC_FILE="$HOME/.m2/settings-security.xml"

SEC_FILE="$HOME/.m2/settings-security.xml"
tee <<EOF > $SEC_FILE
<settingsSecurity>
<master>${ENCRYPT_MASTER_PASSWORD}</master>
</settingsSecurity>
EOF

ENCRYPT_REPO_PASSWORD="$(mvn --encrypt-password "$REPO_PASSWORD")"

function add_server_auth {
  
SETTINGS_FILE="$HOME/.m2/settings.xml"
if [ -f "$SETTINGS_FILE" ]; then
    awk -v name="$REPO_USER" -v password="$ENCRYPT_REPO_PASSWORD" 'BEGIN {notdone=1; printserver=1}
    /<servers>/{printserver=0};
    /<id>internal.repo<\/id>/{notdone=0};
    notdone && (/<\/servers>/ || /<\/settings>/) {
        notdone=0;
        if(printserver)print("<servers>");
        print("    <server>");
        print("      <id>internal.repo</id>");
        print("      <username>" name "</username>")
        print("      <password>" password "</password>");
        print("    </server>");
        print("    <server>");
        print("      <id>internal.central</id>");
        print("      <username>" name "</username>")
        print("      <password>" password "</password>");
        print("    </server>");
        if(printserver)print("</servers>");
        };
        {print($0)}' "$SETTINGS_FILE" > "${SETTINGS_FILE}.tmp"
        mv "${SETTINGS_FILE}.tmp" "$SETTINGS_FILE"

else
tee <<EOF > $SETTINGS_FILE
<settings>
  <servers>
    <server>
      <id>internal.repo</id>
      <username>$REPO_USER</username>
      <password>$ENCRYPT_REPO_PASSWORD</password>
    </server>
    <server>
      <id>internal.central</id>
      <username>$REPO_USER</username>
      <password>$ENCRYPT_REPO_PASSWORD</password>
    </server>
  </servers>
</settings>
EOF
fi


#!/bin/bash

screen -S rss-rest-reader -dm bash -c "
 java \
   -Dapp.security.api-key=changeme \
   -Dapp.security.rate-limit-action=100 \
   -Dapp.security.rate-limit-wrong-api-key=1 \
   -Dserver.address=0.0.0.0 \
   -Dserver.port=9009 \
   -jar rss-rest-reader-1.0.0-SNAPSHOT.jar
"

echo "The application is now running in the screen named `rss-rest-reader`"
echo "Use `screen -x rss-rest-reader` to access the terminal."

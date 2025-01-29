![Java](https://img.shields.io/badge/Java-17%2B-216B00?style=for-the-badge)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4+-216B00?style=for-the-badge)
![License](https://img.shields.io/github/license/atom7xyz/rss-rest-reader?style=for-the-badge&color=7469B6)
![GitHub Downloads (all assets, all releases)](https://img.shields.io/github/downloads/atom7xyz/rss-rest-reader/total?style=for-the-badge&color=155E95)

# rss-rest-reader

Fetch data from RSS URLs and retrieve results over HTTP using the REST endpoint.

## Features
- 🚀 Spring Boot server implementation
- 🔒 Secure API key authentication
- 🛡️ Built-in rate limiting protection
- 📡 RSS feed aggregation
- 🐳 Easy deployment with Docker
- 🔧 Configurable via environment variables

---

### ⚠️ Security Note

It's recommended to run the application behind a reverse proxy with HTTPS enabled (like nginx) for secure communication.

By default, port `9009` is configured to accept connections from the internet.

---

### Simple deploy using Docker Compose

1. **Ensure that you have Docker and Docker Compose installed on your system.**
2. **Download the docker-compose and the application file:**

   ```shell
   wget https://raw.githubusercontent.com/atom7xyz/rss-rest-reader/refs/heads/master/docker-compose.yml && \
   wget https://github.com/atom7xyz/rss-rest-reader/releases/download/1.0.0/rss-rest-reader-1.0.0-SNAPSHOT.jar
   ```

3. **Move the application JAR file to a convenient directory:**

   ```shell
   mkdir -p ~/tools/rss-rest-reader && \
   mv ./rss-rest-reader-1.0.0-SNAPSHOT.jar ~/tools/rss-rest-reader/ && \
   mv ./docker-compose.yml ~/tools/rss-rest-reader/ && \
   cd ~/tools/rss-rest-reader/
   ```

   > **Note:** You **can** have a different directory from `~/tools/rss-rest-reader/`.

4. **Start your instance in the background:**

   ```shell
   docker compose up rss-rest-reader -d
   ```

   > **Note:** To refresh the configuration append `--force-recreate` to this command.

5. **Configuration:**

   From the provided docker-compose.yml file, edit it using an editor like `vim` or `nano`:
   ```yml
   environment:
      - APP_SECURITY_API_KEY=changeme
      - APP_RATE_LIMIT_ACTION=50
      - APP_RATE_LIMIT_WRONG_API_KEY=1
      - SERVER_PORT=9009
   ports:
      - "9009:9009"
   ```
   To no longer expose the application port to the internet, set `ports` like this:
   ```yml
    ports:
      - "127.0.0.1:9009:9009"
   ```
   To allow connections from/to the nginx container, uncomment the networks configuration:
   ```yml
      ...
      user: "1000:1000"
      networks:
        - shared-nginx

   networks:
     shared-nginx:
       external: true
   ```

----

### Installing from Source

To run your `rss-rest-reader` instance from source, follow these steps:

1. **Clone the repository:**

   ```shell
   git clone https://github.com/atom7xyz/rss-rest-reader && \
   cd rss-rest-reader
   ```

2. **Retrieve dependencies and compile the project:**

    ```shell
    chmod +x ./mvnw && \
    ./mvnw package -Pjava.version=17
    ```

    > **Note:** Supports Java versions 17-23. Default is Java 21 if not specified.

3. **Move the compiled JAR file to a convenient directory:**

   ```shell
   mkdir -p ~/tools/rss-rest-reader && \
   mv ./target/rss-rest-reader-1.0.0-SNAPSHOT.jar ~/tools/rss-rest-reader/ && \
   mv ./start.sh ~/tools/rss-rest-reader/ && \
   cd ~/tools/rss-rest-reader/
   ```

4. **Run the JAR file:**

   ```shell
   java -jar rss-rest-reader-1.0.0-SNAPSHOT.jar
   ```
   or
   ```shell
   chmod +x ./start.sh && \
   ./start.sh
   ```

5. **Configuration:**

   From the provided start.sh file, edit it using an editor like `vim` or `nano`:
   ```shell
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
   ```
   To no longer expose the application port to the internet, set `server.address` like this:
   ```shell
    -Dserver.address=127.0.0.1
   ```

6. **(Optional) Automatically start the instance at server startup.**

   Using the `start.sh` file:
   ```shell
   (crontab -l 2>/dev/null; echo "@reboot ~/tools/rss-rest-reader/start.sh") | crontab -
   ```

---

## License
This project is licensed under the GNU General Public License v3.0 - see the [LICENSE](LICENSE) file for details.

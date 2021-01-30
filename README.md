# tunestumbler-wrapper-for-reddit
Built with Spring Boot, the Tunestumbler API creates lists of Reddit search results based on a user's Subreddits, Multireddits, and filter preferences.

This project uses a MySQL database. 

For an example of an application that uses the Tunestumbler API, see [Tunestumbler](https://github.com/CrispiestHashbrown/tunestumbler-for-reddit).

## Getting started (If you want to run this on your own machine)
Clone the application: 
```
git clone https://github.com/CrispiestHashbrown/tunestumbler-wrapper-for-reddit.git
```
In `src/main/resources`, modify `application.properties` to contain the following fields:
```
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=
server.port=
tokenSecret=
server.servlet.context-path=
basicAuth=
```
To run unit tests and build the project, follow [these steps](https://maven.apache.org/install.html) to install maven, and then run:
```
mvn install
```
For testing, I use the [Spring Tools Suite](https://spring.io/tools).

## Usage
- This API makes HTTP requests to the Reddit API. For more information on the Reddit API, visit the [Reddit API Wiki](https://github.com/reddit-archive/reddit/wiki/API) and the [live documentation](https://www.reddit.com/dev/api). 

- Reddit tokens expire every hour. To revoke Tunestumbler permissions, you can visit your Reddit `preferences > apps` and click `revoke access`.

- This has only been tested on Chrome and Firefox web browsers. 

## Contributions
- Contributions welcome!

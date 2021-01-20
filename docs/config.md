# Configuration

## Linux
If you installed rayd.io with APT, 
you will find the configuration in `/etc/raydio/main.cfg`.
The Configuration file consits of key-value pairs.
The Key is not allowed to contain spaces!
The Value can contain spaces and is seperated from the key by an `=` sign.

## Docker
You can simply set all configuration variables as environment variables.

E.g. `docker run -d -e SERVER_PORT=8081 -p 8081:8081 raydio`

## Properties

### Web Server

#### SERVER_PORT
The `SERVER_PORT` property sets the port on which the server is reachable.
Default is port 8080

#### SERVER_SSL_ENABLED
Either `true` or `false`. Defaults to `false`.
__Make sure you cofigured the Key Store correctly for this to work__
For more Information see [here](https://www.baeldung.com/spring-boot-https-self-signed-certificate)

#### SERVER_SSL_KEY_STORE
The location of the used Key Store.

#### SERVER_SSL_KEY_STORE_TYPE
The type of Key Store used. Usually either `PKCS12` or `JKS`.

#### SERVER_SSL_KEY_STORE_PASSWORD
The password of the used Key Store.

#### SERVER_SSL_KEY_ALIAS
The alias for the server key in the Key Store.

### Database

#### SPRING_DATASOURCE_URL
The `SPRING_DATASOURCE_URL` property sets the JDBC URL for the database.

__Do not change this unless you're sure what you are doing!__

If you don't want to use the default sqlite database,
you can add the URL of a MySQL or Postgresql Database here.
Make sure you also set the `SPRING_DATASOURCE_USERNAME`, 
`SPRING_DATASOURCE_PASSWORD` and `SPRING_JPA_DATABASE_PLATFORM` properties.

#### SPRING_DATASOURCE_USERNAME
The `SPRING_DATASOURCE_USERNAME` property sets the username for the used database.
This is not used by default, since sqlite does not need authentication.

__Do not change this unless you're sure what you are doing!__

#### SPRING_DATASOURCE_PASSWORD
The `SPRING_DATASOURCE_PASSWORD` property sets the password for the database user.
This is not used by default, since sqlite does not need authentication.

__Do not change this unless you're sure what you are doing!__

# Description

It is part of [mtdn2twtr](https://github.com/3DRaven/mtdn2twtr) project. For hidding client app credentials (you can get them by registering as 
developer in twitter), created separated server for getting PIN based authentification URL.

For decreasing application registration frequency on Mastodon servers used persistent cache. So instead of register
new application on Mastodon node, server try to find existent registration instead.

There is work server url where you can register mtdn2twtr instance: 
* [for twitter request](https://pin.anyqn.com/api/v1/twitter/getAuthUrl)
* [for mastodon request](https://pin.anyqn.com/api/v1/mastodon/getAuthUrl?host=mtdn.anyqn.com)

Server will execute one request per second from all clients by default.

Authentication documentation: 
* [Twitter obtaining OAuth1.0a token](https://developer.twitter.com/en/docs/authentication/oauth-1-0a/obtaining-user-access-tokens)
* [Twitter PIN based OAuth1.0a](https://developer.twitter.com/en/docs/authentication/oauth-1-0a/pin-based-oauth)
* [Twitter OAuth 1.0a authenticated request](https://developer.twitter.com/en/docs/authentication/oauth-1-0a)
* [Mastodon documentation](https://docs.joinmastodon.org/methods/oauth/)

There is mistake in Mastodon documentation about OAuth. For request:
POST https://mastodon.example/oauth/token
Really a parameter client_secret not described in documentation needed.

# Server usage

For getting twitter PIN authorization URL:
Request
```
GET https://${pingenHost}/api/v1/twitter/getAuthUrl
```
Response
```
{
  "oauthToken": "token",
  "authorizationUrl": "https://api.twitter.com/oauth/authorize?oauth_token=token"
}
```
For getting mastodon CODE (similar as PIN) authorization URL:
Request
```
GET https://${pingenHost}/api/v1/mastodon/getAuthUrl?host=mtdn.anyqn.com
```
host - it is just percent encoded full Mastodon user node url for registration (in this example it is mtdn.anyqn.com).
Response
```
{
    "clientId":"x81Hb3uxGH1ioOV3WJD599JByd3UFJneyDAOsvyn7eY",
    "authorizationUrl":"https://mtdn.anyqn.com/oauth/authorize?response_type=code&client_id=x81Hb3uxGH1ioOV3WJD599JByd3UFJneyDAOsvyn7eY&redirect_uri=urn:ietf:wg:oauth:2.0:oob&scope=read&force_login=false"
}
```
# Server properties

All properties must be saved to file with name application.properties in same path than server jar file
```
# If you need to use this server without nginx reverse proxy, but need https, uncoment this properties and add cert
# The format used for the keystore. It could be set to JKS in case it is a JKS file
#server.ssl.key-store-type=PKCS12
# The path to the keystore containing the certificate
#server.ssl.key-store=name.p12
# The password used to generate the certificate
#server.ssl.key-store-password=
# The alias mapped to the certificate
#server.ssl.key-alias=
# Since we're using a Spring Security enabled application, let's configure it to accept only HTTPS requests:
#server.ssl.enabled=true
# By default server listen on 127.0.0.1 only
# server.address=0.0.0.0
# Default server port
server.port=8080
# API key and secret from twitter developer portal for your registered application
client.key=twitterApiKey
client.secret=twitterApiSecret
```

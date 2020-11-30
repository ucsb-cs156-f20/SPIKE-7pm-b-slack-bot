# demo-spring-react-minimal

[![codecov](https://codecov.io/gh/ucsb-cs156-f20/demo-spring-react-minimal/branch/main/graph/badge.svg)](https://codecov.io/gh/ucsb-cs156-f20/demo-spring-react-minimal)

## Purpose

This app is spike of creating a slack bot built on top of the
[demo-spring-react-minimal](https://github.com/ucsb-cs156-f20/demo-spring-react-minimal) app.  It is part of a course project of <https://ucsb-cs156.github.io>, a course at [UC Santa Barbara](https://ucsb.edu).


This repo uses:
* JBot for configuration of the Slack Bot
* Spring Boot Backend
* React Front end
* Auth0 authentication using Google

## Specific Notes on Slack Bots

Resources:

* JBot documentation: <https://blog.rampatra.com/how-to-make-a-slack-bot-in-java#getting-started>

Notes:

Be sure that the route you choose for the slash commands is one that is not protected to require logins.  This is configured in `src/main/java/edu/ucsb/changeme/config/SecurityConfig.java`.  For example, this code says that anything under `/api/public/` doesn't need
authentication.

```java
http.authorizeRequests().mvcMatchers("/api/public/**").permitAll()
```

Testing with CURL:

If you have `curl` installed on your system, you can try interacting with the slack bot by simulating the message that Slack would send to your back end.  Here's an example.  You need to replace `put-your-slash-command-token-here` with the value of the Slack command
token that you get from Slack when configuring the slack commands.

```
curl -d "token=put-your-slash-command-token-here&team_id=&team_domain=&channel_id=&channel_name=&user_id=&user_name=&command=&text=&response_url=" -X POST http://localhost:8080/api/public/slash-command
```

If it works, you should see the log message on the Spring Back end console:

```
2020-11-30 13:43:50.938  INFO 58259 --- [nio-8080-exec-5] e.u.c.c.SlackSlashCommandController      : slash command processing...
```

And in the terminal where you typed the curl command, you should see something like this:

```
{"username":null,"channel":null,"text":"The is Slash Commander!","attachments":[{"fallback":null,"color":null,"pretext":null,"title":null,"text":"I will perform all tasks for you.","fields":null,"footer":null,"ts":null,"author_name":null,"author_link":null,"author_icon":null,"title_link":null,"image_url":null,"thumb_url":null,"footer_icon":null}],"icon_emoji":null,"response_type":"in_channel"}  
```

## Property file values

This section serves as a quick reference for values found in either [`secrets-localhost.properties`](./secrets-localhost.properties) and/or [`secrets-heroku.properties`](./secrets-heroku.properties).

| Property name                                                     | Heroku only? | Explanation                                                               |
| ----------------------------------------------------------------- | ------------ | ------------------------------------------------------------------------- |
| `app.namespace`                                                   |              | See `Getting Started` below                                               |
| `app.admin.emails`                                                |              | A comma separated list of email addresses of permanent admin users.       |
| `app.member.hosted-domain`                                        |              | The email suffix that identifies members (i.e. `ucsb.edu` vs `gmail.com`) |
| `auth0.domain`                                                    |              | See `Getting Started` below                                               |
| `auth0.clientId`                                                  |              | See `Getting Started` below                                               |
| `security.oauth2.resource.id`                                     |              | Should always be `${app.namespace}/api`                                   |
| `security.oauth2.resource.jwk.keySetUri`                          |              | Should always be `https://\${auth0.domain}/.well-known/jwks.json`         |
| `spring.jpa.database-platform`                                    | Yes          | Should always be `org.hibernate.dialect.PostgreSQLDialect`                |
| `spring.datasource.driver-class-name`                             | Yes          | Should always be `org.postgresql.Driver`                                  |
| `spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults` | Yes          | Should always be `false`                                                  |
| `spring.datasource.url`                                           | Yes          | Should always be `${JDBC_DATABASE_URL}`                                   |
| `spring.datasource.username`                                      | Yes          | Should always be `${JDBC_DATABASE_USERNAME}`                              |
| `spring.datasource.password`                                      | Yes          | Should always be `${JDBC_DATABASE_PASSWORD}`                              |
| `spring.jpa.hibernate.ddl-auto`                                   | Yes          | Should always be `update`                                                 |

## Getting started

The first step is to create our secrets file for running the application locally. You can do this by running the following command:

```bash
  cp secrets-localhost.properties.SAMPLE secrets-localhost.properties
```

The next thing you'll want to do is to choose a name for your application. The name should be lowercase letters, digits, and hyphens only. You'll want to then create this application on Heroku in order to verify that the name is available.

- For example, if your name is `cool-application` then your heroku application's url will end up looking like `https://cool-application.herokuapp.com`.

We will now update the following properties in `secrets-localhost.properties` with the url of our application:

| Property name              | Value                                                                                                                                                    |
| -------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `app.namespace`            | The url of your heroku application (i.e. `https://cool-application.herokuapp.com`)                                                                       |
| `app.admin.emails`         | A comma separated list of email addresses of permanent admin users. We suggest adding your email to the list (i.e. `phtcon@ucsb.edu,youremail@ucsb.edu`) |
| `app.member.hosted-domain` | `ucsb.edu`                                                                                                                                               |

The next thing you'll want to do is set up your Auth0 SPA App. Instructions for setting up auth0 can be found [here](./docs/auth0.md).

- As part of these instructions, you will have created `javascript/.env.local` from `javascript/.env.local.SAMPLE`
- Next, you need update the values in your new `secrets-localhost.properties`. You can copy the corresponding values from the `javascript/.env.local`,
  using this guide:

  | For this value in `secrets-localhost.properties` | Use this value from `javascript/.env.local` |
  | ------------------------------------------------ | ------------------------------------------- |
  | `auth0.domain`                                   | `REACT_APP_AUTH0_DOMAIN`                    |
  | `auth0.clientId`                                 | `REACT_APP_AUTH0_CLIENT_ID`                 |
                    
  You may see additional values in `secrets-localhost.properties` such as the ones below. You do not need to adjust these; leave the values alone.

  ```
  security.oauth2.resource.jwk.keySetUri=https://${auth0.domain}/.well-known/jwks.json
  ```

At this point, you should be able to run the app locally via

```bash
mvn spring-boot:run
```

## Deploying to Production

To deploy to production on Heroku, see: [./docs/heroku.md](./docs/heroku.md)

## Setting up GitHub Actions

To setup GitHub Actions so that the tests pass, you will need to configure
some _secrets_ on the GitHub repo settings page; see: [./docs/github-actions-secrets.md](./docs/github-actions-secrets.md) for details.

## Setting up Custom Claims in Auth0

User Access Tokens are two json objects: within the json objects there are some keys and fields that are expected to be there. If you want to add addditional keys and values, these are called custom claims. The keys for custom claims must begin with something that resembles a url. This makes sure that claims from one application don't interfere with claims from another application.

In this case, we want to put in email, first name, and last name.

In Auth0.com go to the left hand sidebar and click `Rules`, then click `Create Rule`. Select `Empty Rule` at the top.

There is a function that takes a user, a context, and a callback. Context has an access token as a property. User has all of the user information. We want to add a property to context.accessToken.

To do this, add:

```javascript
context.accessToken[<application url>]={
  "email" : user.email,
  "given_name" : user.given_name,
  "family_name" : user.family_name
};
```

before the return statement. After that, save the new rule.

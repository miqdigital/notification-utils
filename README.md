## notification-utils
[![Build Status](https://travis-ci.org/MediaIQ/notification-utils.svg?branch=master)](https://travis-ci.org/MediaIQ/notification-utils) [![Coverage Status](https://coveralls.io/repos/github/MediaIQ/notification-utils/badge.svg?branch=master)](https://coveralls.io/github/MediaIQ/notification-utils?branch=master)  [![Codacy Badge](https://api.codacy.com/project/badge/Grade/2159704839b549ab870cd0c8d06bab9b)](https://www.codacy.com/app/ajatix/notification-utils?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=MediaIQ/notification-utils&amp;utm_campaign=Badge_Grade)

### How to use in an existing project
```bash
# clone the repo
git clone git@github.com:MediaIQ/notification-utils.git

# publish artifacts locally
sbt publishLocal
```
Refer to `sample-service` project

#### Travis CI Integration
##### Encrypting slack notifier
```bash
travis encrypt "<token>" --add notifications.slack
```

##### Encrypting environment variables
```bash
travis encrypt COVERALLS_REPO_TOKEN=<coveralls-repo-token> --add env
travis encrypt CODACY_PROJECT_TOKEN=<codacy-project-token> --add env

travis encrypt SLACK_WEBHOOK=<slack-webhook-url> --add env
travis encrypt SLACK_BOT_TOKEN=<slack-bot-token> --add env
travis encrypt SLACK_CHANNEL_ID=<sender-email-token> --add env
travis encrypt SLACK_USER_EMAIL=<sender-email-token> --add env

travis encrypt SENDER_EMAIL=<sender-email> --add env
travis encrypt SENDER_EMAIL_TOKEN=<sender-email-token> --add env
```
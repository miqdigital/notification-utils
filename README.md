## notification-utils
[![Build Status](https://travis-ci.org/MediaIQ/notification-utils.svg?branch=master)](https://travis-ci.org/MediaIQ/notification-utils) [![Coverage Status](https://coveralls.io/repos/github/MediaIQ/notification-utils/badge.svg?branch=master)](https://coveralls.io/github/MediaIQ/notification-utils?branch=master)

#### Travis CI Integration
##### Encrypting slack notifier
```bash
travis encrypt "<token>" --add notifications.slack
```

##### Encrypting environment variables
```bash
travis encrypt SLACK_WEBHOOK=<slack-webhook-url> --add env
travis encrypt COVERALLS_REPO_TOKEN=<coveralls-repo-token> --add env
```
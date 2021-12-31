# QSA I18n Service ![CircleCI](https://img.shields.io/circleci/build/github/Queueing-Systems-Assistance/qsa-i18n-service/master)

### Project description

This service is responsible for retrieving i18n keys from AWS DynamoDB. To use the service, install the AWS CLI and configure the default profile with the secret key and id.

### Functions

#### I18n Retriever

- Need a JSON input (example):
```json
[
    "system.element.MM2.name",
    "system.element.MM1.description"
]
```
- Output (example):
```json
[
    {
        "key": "system.element.MM1.description",
        "value": {
            "en_US": "Poisson process<br>Exponentially distributed arrival times<br>Exponentially distributed service times<br><b>1</b> server",
            "hu_HU": "Poisson-folyamat<br>Exponenciális eloszlású beérkezési időközök<br>Exponenciális eloszlású kiszolgálási időközök<br><b>1</b> kiszolgáló"
        }
    },
    {
        "key": "system.element.MM2.name",
        "value": {
            "en_US": "M | M | 2"
        }
    }
]
```

#### I18n Updater

- Need a JSON input (example):
```json
[
    {
        "key": "example.key",
        "value": {
            "en_US": "Example value"
        }
    }
]
```
- Output: the updated i18n keys in a list (example):
```json
[
    "example.key"
]
```

### Errors

- If anything goes wrong, this is the response the result will contain the error message, which is informative and should **never** be sent out to the user
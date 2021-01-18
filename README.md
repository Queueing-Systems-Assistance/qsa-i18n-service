# QSA I18n Service [![Build Status](https://travis-ci.com/Queueing-Systems-Assistance/qsa-i18n-service.svg?branch=master)](https://travis-ci.com/Queueing-Systems-Assistance/qsa-i18n-service)

### Project description

This service is responsible for retrieving i18n keys from the AWS. To use the service, please provide the following values in the `application.yml` file:
```yaml
aws:
  access-key-id: YOUR_KEY_ID
  secret-access-key: YOUR_ACCESS_KEY
```

### Endpoints

#### /keys

- Accepts only POST requests
- Need a JSON body (example):
```json
[
    "system.element.MM2.name",
    "system.element.MM1.description"
]
```
- Response (example):
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

#### /keys/update
- Accepts only POST requests
- Need a JSON body (example):
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
- Response: empty body, with 200 HTTP status code

### Errors

- If anything goes wrong, this is the response:
  - `422` - UNPROCESSABLE_ENTITY: requested key(s) not found. Check if they are available in the database
  - `406` - NOT_ACCEPTABLE: HTTP body not valid JSON
  - `404` - NOT_FOUND: wrong endpoint
  - `405` - METHOD_NOT_ALLOWED: wrong HTTP method
  - `500` - INTERNAL_SERVER_ERROR: any other error during the request processing (except `422`)

Other than that, there is a cache: every key cached 12 hours by default. If a key returns a null value (so the response code will be a `422`) it's not cached.

Every time, the application will retrieve all the requested keys with their locales or an error thrown. 
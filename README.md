# Mockenger #
[![Build Status](https://semaphoreci.com/api/v1/dryazanov/mockenger/branches/develop/badge.svg)](https://semaphoreci.com/dryazanov/mockenger)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/4cfcf88539ba49be8ed773807b312405)](https://www.codacy.com/app/dryazanov/mockenger)
##
##
**Frontend install**

* Install Node JS
* Install Bower
* Install Gulp

##
Install build dependencies from `package.json`
```
# cd mockenger-standalone-frontend/
# npm install
```
Run `npm install` every time when updating `package.json`

##
In order to install frontend libraries taken from `bower.json`, run
```
# cd mockenger-standalone-frontend/
# bower install
```

##
To build the frontend, run

```
# cd mockenger-standalone-frontend/
# gulp
```

##
##
**Backend install and run**

1) Install mongodb

2) Run mongodb
```
# mongod --dbpath /path/to/mongodb/data
```

Use `--auth` flag if you created users in your mongodb for access control
```
# mongod --auth --dbpath /path/to/mongodb/data
```
Don't forget to update property `spring.data.mongodb.uri` in `application.properties`


##
3) When your mongodb is up and running you can add some data there

 - To restore db from the dump run

```
# mongorestore --db mockenger mockenger-parent/testdata/mockenger/
```

 - To create db dump run

```
# mongodump --db mockenger --out mockenger-parent/testdata/
```

##
4) Start frontend and backend separately

Run backend
```
# cd mockenger-parent/
# mvn clean install
# cd ../mockenger-standalone/
# mvn spring-boot:run -Drun.arguments="--mockenger.frontend.port=15123"
```

Alternatively you can run backend using generated jar file
```
# cd mockenger-parent/
# mvn clean install
# cd ../mockenger-standalone/target/
# java -jar mockenger-standalone-<release_number>.jar --mockenger.frontend.port=15123
```

Run frontend
```
# gulp
# gulp webServer --security false
```

##
|                       | Frontend                            | Backend                               |
|-----------------------|-------------------------------------|---------------------------------------|
| **Environment**       | `--environment development` (check gulpfile.js for more information) | Override properties `server.address`, `server.port`, `mockenger.frontend.host`, `mockenger.frontend.port` |
| **Security (OAuth2)** | `--security true` | `--spring.profiles.active=security` |
| **No security**       | `--security false` | Profile `security` deactivated by default |

#
Examples:
```
# java -jar target/mockenger-standalone-<release_number>.jar --spring.profiles.active=security --mockenger.frontend.port=15123
# gulp webServer --environment production --security true
```
or
```
# java -jar target/mockenger-standalone-<release_number>.jar --mockenger.frontend.port=12345
# gulp webServer --environment development --security false
```

##
5) Start frontend and backend together
You need to create jar file first
```
# cd mockenger-standalone/
# mvn clean install -P withFrontend
```
Override properties for frontend as `-Dgulp.build.security=production -Dgulp.build.security=false`

After that start application with generated jar file
```
java -jar target/mockenger-standalone-<release_number>.jar
```

##
6) If use started application with OAuth2 security, use default credentials `admin@email.com/123456` for get access

##
##
**API response codes**

GET: 200 - Request has succeeded, return object or array of objects in the response body 404 - Resource not found

POST/PUT: 200 - Request has succeeded, return added/updated object in the response body

DELETE: 204 - Request has succeeded, no content in the response body

REQUEST ERROR: 400 - Bad request, data from user is not valid. Error description in the response body

INTERNAL ERROR: 500 - Technical error. Error description in the response body
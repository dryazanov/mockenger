# [Mockenger](http://mockenger.io)
[![Build Status](https://semaphoreci.com/api/v1/dryazanov/mockenger-2/branches/develop/badge.svg)](https://semaphoreci.com/dryazanov/mockenger-2)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/8767ae18cc6e4977b6bde215ac0e65e2)](https://www.codacy.com/app/dryazanov/mockenger?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=dryazanov/mockenger&amp;utm_campaign=Badge_Grade)


[Mockenger v3.0 releaed](https://github.com/dryazanov/mockenger/releases/tag/v3.0)

[All releases](https://github.com/dryazanov/mockenger/releases)

## For users

* [Install](https://www.mongodb.com) and run MongoDB
* [Download](https://github.com/dryazanov/mockenger/releases/latest) runable jar
* Create configuration file ([example](https://github.com/dryazanov/mockenger/blob/develop/examples/user.properties))
* Run!
```
java -jar mockenger-vX.X.jar --spring.config.location=/path/to/your/config/user.properties
```

## For developers

* [Install](https://nodejs.org) Node JS
* [Install](https://gulpjs.com/) Gulp
* [Install](https://www.mongodb.com) MongoDB


#### Run frontend and backend separately

* Start MongoDb
```
mongod --dbpath /path/to/mongodb/data
```

Use `--auth` flag if you created users in your mongodb for access control
```
mongod --auth --dbpath /path/to/mongodb/data
```
* Update `/core/src/main/resources/application.properties`
* Run java backend with maven
```
cd mockenger/
mvn clean install -DskipTests
cd standalone/
mvn spring-boot:run
```

Alternatively you can run backend using generated jar file
```
cd mockenger/
mvn clean install -DskipTests
java -jar standalone/target/mockenger-vX.X.jar --spring.config.location=/path/to/your/config/application.properties
```

For the frontend there are three options defined in `package.json` - `build`, `server`, `build:and:start:server`. Use one of them with `npm`, for example:
```
cd frontend/
npm run build:and:start:server
```
In the file `gulpfile.js` you can set you own properties for server's host and port 

##
#### Run frontend and backend together
* Start MongoDb
* Create runable jar and run it
```
cd mockenger/
mvn clean install -DskipTests -P withFrontend
java -jar standalone/target/mockenger-vX.X.jar --spring.config.location=/path/to/your/config/application.properties
```

- - - -
### API response codes
GET: 200 - Request has succeeded, return object or array of objects in the response body 404 - Resource not found

POST/PUT: 200 - Request has succeeded, return added/updated object in the response body

DELETE: 204 - Request has succeeded, no content in the response body

REQUEST ERROR: 400 - Bad request, data from user is not valid. Error description in the response body

INTERNAL ERROR: 500 - Technical error. Error description in the response body

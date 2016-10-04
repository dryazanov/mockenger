# Mockenger #
[![Build Status](https://semaphoreci.com/api/v1/dryazanov/mockenger/branches/develop/badge.svg)](https://semaphoreci.com/dryazanov/mockenger)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/4cfcf88539ba49be8ed773807b312405)](https://www.codacy.com/app/dryazanov/mockenger)
##
##
**Frontend install**

* Install Node JS
* Install Bower
* Install Grunt

##
From mockenger-standalone-frontend module run:

```
npm install
```
This will install build dependencies, taken from package.json. Run 'npm install' each time package.json updates.

##
In order to install frontend libraries taken from bower.json, run
```
cd mockenger-standalone-frontend/
```

```
bower install
```

##
In order to start frontend in dev mode, run

```
grunt serve
```

##
To build frontend, run

```
grunt
```

##
To build and start from built artifact, run

```
grunt serveDist
```

##
##
**Backend install and run**

1) Install mongodb

2) Run mongodb with the command
```
mongod --dbpath /path/to/mongodb/data
```

Use `auth` flag if you created users in your mongodb for access control.
Don't forget to update property `spring.data.mongodb.uri` in `application.properties`
```
mongod --auth --dbpath /path/to/mongodb/data
```

##
3) When your mongodb is up and running you can add some data there:
##
 - To restore db from the dump run
    ```
    mongorestore --db mockenger mockenger-parent/testdata/mockenger/
    ```

 - To create db dump run
    ```
    mongodump --db mockenger --out mockenger-parent/testdata/
    ```

##
4) Now, run command 
```
cd mockenger-standalone/
```

```
mvn clean install
```

```
mvn spring-boot:run
```

You can also start backend application using generated jar file
```
java -jar target/mockenger-standalone-<release_number>.jar
```
Use argument `--spring.profiles.active=security` if you want to 
start backend with OAuth2 security protection

##
5) Open http://localhost:15123/#/
##
6) Use default credentials `admin@email.com/123456` for get inside

##
##
**API response codes**

GET: 200 - Request has succeeded, return object or array of objects in the response body 404 - Resource not found

POST/PUT: 200 - Request has succeeded, return added/updated object in the response body

DELETE: 204 - Request has succeeded, no content in the response body

REQUEST ERROR: 400 - Bad request, data from user is not valid. Error description in the response body

INTERNAL ERROR: 500 - Technical error. Error description in the response body
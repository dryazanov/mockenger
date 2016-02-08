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
#!cmd

>> npm install
```
This will install build dependencies, taken from package.json. Run 'npm install' each time package.json updates.

##
From mockenger-standalone-frontend module run:

```
#!cmd

>> bower install
```
This will install frontend libraries taken from bower.json.

##
To start frontend in dev mode, run:

```
#!cmd

>> grunt serve
```

##
To build frontend, run:

```
#!cmd

>> grunt
```

##
To build and start from built artifact, run:

```
#!cmd

>> grunt serveDist
```

##
##
**Backend install and run**

1) Install mongodb

2) Run mongo with the command
mongod --dbpath \path\to\mongodb\data

3a) To restore db from the dump run:
mongorestore --db mockenger \mockenger-parent\testdata\mockenger

3b) To create db dump run:
mongodump --db mockenger --out mockenger-parent\testdata\

4) Go to mockenger-standalone parent folder and run
mvn spring-boot:run

5) Open http://localhost:15123/#/

##
##
**API response codes**

GET: 200 - Request has succeeded, return object or array of objects in the response body 404 - Resource not found

POST/PUT: 200 - Request has succeeded, return added/updated object in the response body

DELETE: 204 - Request has succeeded, no content in the response body

REQUEST ERROR: 400 - Bad request, data from user is not valid. Error description in the response body

INTERNAL ERROR: 500 - Technical error. Error description in the response body
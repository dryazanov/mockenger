// require
var gulp = require('gulp');
var uglify = require('gulp-uglify');
var concat = require('gulp-concat');
var removeUseStrict = require("gulp-remove-use-strict");
var ngAnnotate = require('gulp-ng-annotate');
var sourcemaps = require('gulp-sourcemaps');
var useref = require('gulp-useref');
var gulpif = require('gulp-if');
var csso = require('gulp-csso');
var autoprefixer = require('gulp-autoprefixer');
var copy = require('gulp-contrib-copy');
var es = require('event-stream');
var minifyCss = require('gulp-clean-css');
var lazypipe = require('lazypipe');
var ngConstant = require('gulp-ng-constant');
var webserver = require('gulp-webserver');

var fs = require('fs');
var parseString = require('xml2js').parseString;




// Returns current version number of the project from the pom.xml
var parseVersionFromPomXml = function () {
    var version;
    var pomXml = fs.readFileSync('pom.xml', 'utf8');
    parseString(pomXml, function (err, result) {
        version = result.project.parent[0].version[0];
    });
    return version;
};

// Returns value of the property frontend.build.dir
var parseBuildDirFromPomXml = function () {
    var buildDir;
    var pomXml = fs.readFileSync('../mockenger-parent/pom.xml', 'utf8');
    parseString(pomXml, function (err, result) {
        buildDir = result.project.properties[0]['frontend.build.dir'][0];
    });
    return buildDir;
};

var getArgument = function(option) {
    var ARG_PREFIX = "--";
    var i = process.argv.indexOf(ARG_PREFIX + option);

    if (i > -1) {
        var v = process.argv[i + 1];

        if (v.indexOf(ARG_PREFIX) > -1) {
            v = undefined;
        }

        console.log("Found argument '" + option + "' with value: " + v);
        return [true, v];
    }

    return [undefined, undefined];
}


// Create properties
var properties = {
        env: {
            dev: {
                host: 'localhost',
                port: 15123,
                const: {
                    ENV: 'dev',
                    SECURITY: true,
                    SECRET_KEY: 'Y2xpZW50YXBwOjEyMzQ1Ng==',
                    API_BASE_PATH: 'http://localhost:8080/api',
                    APP_VERSION: parseVersionFromPomXml(),
                    REQUESTS_PER_PAGE: 10,
                    BUILD_DATE: new Date()
                }
            },
            prod: {
                host: 'localhost',
                port: 15123,
                const: {
                    ENV: 'prod',
                    SECURITY: true,
                    SECRET_KEY: 'Y2xpZW50YXBwOjEyMzQ1Ng==',
                    API_BASE_PATH: 'http://localhost:9000/api',
                    APP_VERSION: parseVersionFromPomXml(),
                    REQUESTS_PER_PAGE: 20,
                    BUILD_DATE: new Date()
                }
            }
        },
        project: {
            source: 'src/main/resources/static/',
            dest: parseBuildDirFromPomXml() + '/'
        }
}


gulp.task('minifyAndUpdateIndexRef', ['ngConstants'], function () {
  return gulp.src(properties.project.source + 'index.html')
    .pipe(useref({}, lazypipe().pipe(sourcemaps.init, { loadMaps: true })))
    .pipe(gulpif('*.js', ngAnnotate()))
    .pipe(gulpif('*.js', removeUseStrict()))
    .pipe(gulpif('*.js', uglify()))
    .pipe(gulpif('*.css', autoprefixer({
        browsers: ['last 2 versions'],
        cascade: false
    })))
    .pipe(gulpif('*.css', csso()))
    .pipe(gulpif('*.css', minifyCss()))
    .pipe(sourcemaps.write('maps'))
    .pipe(gulp.dest(properties.project.dest));
});


gulp.task('copyViews', function() {
    return gulp.src([properties.project.source + 'modules/main/views/*.html'])
        .pipe(gulp.dest(properties.project.dest + 'modules/main/views/'))
});


gulp.task('copyFonts', function() {
    return gulp.src([properties.project.source + 'libs/bootstrap/fonts/*'])
        .pipe(gulp.dest(properties.project.dest + 'assets/fonts/'))
});


gulp.task('copyImages', function() {
    return gulp.src([properties.project.source + 'assets/images/*'])
        .pipe(gulp.dest(properties.project.dest + 'assets/images/'))
});


gulp.task('ngConstants', function () {
    console.log(process.argv);

    var securityArg = getArgument('security');
    var environmentArg = getArgument('environment');
    var envConfig = (environmentArg[0] !== undefined && environmentArg[1] === 'production') ? properties.env.prod.const : properties.env.dev.const;
    envConfig.SECURITY = (securityArg[0] === undefined || (String(securityArg[1]) != 'true' && String(securityArg[1]) != 'false')) ? true : securityArg[1];

    return ngConstant({
        constants: envConfig,
        name: 'mockengerClientComponents',
        wrap: false,
        stream: true
    })
    .pipe(gulp.dest(properties.project.source + 'modules/components/'));
});


gulp.task('webServer', function() {
    var environmentArg = getArgument('environment');
    var envConfig = (environmentArg[0] !== undefined && environmentArg[1] === 'production') ? properties.env.prod : properties.env.dev;

    return gulp.src(properties.project.source)
        .pipe(webserver({
            host: envConfig['host'],
            port: envConfig['port'],
            livereload: true,
            open: true
        }));
});



gulp.task('default', [
    'minifyAndUpdateIndexRef',
    'copyViews',
    'copyFonts',
    'copyImages'
])

var gulp = require('gulp');
var uglify = require('gulp-uglify');
var concat = require('gulp-concat');
var removeUseStrict = require("gulp-remove-use-strict");
var ngAnnotate = require('gulp-ng-annotate');
var sourcemaps = require('gulp-sourcemaps');
var useref = require('gulp-useref');
var gulpif = require('gulp-if');
var minifyCss = require('gulp-clean-css');
var lazypipe = require('lazypipe');

var fs = require('fs');

var parseString = require('xml2js').parseString;
// Returns the second occurence of the version number
var parseVersionFromPomXml = function () {
    var version;
    var pomXml = fs.readFileSync('pom.xml', 'utf8');
    parseString(pomXml, function (err, result) {
        version = result.project.parent[0].version[0];
    });
    return version;
};

var parseBuildDirFromPomXml = function () {
    var buildDir;
    var pomXml = fs.readFileSync('pom.xml', 'utf8');
    parseString(pomXml, function (err, result) {
        buildDir = result.project.properties[0]['grunt.build.dir'][0];
    });
    return buildDir;
};


var properties = {
    project: {
        version: parseVersionFromPomXml(),
        source: 'src/main/resources/static/',
        destination: 'dest/' //TODO: parseVersionFromPomXml()
    }
}

gulp.task('minifyAppJs', function () {
//    console.log('==> ' + properties.project.source);
//    console.log('==> ' + properties.project.destination);
    return gulp.src([
            properties.project.source + 'modules/**/mockengerClientComponents.js',
            properties.project.source + 'modules/**/!(mockengerClientComponents)*.js'
        ])
        .pipe(sourcemaps.init())
        .pipe(ngAnnotate())
        .pipe(uglify())
        .pipe(concat('api.js'))
        .pipe(removeUseStrict())
//        .pipe(sourcemaps.write("./"))
        .pipe(gulp.dest(properties.project.destination + 'js'))
});

gulp.task('minifyVendorJs', function () {
    return gulp.src(properties.project.source + 'libs/**/*.min.js')
        .pipe(concat('vendor.js'))
        .pipe(gulp.dest(properties.project.destination + 'js'))
});


gulp.task('minifyAppCss', function () {
    return gulp.src(properties.project.source + 'assets/**/*.css')
        .pipe(minifyCss())
        .pipe(gulp.dest(properties.project.destination))
});

gulp.task('minifyVendorCss', function () {
    return gulp.src(properties.project.source + 'libs/**/*.css')
        .pipe(minifyCss())
        .pipe(concat('vendor.css'))
        .pipe(gulp.dest(properties.project.destination + 'styles'))
});


gulp.task('default', ['minifyAppJs', 'minifyVendorJs', 'minifyAppCss', 'minifyVendorCss'])
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

// Create internal properties
var properties = {
    project: {
        version: parseVersionFromPomXml(),
        source: 'src/main/resources/static/',
        destination: parseBuildDirFromPomXml() + '/'
    }
}


gulp.task('minifyAndUpdateIndexRef', function () {
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
    .pipe(gulp.dest(properties.project.destination));
});

gulp.task('copyViews', function() {
    return gulp.src([properties.project.source + 'modules/main/views/*.html'])
        .pipe(gulp.dest(properties.project.destination + 'modules/main/views/'))
});

gulp.task('copyFonts', function() {
    return gulp.src([properties.project.source + 'libs/bootstrap/fonts/*'])
        .pipe(gulp.dest(properties.project.destination + 'assets/fonts/'))
});

gulp.task('copyImages', function() {
    return gulp.src([properties.project.source + 'assets/images/*'])
        .pipe(gulp.dest(properties.project.destination + 'assets/images/'))
});



gulp.task('default', ['minifyAndUpdateIndexRef', 'copyViews', 'copyFonts', 'copyImages'])

// require
var gulp = require('gulp');
var htmlReplace = require('gulp-html-replace');
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
var gnf = require('gulp-npm-files');
var runSequence = require('run-sequence');

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
    var pomXml = fs.readFileSync('../pom.xml', 'utf8');
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
	host: 'localhost',
	port: 15123,
	backend: {
		url: 'http://localhost:8080',
	},
	project: {
		source: 'src/main/resources/static/',
		dest: parseBuildDirFromPomXml() + '/'
	}
}


gulp.task('minifyAndUpdateIndexRef', function () {
  var args = getArgument('standalone');
  var backendUrl = (args[0] !== undefined && args[0] == true) ? properties.backend.url : '';

  return gulp.src(properties.project.source + 'index.html')
  	.pipe(htmlReplace({
  		'const': {
  			src: null,
  			tpl: '<script src="' + backendUrl + '/modules/components/constants.js"></script>'
  		}
  	}))
    .pipe(useref({}, lazypipe().pipe(sourcemaps.init, { loadMaps: true })))
    .pipe(gulpif('*.js', ngAnnotate()))
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


gulp.task('copyNpmDependencies', function() {
	return gulp.src(gnf(null, './package.json'), {base:'./'})
		.pipe(gulp.dest(properties.project.source));
});


gulp.task('copyViews', function() {
    return gulp.src([properties.project.source + 'modules/main/views/*.html'])
        .pipe(gulp.dest(properties.project.dest + 'modules/main/views/'))
});


gulp.task('copyFonts', function() {
    return gulp.src([properties.project.source + 'node_modules/bootstrap/fonts/*'])
        .pipe(gulp.dest(properties.project.dest + 'assets/styles/fonts/'))
});


gulp.task('copyImages', function() {
    return gulp.src([properties.project.source + 'assets/images/*'])
        .pipe(gulp.dest(properties.project.dest + 'assets/images/'))
});


gulp.task('webServer', function() {
    return gulp.src(properties.project.dest)
        .pipe(webserver({
            host: properties.host,
            port: properties.port,
            livereload: true,
            open: true
        }));
});



gulp.task('default', ['copyNpmDependencies'], function(callback) {
	runSequence('minifyAndUpdateIndexRef', [
		'copyViews',
		'copyFonts',
		'copyImages'
	],
	callback);
});

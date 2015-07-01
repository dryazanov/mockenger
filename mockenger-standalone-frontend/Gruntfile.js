'use strict';
var fs = require('fs');

var parseString = require('xml2js').parseString;
// Returns the second occurence of the version number
var parseVersionFromPomXml = function() {
    var version;
    var pomXml = fs.readFileSync('pom.xml', 'utf8');
    parseString(pomXml, function (err, result){
        version = result.project.version[0];
    });
    return version;
};


module.exports = function(grunt) {

    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

        mockengerfrontend: {
            // configurable paths
            app: require('./bower.json').appPath || 'app',
            dist: 'target/classes/static'
        },

        clean: {
            dist: {
                files: [{
                    dot: true,
                    src: [
                        '.tmp',
                        '<%= mockengerfrontend.dist %>/*',
                        '!<%= mockengerfrontend.dist %>/.git*'
                    ]
                }]
            },
            server: '.tmp'
        },
        watch: {
            bower: {
                files: ['bower.json'],
                tasks: ['wiredep']
            }
            //,
            //ngconstant: {
            //    files: ['Gruntfile.js', 'pom.xml'],
            //    tasks: ['ngconstant:dev']
            //}
        },

        wiredep: {
            app: {
                src: ['src/main/resources/static/index.html'],
                exclude: [
                    /angular-i18n/  // localizations are loaded dynamically
                ]
            }
        },

        jshint: {
            options: {
                jshintrc: '.jshintrc'
            },
            all: [
                'Gruntfile.js'
                //,
                //'src/main/webapp/scripts/app.js',
                //'src/main/webapp/scripts/app/**/*.js',
                //'src/main/webapp/scripts/components/**/*.js'
            ]
        }
    });

    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-wiredep');
    grunt.loadNpmTasks('grunt-contrib-watch');

    // task setup

    grunt.registerTask('test', [
    ]);

    grunt.registerTask('build', [
        'clean:dist',
        'wiredep:app',
        'jshint'
    ]);

    grunt.registerTask('default', [
        'test',
        'build'
    ]);

    grunt.registerTask('serve', [
        'clean:server',
        //'wiredep',
        //'ngconstant:dev',
        //'browserSync',
        'watch'
    ]);
};
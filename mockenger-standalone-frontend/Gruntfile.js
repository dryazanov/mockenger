'use strict';
var fs = require('fs');

var parseString = require('xml2js').parseString;
// Returns the second occurence of the version number
var parseVersionFromPomXml = function() {
    var version;
    var pomXml = fs.readFileSync('pom.xml', 'utf8');
    parseString(pomXml, function (err, result){
        version = result.project.parent[0].version[0];
    });
    return version;
};

// usemin custom step
var useminAutoprefixer = {
    name: 'autoprefixer',
    createConfig: function(context, block) {
        if(block.src.length === 0) {
            return {};
        } else {
            return require('grunt-usemin/lib/config/cssmin').createConfig(context, block); // Reuse cssmins createConfig
        }
    }
};


module.exports = function(grunt) {

    require('time-grunt')(grunt);

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
            },
            ngconstant: {
                files: ['Gruntfile.js', 'pom.xml'],
                tasks: ['ngconstant:dev']
            }
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
        },
        browserSync: {
            dev: {
                bsFiles: {
                    src : [
                        'src/main/resources/static/**/*.html',
                        'src/main/resources/static/**/*.json',
                        'src/main/resources/static/assets/styles/**/*.css',
                        'src/main/resources/static/scripts/**/*.js',
                        'src/main/resources/static/assets/images/**/*.{png,jpg,jpeg,gif,webp,svg}',
                        'tmp/**/*.{css,js}'
                    ]
                }
            },
            options: {
                watchTask: true,
                server: {
                    baseDir: 'src/main/resources/static',
                    index: 'index.html'
                }
            }
        },

        ngconstant: {
            options: {
                name: 'mockengerClientMainApp',
                deps: false,
                wrap: '"use strict";\n// DO NOT EDIT THIS FILE, EDIT THE GRUNT TASK NGCONSTANT SETTINGS INSTEAD WHICH GENERATES THIS FILE\n{%= __ngModule %}'
            },
            dev: {
                options: {
                    dest: 'src/main/resources/static/modules/main/app.constants.js'
                },
                constants: {
                    ENV: 'dev',
                    CLIENT_VERSION: parseVersionFromPomXml()
                }
            },
            prod: {
                options: {
                    dest: '.tmp/scripts/app/app.constants.js'
                },
                constants: {
                    ENV: 'prod',
                    CLIENT_VERSION: parseVersionFromPomXml()
                }
            }
        },


        useminPrepare: {
            html: 'src/main/resources/static/**/*.html',
            options: {
                dest: '<%= mockengerfrontend.dist %>',
                flow: {
                    html: {
                        steps: {
                            js: ['concat', 'uglifyjs'],
                            css: ['cssmin', useminAutoprefixer] // Let cssmin concat files so it corrects relative paths to fonts and images
                        },
                        post: {}
                    }
                }
            }
        }


    });

    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-wiredep');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-browser-sync');
    grunt.loadNpmTasks('grunt-ng-constant');

    grunt.loadNpmTasks('grunt-usemin');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    grunt.loadNpmTasks('grunt-filerev');
    grunt.loadNpmTasks('grunt-angular-templates');

    // task setup

    grunt.registerTask('test', [
        'clean:server',
        'wiredep',
        //'wiredep:test',
        'ngconstant:dev',
        'jshint'
        //,
        //'karma'
    ]);

    grunt.registerTask('build', [
        'clean:dist',
        'wiredep:app',
        'ngconstant:prod',
        'useminPrepare'
    ]);

    grunt.registerTask('default', [
        'test',
        'build'
    ]);

    grunt.registerTask('serve', [
        'clean:server',
        'wiredep',
        'ngconstant:dev',
        'browserSync',
        'watch'
    ]);
};
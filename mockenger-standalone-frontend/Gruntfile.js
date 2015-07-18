'use strict';
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

// usemin custom step
var useminAutoprefixer = {
    name: 'autoprefixer',
    createConfig: function (context, block) {
        if (block.src.length === 0) {
            return {};
        } else {
            return require('grunt-usemin/lib/config/cssmin').createConfig(context, block); // Reuse cssmins createConfig
        }
    }
};


module.exports = function (grunt) {

    require('time-grunt')(grunt);

    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

        mockengerfrontend: {
            // configurable paths
            app: require('./bower.json').appPath || 'app',
            dist: 'dist'
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
                'Gruntfile.js',
                'src/main/resources/static/modules/**/*.js',
                'src/main/resources/static/*.js'
            ]
        },

        browserSync: {
            dev: {
                bsFiles: {
                    src: [
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
                wrap: '\'use strict\';\n// DO NOT EDIT THIS FILE, EDIT THE GRUNT TASK NGCONSTANT SETTINGS INSTEAD WHICH GENERATES THIS FILE\n{%= __ngModule %}'
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
        },

        copy: {
            fonts: {
                files: [{
                    expand: true,
                    dot: true,
                    flatten: true,
                    cwd: 'src/main/resources/static',
                    dest: '<%= mockengerfrontend.dist %>/assets/fonts',
                    src: [
                        'libs/bootstrap/fonts/*.*'
                    ]
                }]
            },
            dist: {
                files: [{
                    expand: true,
                    dot: true,
                    cwd: 'src/main/resources/static',
                    dest: '<%= mockengerfrontend.dist %>',
                    src: [
                        '*.html',
                        'modules/**/*.html',
                        'modules/**/assets/images/**/*.{png,gif,webp,jpg,jpeg,svg}',

                        'assets/images/**/*.{png,gif,webp,jpg,jpeg,svg}',
                        'assets/fonts/*'
                    ]
                }
                ]
            }
        },

        concat: {
            // src and dest is configured in a subtask called "generated" by usemin
        },

        uglifyjs: {
            // src and dest is configured in a subtask called "generated" by usemin
        },

        cssmin: {
            // src and dest is configured in a subtask called "generated" by usemin
        },

        autoprefixer: {
            // src and dest is configured in a subtask called "generated" by usemin
        },

        usemin: {
            html: ['<%= mockengerfrontend.dist %>/**/*.html'],
            css: ['<%= mockengerfrontend.dist %>/assets/styles/**/*.css'],
            js: ['<%= mockengerfrontend.dist %>/scripts/**/*.js'],
            options: {
                assetsDirs: ['<%= mockengerfrontend.dist %>', '<%= mockengerfrontend.dist %>/assets/styles', '<%= mockengerfrontend.dist %>/assets/images', '<%= mockengerfrontend.dist %>/assets/fonts'],
                patterns: {
                    js: [
                        [/(assets\/images\/.*?\.(?:gif|jpeg|jpg|png|webp|svg))/gm, 'Update the JS to reference our revved images']
                    ]
                },
                dirs: ['<%= mockengerfrontend.dist %>']
            }
        },

        connect: {
            dist: {
                options: {
                    keepalive: true,
                    port: 15123,
                    hostname: 'localhost',
                    base: '<%= mockengerfrontend.dist %>'
                }
            }
        },

        rev: {
            dist: {
                files: {
                    src: [
                        '<%= mockengerfrontend.dist %>/scripts/**/*.js',
                        '<%= mockengerfrontend.dist %>/assets/styles/**/*.css',
                        '<%= mockengerfrontend.dist %>/assets/images/**/*.{png,jpg,jpeg,gif,webp,svg}',
                        '<%= mockengerfrontend.dist %>/assets/fonts/*'
                    ]
                }
            }
        },

        htmlmin: {
            dist: {
                options: {
                    removeCommentsFromCDATA: true,
                    // https://github.com/yeoman/grunt-usemin/issues/44
                    collapseWhitespace: true,
                    collapseBooleanAttributes: true,
                    conservativeCollapse: true,
                    removeAttributeQuotes: true,
                    removeRedundantAttributes: true,
                    useShortDoctype: true,
                    removeEmptyAttributes: true,
                    keepClosingSlash: true
                },
                files: [{
                    expand: true,
                    cwd: '<%= mockengerfrontend.dist %>',
                    src: ['*.html', '**/*.html'],
                    dest: '<%= mockengerfrontend.dist %>'
                }]
            }
        }

    });

    grunt.loadNpmTasks('grunt-browser-sync');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-connect');
    grunt.loadNpmTasks('grunt-open');
    grunt.loadNpmTasks('grunt-wiredep');
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-ng-constant');
    grunt.loadNpmTasks('grunt-usemin');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    grunt.loadNpmTasks('grunt-contrib-copy');
    //grunt.loadNpmTasks('grunt-filerev');
    grunt.loadNpmTasks('grunt-rev');
    grunt.loadNpmTasks('grunt-autoprefixer');
    grunt.loadNpmTasks('grunt-contrib-htmlmin');
    grunt.loadNpmTasks('grunt-contrib-jshint');


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
        'useminPrepare',
        'concat',
        'copy:fonts',
        'copy:dist',
        'cssmin',
        'autoprefixer',
        'uglify',
        'rev',
        'usemin',
        'htmlmin'
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

    grunt.registerTask('serveDist', [
        'build',
        'connect:dist'
    ]);
};
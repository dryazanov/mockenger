module.exports = function(grunt) {

    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),


        wiredep: {
            app: {
                src: ['src/main/resources/static/index.html'],
                exclude: [
                    /angular-i18n/  // localizations are loaded dynamically
                ]
            }
        }
    });

    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-wiredep');

    // task setup
    grunt.registerTask('default', ['wiredep']);
};
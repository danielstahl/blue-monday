module.exports = function(grunt) {
    grunt.initConfig({
      pkg: grunt.file.readJSON('package.json'),
      connect: {
        server: {
          options: {
            port: 8080,
            base: 'app'
          }
        }
      },
      watch: {
        files: ['app/**/*']
      },
      jshint: {
        all: ['Gruntfile.js', 'app/js/**/*.js', 'test/**/*.js']
      },
      karma: {
        unit: {
          configFile: 'karma.conf.js',
          runnerPort: 9999,
          singleRun: true,
          browsers: ['PhantomJS']
        }
      }
    });
    
    grunt.loadNpmTasks('grunt-contrib-connect');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-karma');
    
    grunt.registerTask("dev", ["connect", "watch"]);
};

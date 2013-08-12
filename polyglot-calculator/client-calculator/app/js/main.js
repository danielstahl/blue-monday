/* global requirejs, require */

requirejs.config({
  baseUrl: '',
  paths: {
    flight: 'bower_components/flight',
    component: 'js/component',
    page: 'js/page'
  }
});

require(['page/default'], function(App) {
    'use strict';
    App.initialize();
});

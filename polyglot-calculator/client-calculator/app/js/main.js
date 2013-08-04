'use strict';

requirejs.config({
  baseUrl: '',
  paths: {
    flight: 'bower_components/flight',
    component: 'js/component',
    page: 'js/page'
  }
});

require(['page/default'], function(App) {
    App.initialize();
});

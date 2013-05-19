'use strict';

require.config({
	baseUrl: './assets',
	paths: {
		jquery: 'components/jquery/jquery-1.9.1',
		es5shim: 'components/es5-shim/es5-shim',
	    es5sham: 'components/es5-sham/es5-sham'
	},
	map: {
		'*': {
			'component': 'components/flight/lib/component'
		}
	},
	shim: {
		'components/flight/lib/index': {
			deps: ['jquery', 'es5shim', 'es5sham']
		},
		'components/app': {
			deps: ['components/flight/lib/index']
		}
	}
});

require(['components/app'], function(App) {
	App.initialize();
});

/*
require(["jquery"], function($) {
	$(document).ready(function() {
		alert('Hello world')
	});
});
*/
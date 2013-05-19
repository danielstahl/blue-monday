'use strict';

define(
	[
	 './calculateform'
	 ],
	 
	 function(CalculateFormUI) {
		var initialize = function() {
			CalculateFormUI.attachTo('#calculateform')
		}
		return {
			initialize: initialize
		};
	}
);
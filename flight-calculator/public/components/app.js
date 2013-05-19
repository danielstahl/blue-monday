'use strict';

define(
	[
	 './calculateForm'
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
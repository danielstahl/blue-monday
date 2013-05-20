'use strict';

define(
	[
	    './calculateform',
        './calculateservice',
        './calculateresult',
        'components/flight/tools/debug/debug'

	 ],
	 
	 function(CalculateFormUI, CalculateServiceUI, CalculateResultUI) {
		var initialize = function() {
			CalculateFormUI.attachTo('#calculateform');
            CalculateServiceUI.attachTo(document);
            CalculateResultUI.attachTo('#result');
		}
		return {
			initialize: initialize
		};
	}
);
'use strict';

define(
	[
	    './calculateform',
        './calculateresult',
        'components/flight/tools/debug/debug'

	 ],
	 
	 function(CalculateFormUI, ResultUI) {
		var initialize = function() {
			CalculateFormUI.attachTo('#calculateform');
            ResultUI.attachTo('#result');
		}
		return {
			initialize: initialize
		};
	}
);
'use strict';

define(
	[
	    'components/flight/lib/component'
	],
	 function(defineComponent) {
		return defineComponent(calculateForm);
		
		function calculateForm() {
			
			this.after('initialize', function() {
				this.on('submit', function() {
		        	this.trigger(document, 'calculateFormEvent', {payload: $("input:first").val()});
                    return false;
		        });
		    });
		}
		
		
	}
	
);
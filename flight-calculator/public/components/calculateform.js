'use strict';

define(
	[
	 'components/flight/lib/component'
	 ],
	 function(defineComponent) {
		return defineComponent(calculateForm);
		
		function calculateForm() {
			
			this.after('initialize', function() {
		        this.on('click', function(e) {
		        	alert(e)		
		        });
		    });
		}
		
		
	}
	
);
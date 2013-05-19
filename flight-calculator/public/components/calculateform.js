'use strict';

define(
	[
	 'component'
	 ],
	 function(defineComponent) {
		return defineComponent(calculateForm);
		
		function calculateForm() {
			this.submit = function(e) {
				alert(this.$node.val)				
			}
		}
	}
	
);
'use strict';

describeComponent('component/calculateform', function() {
	beforeEach(function() {
		setupComponent();
	});
	
	it('should send event on submit', function() {
		var eventSpy = spyOnEvent(document, 'calculateFormEvent');
		this.component.trigger('submit');
		expect(eventSpy).toHaveBeenTriggeredOn(document);
	});
	
	it('should fetch input from the first input input', function() {
		var eventSpy = spyOnEvent(document, 'calculateFormEvent');
		setupComponent('<input id="expr" name="expression" type="text" value="5" />');
		this.component.trigger('submit');
		expect(eventSpy.mostRecentCall.data).toEqual({
			payload: '5'
		});
		
	});
});

'use strict';

define(
    [
        'flight/lib/component'
    ],

    function(defineComponent) {
        return defineComponent(calculateService);

        function calculateService() {
            this.restService = function(e, data) {
                var expr = data.payload;
                var myComp = this;

                $.get('/service/calculate', {
                    expression: expr
                }, function(data) {
                    myComp.trigger(document, 'calculateResultEvent', {payload: expr + " = " + data});
                }).error(function(data) {
                    myComp.trigger(document, 'calculateErrorResultEvent', {payload: expr + " is not a valid expression: " + data.responseText});
                });
            };

            this.after('initialize', function() {
                this.on(document, 'calculateFormEvent', this.restService);
            });
        }
    }
);

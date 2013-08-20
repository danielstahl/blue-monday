/* global define, $ */

define(
    [
        'flight/lib/component'
    ],
    function (defineComponent) {
        'use strict';

        return defineComponent(calculateForm);

        function calculateForm() {
           /* jshint validthis: true */
            this.after('initialize', function () {
                this.on('submit', function () {
                    this.trigger(document, 'calculateFormEvent', {payload: $("input:first").val()});
                    return false;
                });
            });
        }

    }

);
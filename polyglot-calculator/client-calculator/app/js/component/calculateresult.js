

/* global define */

define(
  [
      'flight/lib/component'
  ],

  function(defineComponent) {
    'use strict';
    return defineComponent(calculateResult);

    function calculateResult() {
        /* jshint validthis: true */
        this.renderItem = function(e, data) {
            e.preventDefault();
            this.$node.removeClass('text-danger').addClass('text-success');
            this.$node.text(data.payload);
        };

        this.renderErrorItem = function(e, data) {
            e.preventDefault();
            this.$node.removeClass('text-success').addClass('text-danger');
            this.$node.text(data.payload);
        };

        this.after('initialize', function() {
           this.on(document, 'calculateResultEvent', this.renderItem);
           this.on(document, 'calculateErrorResultEvent', this.renderErrorItem);
        });

    }
  }

);

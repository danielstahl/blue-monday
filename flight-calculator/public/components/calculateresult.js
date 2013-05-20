'use strict';

define(
  [
      'components/flight/lib/component'
  ],

  function(defineComponent) {
    return defineComponent(calculateResult);

    function calculateResult() {
        this.renderItem = function(e, data) {
            e.preventDefault();
            this.$node.removeClass('text-error').addClass('text-success')
            this.$node.text(data.payload)
        }

        this.renderErrorItem = function(e, data) {
            e.preventDefault();
            this.$node.removeClass('text-success').addClass('text-error')
            this.$node.text(data.payload)
        }

        this.after('initialize', function() {
           this.on(document, 'calculateResultEvent', this.renderItem);
           this.on(document, 'calculateErrorResultEvent', this.renderErrorItem);
        });

    }
  }

);
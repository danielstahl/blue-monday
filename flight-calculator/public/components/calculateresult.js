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

            var expr = data.payload;
            var myNode = this.$node;

            $.get('/calculateAjax', {
                    expression: expr
                }, function(data) {
                    myNode.removeClass('text-error').addClass('text-success')
                    myNode.text(expr + " = " + data)
                }).error(function(data) {
                    myNode.removeClass('text-success').addClass('text-error')
                    myNode.text(expr + " is not a valid expression: " + data.responseText)
                });
        }

        this.after('initialize', function() {
           this.on(document, 'calculateFormEvent', this.renderItem)
        });

    }
  }

);
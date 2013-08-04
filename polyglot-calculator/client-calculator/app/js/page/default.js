'use strict';

define(
  [
    'component/calculateform',
    'component/calculateservice',
    'component/calculateresult'
  ],

  function(CalculateFormUI, CalculateServiceUI, CalculateResultUI) {
    var initialize = function() {
      CalculateFormUI.attachTo('#calculateform');
      CalculateServiceUI.attachTo(document);
      CalculateResultUI.attachTo('#result');
    };
    return {
      initialize: initialize
    };
  }
);

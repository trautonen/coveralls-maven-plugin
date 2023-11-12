(function() {
  var Localization;

  Localization = (function() {
    function Localization(values) {
      this.values = values;
    }

    Localization.prototype.byKey = function(key) {
      return this.values[key];
    };

    return Localization;

  })();

}).call(this);

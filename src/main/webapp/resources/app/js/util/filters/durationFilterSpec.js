define(['angular', 'angular-mocks'], function () {
  describe('The duration filter', function () {
    var durationFilter;

    beforeEach(module('trialverse.util'));

    beforeEach(inject(function($filter) {
      durationFilter = $filter('durationFilter');
    }));

    it('should pass though undefined durations', function() {
      expect(durationFilter(undefined)).toEqual(undefined);
    });

    it('should humanize a valid duration', function() {
      expect(durationFilter('P1D')).toEqual('a day');
    });

    it('should replace empty periods with null', function() { // is this what we want ? :s
      expect(durationFilter('P0D')).toEqual(null);
      expect(durationFilter('-P0D')).toEqual(null);
    });

    it('should replace 0 seconds with "instantaneous"', function() {
      expect(durationFilter('PT0S')).toEqual('instantaneous');
    });

  });
});

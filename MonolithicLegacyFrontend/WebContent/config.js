angular.
	module('app').
	run(["$rootScope", "$location", function($rootScope, $location) {
		$rootScope.$on("$routeChangeError", function(event, next, previous, error) {
			if (error === "AUTH_REQUIRED") {
				$location.path("/");
			}
		});
	}
]);

angular.
	module('app').
	config(['$routeProvider', function config($routeProvider) {
		$routeProvider.
		when('/', {
			template: '<login></login>'
		}).
		when('/books', {
			template: '<book-table></book-table><logout></logout>',
			resolve: {
				"currentAuth": ["Auth", function(Auth) {
					return Auth.$waitForSignIn();
				}]
			}
		}).
		when('/books/:bookId', {
			template: '<book-detail></book-detail>',
			resolve: {
				"currentAuth": ["Auth", function(Auth) {
					return Auth.$waitForSignIn();
				}]
			}
		}).
		otherwise('/');
	}
]);
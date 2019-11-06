angular.
	module('component.logout').
	component('logout', {
	templateUrl: 'component/logout/template.html',
	controller: ['Auth', '$location', function LoginController(Auth, $location) {
		var self = this;
		self.auth = Auth;
		
		if(self.auth.$getAuth() == null) {
		//	$location.path('/login');
		}

		self.signOut = function() {
			self.auth.$signOut().then(function() {
				$location.path('/login');
			}).catch(function(error) {
				self.error = error;
			});
		}
	}]
})
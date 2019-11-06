angular.
	module('component.login').
	component('login', {
	templateUrl: 'component/login/template.html',
	controller: ['Auth', '$location', '$cookies', function LoginController(Auth, $location, $cookies) {
		var self = this;
		self.auth = Auth;
		
		if($cookies.get('authenticating')) {
			self.authenticating = true;
		} else {
			self.authenticating = false;
		}
		
		self.auth.$onAuthStateChanged(function(firebaseUser) {
			if(firebaseUser) {
				$cookies.remove('authenticating');
				$location.path('/books');
				firebaseUser.getIdToken().then(function(token) { 
				}).catch(function(error) {
					self.error = error;
				});
			}
		});

		self.signIn = function() {
			self.auth.$signInWithEmailAndPassword(self.email, self.password).then(function(firebase) {
			}).catch(function(error) {
				self.error = error;
			});
		}
		
		self.signInWithGoogle = function() {
			$cookies.put('authenticating', true);
			self.auth.$signInWithRedirect("google");
		}
	}]
})
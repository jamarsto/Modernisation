angular.module('component.signin').component('signin', {
	templateUrl: 'component/signin/template.html',
	controller: ['Auth', '$location', '$cookies', '$timeout', function SigninController(Auth, $location, $cookies, $timeout) {
		var self = this;
		self.auth = Auth;
		
		if($cookies.get('authenticating')) {
			self.authenticating = true;
			$timeout(function() { self.authenticating = false; $cookies.remove('authenticating'); }, 5000);
		} else {
			self.authenticating = false;
		}
		
		self.auth.$onAuthStateChanged(function(firebaseUser) {
			if(firebaseUser) {
				$cookies.remove('authenticating');
				$location.path('/books');
			}
		});

		self.signIn = function() {
			$cookies.put('authenticating', true);
			self.auth.$signInWithEmailAndPassword(self.email, self.password);
		}
		
		self.signInWithGoogle = function() {
			$cookies.put('authenticating', true);
			self.auth.$signInWithRedirect("google");
		}
	}]
})
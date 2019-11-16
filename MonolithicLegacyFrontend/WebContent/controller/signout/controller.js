app.controller('signout', ['$scope', 'Auth', '$location', function SignoutController($scope, Auth, $location) {
	$scope.auth = Auth;
	
	$scope.signOut = function() {
		$scope.auth.$signOut().then(function() {
			$location.path('/signin');
		});
	}
}]);
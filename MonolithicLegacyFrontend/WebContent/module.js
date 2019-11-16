var app = angular.module('app', ['ngRoute', 'ui.bootstrap', 'firebase', 'component.signin', 'component.bookTable', 'component.bookDetail']);

app.factory("Auth", ["$firebaseAuth", 
	function($firebaseAuth) {
		return $firebaseAuth();
	}
]);
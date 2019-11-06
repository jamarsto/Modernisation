var app = angular.module('app', ['ngRoute', 'ngCookies', 'ui.bootstrap', 'firebase', 'component.login', 'component.logout', 'component.bookTable', 'component.bookDetail', 'service.book']);

app.factory("Auth", ["$firebaseAuth", function($firebaseAuth) {
	return $firebaseAuth();
}]);
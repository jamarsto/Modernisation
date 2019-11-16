angular.module('component.bookDetail').component('bookDetail', {
	templateUrl: 'component/book-detail/template.html',
	controller: ['Auth', '$routeParams', 'Book', function BookDetailController(Auth, $routeParams, Book) {
		var self = this;
		self.auth = Auth;
		self.auth.$getAuth().getIdToken().then(function(token) {
			Book.setToken(token);
			self.book = Book.getUnique($routeParams.bookId);
		})
	}]
});
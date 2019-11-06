angular.
	module('component.bookTable').
	component('bookTable', {
	templateUrl: 'component/book-table/template.html',
	controller: ['Auth', 'Book', function BookTableController(Auth, Book) {
		var self = this;
		Auth.$getAuth().getIdToken().then(function(token) {
			Book.setToken(token);
			self.books = Book.getAll();
		})
		self.orderProp = 'age';
	}]
})
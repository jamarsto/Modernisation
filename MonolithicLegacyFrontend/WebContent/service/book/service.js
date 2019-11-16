angular.module('service.book').
	service('Book', ['$resource', function($resource) {
		var token = null;
		var resource = null;
		this.setToken = function(newToken) {
			if(token !== newToken) {
				token = newToken;
				resource = $resource('books/:bookId.json', {bookId: '@bookId'}, {
					getAll: {
						method: 'GET',
						params: {bookId: 'books'},
						isArray: true,
						headers: {
							'Authorization': 'Bearer ' + token
						}
					},
					getUnique: {
						method: 'GET',
						params: {bookId: '@bookId'},
						isArray: false,
						headers: {
							'Authorization': 'Bearer ' + token
						}
					}
				});
			}
		};

		this.getAll = function() {
			return resource.getAll();
		};

		this.getUnique = function(bookId) {
			return resource.getUnique({bookId: bookId});
		};
	}
]);
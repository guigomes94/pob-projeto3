package dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import model.Book;

public class DAOBook extends DAO<Book> {
	
	public Book read (Object chave){
		try{
			String nome = (String) chave;
			TypedQuery<Book> q = manager.createQuery("select p from Book p where p.title=:n", Book.class);
			q.setParameter("n", nome);

			return q.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}
	
	/**********************************************************
	 * 
	 * CONSULTAS DE BOOK
	 * 
	 **********************************************************/

	public List<Book> readBooksAvailable() {
		TypedQuery<Book> q = manager.createQuery
				("select p from Book p where p.available = true",Book.class);
		return q.getResultList();
	}

	public List<Book> readBooksByTitle(String caracteres) {
		TypedQuery<Book> q = manager.createQuery
				("select p from Book p where lower(p.title) like lower('%"+caracteres+"%')",Book.class);
		return q.getResultList();
	}

	public List<Book> readBooksByAuthor(String author) {
		TypedQuery<Book> q = manager.createQuery
				("select b from Book b JOIN b.author a where lower(a.name) like lower('%"+author+"%')",Book.class);
		return q.getResultList();
	}

	public List<Book> readBooksByGenre(String genre) {
		TypedQuery<Book> q = manager.createQuery
				("select b from Book b JOIN b.genre g where lower(g.name) like lower('%"+genre+"%')",Book.class);
		return q.getResultList();
	}
	 
}

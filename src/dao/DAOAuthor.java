package dao;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import model.Author;

public class DAOAuthor extends DAO<Author> {

	public Author read (Object chave){
		try{
			String nome = (String) chave;
			TypedQuery<Author> q = manager.createQuery("select p from Author p where p.name=:n", Author.class);
			q.setParameter("n", nome);

			return q.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}

}

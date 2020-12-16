package dao;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import model.Genre;

public class DAOGenre extends DAO<Genre> {
	
	public Genre read (Object chave){
		try{
			String nome = (String) chave;
			TypedQuery<Genre> q = manager.createQuery("select p from Genre p where p.name=:n", Genre.class);
			q.setParameter("n", nome);

			return q.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}

}

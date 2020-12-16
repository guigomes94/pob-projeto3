package dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import model.User;

public class DAOUser extends DAO<User> {
	
	public User read (Object chave){
		try{
			String nome = (String) chave;
			TypedQuery<User> q = manager.createQuery("select p from User p where p.name=:n", User.class);
			q.setParameter("n", nome);

			return q.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}
	
	/**********************************************************
	 * 
	 * CONSULTAS DE USER
	 * 
	 **********************************************************/
	
	public List<User> readUsersByName(String name) {
		TypedQuery<User> q = manager.createQuery
				("select p from User p where lower(p.name) like lower('%"+name+"%')",User.class);
		return q.getResultList();
	}

	public List<User> readUsersByEmail(String email) {
		TypedQuery<User> q = manager.createQuery
				("select p from User p where lower(p.email) like lower('%"+email+"%')",User.class);
		return q.getResultList();
	}	 

}

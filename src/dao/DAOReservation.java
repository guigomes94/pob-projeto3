package dao;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import model.Reservation;

public class DAOReservation extends DAO<Reservation> {
	
	public Reservation read (Object chave){
		try{
			String id = (String) chave;
			TypedQuery<Reservation> q = manager.createQuery("select r from Reservation r where r.id=:n", Reservation.class);
			q.setParameter("n", id);

			return q.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}
	
	/**********************************************************
	 * 
	 * CONSULTAS DE RESERVATION
	 * 
	 **********************************************************/
	
	
	public Reservation readReservationById(String id) {
			TypedQuery<Reservation> q = manager.createQuery("select r from Reservation r where r.id=:id",Reservation.class);
			q.setParameter("id", id);
					
			return q.getSingleResult();
	}
	 
}

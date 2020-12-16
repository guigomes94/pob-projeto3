package dao;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import model.Loan;

public class DAOLoan extends DAO<Loan> {

	public Loan read (Object chave){
		try{
			String id = (String) chave;
			TypedQuery<Loan> q = manager.createQuery("select l from Loan l where l.id=:n", Loan.class);
			q.setParameter("n", id);

			return q.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}

	/**********************************************************
	 * 
	 * CONSULTAS DE LOAN
	 * 
	 **********************************************************/
	
	public Loan readLoanById(String id) {
		TypedQuery<Loan> q = manager.createQuery("select l from Loan l where l.id=:id",Loan.class);
		q.setParameter("id", id);
				
		return q.getSingleResult();
	}

	public List<Loan> readLastLoans() {
		LocalDate today = LocalDate.now();
		LocalDate behind = today.minusDays(7);
		TypedQuery<Loan> q = manager.createQuery
				("select l from Loan l where l.loanDate < :today and l.loanDate > :behind",Loan.class);
		q.setParameter("today", today);
		q.setParameter("behind", behind);
		return q.getResultList();
	}

	public List<Loan> readNextDevolutions() {
		LocalDate today = LocalDate.now();
		LocalDate after = today.plusDays(7);
		TypedQuery<Loan> q = manager.createQuery
				("select l from Loan l where l.devolutionDate < :after and l.devolutionDate > :today",Loan.class);
		q.setParameter("today", today);
		q.setParameter("after", after);
		return q.getResultList();
	}
	 
}

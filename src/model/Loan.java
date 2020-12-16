package model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.eclipse.persistence.nosql.annotations.DataFormatType;
import org.eclipse.persistence.nosql.annotations.NoSql;

import dao.DAOLoan;

@Entity 
@EntityListeners( DAOLoan.class )  						
@NoSql(dataFormat=DataFormatType.MAPPED)
public class Loan {
	
	@Id		
	@GeneratedValue
	@Column(name="_id")
	private String id;
	
	@Column(columnDefinition = "TIMESTAMP")
	private LocalDate loanDate;
	
	@Column(columnDefinition = "TIMESTAMP")
	private LocalDate devolutionDate;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;
	
	private Double loanValue;
	
	public Loan() {
		
	}

	public Loan(LocalDate loanDate, User user, Book book, LocalDate devolutionDate) {
		this.loanDate = loanDate;
		this.user = user;
		this.book = book;
		this.devolutionDate = devolutionDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDate getLoanDate() {
		return loanDate;
	}

	public void setLoanDate(LocalDate loanDate) {
		this.loanDate = loanDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String getUserName() {
		return user.getName();
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}
	
	public String getBookTitle() {
		return book.getTitle();
	}

	public LocalDate getDevolutionDate() {
		return devolutionDate;
	}

	public void setDevolutionDate(LocalDate devolutionDate) {
		this.devolutionDate = devolutionDate;
	}

	public Double getLoanValue() {
		return loanValue;
	}

	public void setLoanValue(Double loanValue) {
		this.loanValue = loanValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Loan other = (Loan) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "id=" + id + ", loanDate=" + loanDate + ", user=" + user.getName() + ", book=" + book.getTitle() + ", devolutionDate="
				+ devolutionDate + ", loanValue=" + loanValue;
	}
	
	
	
	
		
}

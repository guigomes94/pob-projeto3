package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.eclipse.persistence.nosql.annotations.DataFormatType;
import org.eclipse.persistence.nosql.annotations.NoSql;

import dao.DAOBook;

@Entity 
@EntityListeners( DAOBook.class )  						
@NoSql(dataFormat=DataFormatType.MAPPED)
public class Book {
	
	@Id		
	@GeneratedValue
	@Column(name="_id")
	private String id;
	
	private String title;
	private Integer pages;
	
	@ManyToOne
	@JoinColumn(name = "genre_id")
	private Genre genre;
	
	@ManyToOne
	@JoinColumn(name = "author_id")
	private Author author;
	
	private Boolean available = true;
	
	@OneToMany
	@JoinColumn(name = "book_id")
	private List<Reservation> reservations = new ArrayList<>();
	
	@OneToMany
	@JoinColumn(name = "book_id")
	private List<Loan> loans = new ArrayList<>();
	
	public Book() {
		
	}
	
	public Book(String title, Integer pages, Genre genre, Author author, Boolean available) {
		this.title = title;
		this.pages = pages;
		this.genre = genre;
		this.author = author;
		this.available = available;
	}
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getPages() {
		return pages;
	}

	public void setPages(Integer pages) {
		this.pages = pages;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}
	
	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
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
		Book other = (Book) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Title=" + title + ", pages=" + pages + ", genrer=" + genre.getName() + ", author=" + author.getName();
	}
}

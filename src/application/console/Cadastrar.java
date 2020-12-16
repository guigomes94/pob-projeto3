package application.console;

import java.time.LocalDate;

import facade.Facade;
import model.Author;
import model.Book;
import model.Genre;
import model.User;

public class Cadastrar {

	public Cadastrar() {
		Facade.inicializar();
		cadastrar();
		Facade.finalizar();
	}

	public void cadastrar() {
		try {

			System.out.println("Cadastrando autores...");

			Author author1 = Facade.saveOrUpdateAuthor("Robert C. Martin");
			Author author2 = Facade.saveOrUpdateAuthor("Martin Fowler");
			Author author3 = Facade.saveOrUpdateAuthor("Dan Brown");
			Author author4 = Facade.saveOrUpdateAuthor("Andrew S. Tanenbaum");
			Author author5 = Facade.saveOrUpdateAuthor("Léo Lins");

			System.out.println("\nCadastrando generos...");

			Genre genre1 = Facade.saveOrUpdateGenre("Comedy");
			Genre genre2 = Facade.saveOrUpdateGenre("Fiction");
			Genre genre3 = Facade.saveOrUpdateGenre("Technology");			 
			
			System.out.println("\nCadastrando usuários...");
			
			User u1 = Facade.saveOrUpdateUser("João Pessoa", "joao@gmail.com", "987882222");
			User u2 = Facade.saveOrUpdateUser("Naruto Uzumaki", "naruto@gmail.com", "988883333");
			User u3 = Facade.saveOrUpdateUser("Sanji Vinsmoke", "sanji@gmail.com", "999887755");
			
			
			System.out.println("\nCadastrando livros...");

			Book b1 = Facade.saveOrUpdateBook("O Livro dos Insultos", 127, genre1, author5);
			Book b2 = Facade.saveOrUpdateBook("Inferno", 380, genre2, author3);
			Book b3 = Facade.saveOrUpdateBook("O Código da Vinci", 432, genre2, author3);
			Book b4 = Facade.saveOrUpdateBook("Clean Code", 447, genre3, author1);
			Book b5 = Facade.saveOrUpdateBook("Refatoração", 456, genre3, author2);
			Book b6 = Facade.saveOrUpdateBook("Sistemas Operacionais Modernos", 864, genre3, author4);
			
			LocalDate d1 = LocalDate.of(2020, 12, 20);
			LocalDate d2 = LocalDate.of(2020, 12, 14);
			LocalDate d3 = LocalDate.of(2020, 12, 8);
			LocalDate d4 = LocalDate.of(2020, 12, 12);
			LocalDate d5 = LocalDate.of(2020, 12, 15);
			LocalDate d6 = LocalDate.of(2020, 12, 20);
			LocalDate d7 = LocalDate.of(2020, 12, 21);
			LocalDate d8 = LocalDate.of(2020, 12, 30);
			
			System.out.println("\nCadastrando reservas...");
			Facade.saveOrUpdateReservation(null, d1, u1, b1);
			Facade.saveOrUpdateReservation(null, d6, u3, b2);
			
			System.out.println("\nCadastrando empréstimos...");
			Facade.saveOrUpdateLoan(null, d3, u2, b5, d5);
			Facade.saveOrUpdateLoan(null, d2, u3, b3, d7);
			Facade.saveOrUpdateLoan(null, d4, u1, b6, d6);
			Facade.saveOrUpdateLoan(null, d5, u1, b4, d8);

			System.out.println("\nCadastro inicial realizado com sucesso!");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// =================================================
	public static void main(String[] args) {
		new Cadastrar();
	}
}

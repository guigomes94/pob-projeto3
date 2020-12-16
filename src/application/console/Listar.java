package application.console;

import facade.Facade;

public class Listar {
	
	public Listar(){
		Facade.inicializar();
		try {
			System.out.println("Listar Usuários:");
			Facade.listUsers().stream().forEach(System.out::println);
			
			System.out.println("\nListar Autores:");
			Facade.listAuthors().stream().forEach(System.out::println);
			
			System.out.println("\nListar Livros:");
			Facade.listBooks().stream().forEach(System.out::println);
			
			System.out.println("\nListar Gêneros:");
			Facade.listGenres().stream().forEach(System.out::println);

			System.out.println("\nListar Reservas:");
			Facade.listReservations().stream().forEach(System.out::println);

			System.out.println("\nListar Empréstimos:");
			Facade.listLoans().stream().forEach(System.out::println);			 			 
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		Facade.finalizar();
		System.out.println("\nFim das listagens");
	}


	//=================================================
	public static void main(String[] args) {
		new Listar();
	}

}

package application.console;

import facade.Facade;

public class Consultar {

	public Consultar() {
		Facade.inicializar();
		try {

			System.out.println("Consulta de livros por título: ");
			Facade.searchBooksByTitle("code").stream().forEach(System.out::println);

			System.out.println("\nConsulta de livros pelo autor: ");
			Facade.searchBooksByAuthor("brown").stream().forEach(System.out::println);

			System.out.println("\nConsulta de livros por gênero: ");
			Facade.searchBooksByGenre("comedy").stream().forEach(System.out::println);

			System.out.println("\nConsultar livros disponíveis:");
			Facade.listBooksAvailable().stream().forEach(System.out::println);

			System.out.println("\nConsulta de usuários pelo nome:");
			Facade.searchUsersByName("sanji").stream().forEach(System.out::println);

			System.out.println("\nConsulta de usuários pelo email:");
			Facade.searchUsersByEmail("NARUTO").stream().forEach(System.out::println);

			
			System.out.println("\nBusca de reserva por id:");
			System.out.println(Facade.searchReservationById("5FDA353F1B3C3D5BF63D8FB4"));
			
			System.out.println("\nBusca de empréstimo por id:");
			System.out.println(Facade.searchLoanById("5FDA353F1B3C3D5BF63D8FB7"));
			 

			System.out.println("\nConsulta dos últimos empréstimos:");
			Facade.listLastLoans().stream().forEach(System.out::println);

			System.out.println("\nConsulta das próximas devoluções:");
			Facade.listNextDevolutions().stream().forEach(System.out::println);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		Facade.finalizar();
		System.out.println("\nFim das consultas!");
	}

	// =================================================
	public static void main(String[] args) {
		new Consultar();
	}
}

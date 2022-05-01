package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Category;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {

	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";
	private static final String USER = "student";
	private static final String PWD = "student";

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Integer> searchFilm(String searchTerm, DatabaseAccessor db) {
		String sql = "SELECT film.id FROM film WHERE (film.title LIKE ? OR film.description LIKE ?)";
		try (Connection conn = DriverManager.getConnection(URL, USER, PWD);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			ps.setString(1, "%" + searchTerm + "%");
			ps.setString(2, "%" + searchTerm + "%");
			ResultSet rs = ps.executeQuery();

				ArrayList<Integer> filmIdArr = new ArrayList<>();
				while (rs.next()) {
					filmIdArr.add(rs.getInt("film.id"));
				}
			rs.close();
			return filmIdArr;
		} catch (SQLException e) {
			System.err.println("The application has encountered a SQL Exception.");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Film findFilmById(int filmId) {
		String sql = "SELECT film.id, film.title, film.description, film.release_year, language.name, film.rental_duration, film.rental_rate, film.length, film.replacement_cost, film.rating, film.special_features FROM film JOIN language ON language.id = film.language_id WHERE film.id = ?";
		try (Connection conn = DriverManager.getConnection(URL, USER, PWD);
				PreparedStatement ps = conn.prepareStatement(sql);)

		{
			ps.setInt(1, filmId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Film film = new Film(rs.getInt("film.id"), rs.getString("film.title"), rs.getString("film.description"),
						rs.getInt("film.release_year"), rs.getString("language.name"), rs.getString("rental_duration"),
						rs.getDouble("film.rental_rate"), rs.getString("film.length"),
						rs.getDouble("film.replacement_cost"), rs.getString("film.rating"),
						rs.getString("film.special_features"), findActorsByFilmId(filmId),
						findCategoriesByFilmId(filmId));
				return film;
			}
			rs.close();
		} catch (SQLException e) {
			System.err.println("The application has encountered a SQL Exception.");
			e.printStackTrace();
		}

		return null;
	}

	public List<Actor> findActorsByFilmId(int filmId) {
		List<Actor> actorList = new ArrayList<>();

		String sql = "SELECT actor.id, actor.first_name, actor.last_name FROM actor JOIN film_actor ON film_actor.actor_id = actor.id JOIN film ON film.id = film_actor.film_id WHERE film.id = ?";
		try (Connection conn = DriverManager.getConnection(URL, USER, PWD);
				PreparedStatement ps = conn.prepareStatement(sql);)

		{
			ps.setInt(1, filmId);
			ResultSet rs = ps.executeQuery();
			actorList = new ArrayList<>();
			while (rs.next()) {
				Actor actor = new Actor(rs.getInt("actor.id"), rs.getString("actor.first_name"),
						rs.getString("actor.last_name"));
				actorList.add(actor);
			}
			rs.close();
		} catch (SQLException e) {
			System.err.println("The application has encountered a SQL Exception.");
			e.printStackTrace();
		}
		return actorList;
	}

	public List<Category> findCategoriesByFilmId(int filmId) {
		List<Category> categoryList = new ArrayList<>();

		String sql = "SELECT film.id, category.name FROM category JOIN film_category ON film_category.category_id = category.id JOIN film ON film.id = film_category.film_id WHERE film.id = ?";
		try (Connection conn = DriverManager.getConnection(URL, USER, PWD);
				PreparedStatement ps = conn.prepareStatement(sql);)

		{
			ps.setInt(1, filmId);
			ResultSet rs = ps.executeQuery();
			categoryList = new ArrayList<>();
			while (rs.next()) {
				Category category = new Category(rs.getInt("film.id"), rs.getString("category.name"));
				categoryList.add(category);
			}
			rs.close();
		} catch (SQLException e) {
			System.err.println("The application has encountered a SQL Exception.");
			e.printStackTrace();
		}
		return categoryList;
	}

	public Actor findActorById(int actorId) {

		String sql = "SELECT actor.id, actor.first_name, actor.last_name FROM actor WHERE actor.id = ?";
		try (Connection conn = DriverManager.getConnection(URL, USER, PWD);
				PreparedStatement ps = conn.prepareStatement(sql);)

		{
			ps.setInt(1, actorId); 
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Actor actor = new Actor(rs.getInt("actor.id"), rs.getString("actor.first_name"),
						rs.getString("actor.last_name"));
				return actor;
			}
			rs.close();
		} catch (SQLException e) {
			System.err.println("The application has encountered a SQL Exception.");
			e.printStackTrace();
		}

		return null;
	}

}
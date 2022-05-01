package com.skilldistillery.filmquery.database;

import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Category;
import com.skilldistillery.filmquery.entities.Film;

public interface DatabaseAccessor {
  public Film findFilmById(int filmId);
  public Actor findActorById(int actorId);
  public List<Actor> findActorsByFilmId(int filmId);
  public ArrayList<Integer> searchFilm(String searchTerm, DatabaseAccessor db);
  public List<Category> findCategoriesByFilmId(int filmId);

}
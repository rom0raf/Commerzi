package com.commerzi.commerziapi.dao;

import com.commerzi.commerziapi.model.CommerziUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Classe temporaire avec seulement des users en mémoire
 * TODO: utiliser une base de données
 */
@Repository
public interface UserRepository extends CrudRepository<CommerziUser, String> {

    CommerziUser getUserByEmail(String email);

    CommerziUser getUserBySession(String session);

}
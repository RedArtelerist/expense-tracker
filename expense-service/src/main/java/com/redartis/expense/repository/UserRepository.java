package com.redartis.expense.repository;

import com.redartis.expense.model.User;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("""
            SELECT u FROM User u
            LEFT JOIN FETCH u.account a
            LEFT JOIN FETCH a.categories c
            LEFT JOIN fetch c.keywords
            WHERE u.id = :id"""
    )
    Optional<User> findByIdWithAccount(Long id);

    @Query("SELECT u.id FROM User u")
    Set<Long> getAllUserIds();

    @Query("SELECT u FROM User u WHERE u.id IN (:ids)")
    List<User> findAllUsersByIds(List<Long> ids);

    @Modifying
    @Query("""
            UPDATE User u SET u.username = :username, u.firstName = :firstName,
            u.lastName = :lastName, u.photoUrl = :photoUrl, u.authDate = :authDate
            WHERE u.id= :userId"""
    )
    void updateUserDetailsByUserId(
            Long userId,
            String username,
            String firstName,
            String lastName,
            String photoUrl,
            String authDate
    );
}

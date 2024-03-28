package csd226.lab2.repositories;

import csd226.lab2.data.Account;
import csd226.lab2.data.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);

    int deleteByAccount(Account account);
}

package repository;

import org.springframework.data.jpa.repository.JpaRepository;

import model.CreditCard;

public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {
}

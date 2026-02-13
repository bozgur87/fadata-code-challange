package eu.fadata.code.challege.repository;

import eu.fadata.code.challege.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

	Item save(Item item);

	Optional<Item> findById(Long id);

	List<Item> findAll();

	void deleteAll();
}

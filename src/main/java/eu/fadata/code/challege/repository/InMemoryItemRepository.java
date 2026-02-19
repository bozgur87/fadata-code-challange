package eu.fadata.code.challege.repository;

import eu.fadata.code.challege.model.Item;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory repository. As a singleton bean, state is shared between tests;
 * if the context is not reset, tests can affect each other.
 */
@Repository
public class InMemoryItemRepository implements ItemRepository {

	private final List<Item> store = new ArrayList<>();
	private final AtomicLong idGenerator = new AtomicLong(1);

	@Override
	public Item save(Item item) {
		if (item.getId() == null) {
			item.setId(idGenerator.getAndIncrement());
			item.setCreatedAt(java.time.Instant.now());
			store.add(item);
			return item;
		}
		store.removeIf(i -> i.getId().equals(item.getId()));
		store.add(item);
		return item;
	}

	@Override
	public Optional<Item> findById(Long id) {
		return store.stream().filter(i -> i.getId().equals(id)).findFirst();
	}

	@Override
	public List<Item> findAll() {
		return new ArrayList<>(store);
	}

	@Override
	public void deleteAll() {
		store.clear();
		idGenerator.set(1);
	}
}

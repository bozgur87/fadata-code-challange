package eu.fadata.code.challege.service;

import eu.fadata.code.challege.model.Item;
import eu.fadata.code.challege.model.ItemResponse;
import eu.fadata.code.challege.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ItemService {

	private final ItemRepository itemRepository;

	public ItemService(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	public ItemResponse create(String name) {
		Item item = new Item(null, name, null);
		Item saved = itemRepository.save(item);
		return toResponse(saved);
	}

	public List<ItemResponse> searchByName(String keyword) {
		// TODO: filter items from repository by name (case-insensitive) and return results
		return List.of();
	}

	public List<ItemResponse> findAll(String sort) {
		Stream<Item> stream = itemRepository.findAll().stream();
		if ("name_asc".equals(sort)) {
			stream = stream.sorted(Comparator.comparing(Item::getName));
		} else if ("created_desc".equals(sort)) {
			stream = stream.sorted(Comparator.comparing(Item::getCreatedAt).reversed());
		}
		return stream.map(this::toResponse).toList();
	}

	private ItemResponse toResponse(Item item) {
		return new ItemResponse(item.getId(), item.getName(), item.getCreatedAt());
	}
}

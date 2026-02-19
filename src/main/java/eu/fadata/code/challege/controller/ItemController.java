package eu.fadata.code.challege.controller;

import eu.fadata.code.challege.model.ItemResponse;
import eu.fadata.code.challege.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

	private final ItemService itemService;

	public ItemController(ItemService itemService) {
		this.itemService = itemService;
	}

	@PostMapping
	public ResponseEntity<ItemResponse> create(@RequestBody CreateItemRequest request) {
		ItemResponse created = itemService.create(request.title());
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@GetMapping("/search")
	public List<ItemResponse> searchByName(@RequestParam(name = "keyword") String keyword) {
		return itemService.searchByName(keyword);
	}

	@GetMapping
	public List<ItemResponse> list(@RequestParam(required = false) String xSort) {
		return itemService.findAll(xSort);
	}

	public record CreateItemRequest(String title) {
	}
}

package eu.fadata.code.challege.model;

import java.time.Instant;

/**
 * API response DTO for Item.
 * Note: For a test to expect the "id" field in the response, the API design
 * (field name or DTO) may need to be adjusted accordingly.
 */
public class ItemResponse {

	private Long id;
	private String name;
	private Instant createdAt;

	public ItemResponse() {
	}

	public ItemResponse(Long id, String name, Instant createdAt) {
		this.id = id;
		this.name = name;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
}

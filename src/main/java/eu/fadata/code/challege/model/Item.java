package eu.fadata.code.challege.model;

import java.time.Instant;

public class Item {

	private Long id;
	private String name;
	private Instant createdAt;

	public Item() {
	}

	public Item(Long id, String name, Instant createdAt) {
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

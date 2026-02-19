package eu.fadata.code.challege.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.fadata.code.challege.repository.ItemRepository;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ItemRepository itemRepository;

	@Test
	void getItemsReturnsEmptyWhenNoItemsAdded() throws Exception {
		mockMvc.perform(get("/api/items"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", empty()));
	}

	@Test
	void createItemAndGetList() throws Exception {
		mockMvc.perform(post("/api/items")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Test Item\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.name", is("Test Item")));

		mockMvc.perform(get("/api/items"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", is(1)));
	}


	@Test
	void createAcceptsNameField() throws Exception {
		mockMvc.perform(post("/api/items")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"From Name Field\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name", is("From Name Field")));
	}

	@Test
	void getItemsSortedByNameAsc() throws Exception {
		createItemViaApi("Banana");
		createItemViaApi("Apple");

		mockMvc.perform(get("/api/items").param("sort", "name_asc"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", greaterThanOrEqualTo(2)))
				.andExpect(jsonPath("$[0].name", is("Apple")))
				.andExpect(jsonPath("$[1].name", is("Banana")));
	}

	@Test
	void getItemsSortedByCreatedDesc() throws Exception {
		createItemViaApi("First");
		createItemViaApi("Second");

		mockMvc.perform(get("/api/items").param("sort", "created_desc"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", greaterThanOrEqualTo(2)))
				.andExpect(jsonPath("$[0].name", is("Second")))
				.andExpect(jsonPath("$[1].name", is("First")));
	}

	private void createItemViaApi(String name) throws Exception {
		mockMvc.perform(post("/api/items")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(Map.of("name", name))));
	}
}

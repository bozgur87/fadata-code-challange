package eu.fadata.code.challege.controller;

import eu.fadata.code.challege.model.ItemResponse;
import eu.fadata.code.challege.service.ItemService;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ItemService itemService;

	@Test
	void create_returns201AndBody() throws Exception {
		ItemResponse response = new ItemResponse(1L, "Test", Instant.now());
		when(itemService.create("Test")).thenReturn(response);

		mockMvc.perform(post("/api/items")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Test\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("Test")));

		verify(itemService).create(eq("Test"));
	}

	@Test
	void list_returns200AndList() throws Exception {
		when(itemService.findAll(null)).thenReturn(List.of());

		mockMvc.perform(get("/api/items"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", empty()));
	}

	@Test
	void list_withSortParam_callsServiceWithSort() throws Exception {
		when(itemService.findAll("name_asc")).thenReturn(List.of());

		mockMvc.perform(get("/api/items").param("sort", "name_asc"))
				.andExpect(status().isOk());

		verify(itemService).findAll(eq("name_asc"));
	}
}

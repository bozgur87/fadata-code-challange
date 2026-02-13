package eu.fadata.code.challege.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.fadata.code.challege.repository.ItemRepository;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Code Challenge – Adayın çözmesi beklenen senaryolar:
 *
 * 1) CONTEXT TEMİZLİĞİ: Testler arasında Spring context / repository state temizlenmiyor.
 *    Bir test veri eklediğinde diğer test boş liste bekleyebilir ve fail eder.
 *    Aday: @DirtiesContext veya @BeforeEach ile repository temizliği (veya uygun çözüm) kullanmalı.
 *
 * 2) API TASARIMI: Create endpoint'ine test "name" alanı ile istek gönderiyor; controller şu an "title" bekliyor.
 *    Aday: API'yi testin beklediği sözleşmeye göre düzeltmeli (request body'de "name" kabul edilmeli).
 *
 * 3) ÇAKIŞAN İKİ SENARYO: İki test farklı sıralama bekliyor (biri isme göre artan, biri oluşturulma tarihine göre azalan).
 *    Sadece bir sıralama uygulanırsa biri geçer biri fail eder. Aday: Her iki testin de geçmesi için
 *    API'de sort parametresi kullanıp testlerde doğru parametreyi (sort=name_asc / sort=created_desc) kullanmalı.
 */
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

	// ---------- Senaryo 2: API tasarımı ----------
	// Test "name" alanı ile POST yapıyor. Controller şu an "title" kullanıyor; istek başarısız veya name null döner.
	// Aday: CreateItemRequest ve controller'ı "name" kabul edecek şekilde değiştirmeli.

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

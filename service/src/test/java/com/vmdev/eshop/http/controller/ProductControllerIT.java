package com.vmdev.eshop.http.controller;

import com.vmdev.eshop.repository.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RequiredArgsConstructor
@AutoConfigureMockMvc
@WithMockUser(username = "test@gmail.com", password = "test", authorities = {"ADMIN", "USER"})
class ProductControllerIT extends IntegrationTestBase {

    private final MockMvc mockMvc;

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("product/products"))
                .andExpect(model().attributeExists("products"));
    }

    @Test
    void findById() throws Exception {
        mockMvc.perform(get("/products/1"))
                .andExpectAll(
                        status().is2xxSuccessful(),
                        view().name("product/product"),
                        model().attributeExists("product"),
                        model().attributeExists("types"),
                        model().attributeExists("reviews")
                );
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post("/products")
                        .param("name", "printer")
                        .param("descriptions", "test")
                        .param("cost", "12500")
                        .param("quantity", "5")
                        .param("type", "OFFICE")
                        .param("manufacturer", "HP")
                        .param("image", ""))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern("/products/*")
                );
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(post("/products/2/update")
                        .param("name", "printer")
                        .param("descriptions", "test")
                        .param("cost", "12500")
                        .param("quantity", "5")
                        .param("type", "OFFICE")
                        .param("manufacturer", "HP"))
                .andExpectAll(
                        status().is3xxRedirection()
                );
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(post("/products/3/delete")
                        .param("id", "3"))
                .andExpectAll(
                        status().is3xxRedirection()
                );
    }
}
package com.vmdev.eshop.service;

import com.vmdev.eshop.dto.ClientCreateEditDto;
import com.vmdev.eshop.dto.ClientOrderDto;
import com.vmdev.eshop.dto.ClientReadDto;
import com.vmdev.eshop.dto.OrderProductDto;
import com.vmdev.eshop.dto.ProductCreateEditDto;
import com.vmdev.eshop.dto.ProductReadDto;
import com.vmdev.eshop.entity.enums.ProductType;
import com.vmdev.eshop.entity.enums.Role;
import com.vmdev.eshop.repository.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class OrderProductServiceTest extends IntegrationTestBase {

    private final OrderProductService orderProductService;
    private final ProductService productService;
    private final ClientOrderService clientOrderService;
    private final ClientService clientService;

    @Test
    void incrementByClientAndOrder() {
        ProductReadDto product = getProduct();
        ClientOrderDto order = getClientOrder();
        orderProductService.incrementOrCreateByClientAndOrder(order.getId(), product.getId());


        OrderProductDto actualResult = orderProductService.incrementOrCreateByClientAndOrder(order.getId(), product.getId());
        assertThat(actualResult.getQuantity()).isEqualTo(2);
    }

    @Test
    void createByClientAndOrder() {
        ProductReadDto product = getProduct();
        ClientOrderDto order = getClientOrder();

        OrderProductDto actualResult = orderProductService.incrementOrCreateByClientAndOrder(order.getId(), product.getId());
        assertThat(actualResult.getId()).isNotNull();
    }

    @Test
    void decrementByClientAndOrder() {
        ProductReadDto product = getProduct();
        ClientOrderDto order = getClientOrder();
        orderProductService.incrementOrCreateByClientAndOrder(order.getId(), product.getId());


        OrderProductDto actualResult = orderProductService.decrementByClientAndOrder(order.getId(), product.getId());
        assertThat(actualResult.getQuantity()).isEqualTo(0);
    }

    @Test
    void create() {
        ClientOrderDto order = getClientOrder();
        ProductReadDto product = getProduct();
        OrderProductDto actualResult = orderProductService.create(order.getId(), product.getId());

        assertThat(actualResult.getId()).isNotNull();
    }

    private ClientOrderDto getClientOrder() {
        ClientReadDto user = clientService.create(new ClientCreateEditDto("test", "test", "test@gmail.com", "test",
                "89037539183", "test", Role.ADMIN));
        return clientOrderService.create(user.getId());
    }

    private ProductReadDto getProduct() {
        return productService.create(ProductCreateEditDto.builder()
                .name("test")
                .description("printer")
                .cost(12500)
                .type(ProductType.OFFICE)
                .quantity(15)
                .manufacturer("HP")
                .image(new MockMultipartFile("test", new byte[0]))
                .build());
    }
}
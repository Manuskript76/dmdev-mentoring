package com.vmdev.eshop.util;

import com.vmdev.eshop.entity.Client;
import com.vmdev.eshop.entity.ClientOrder;
import com.vmdev.eshop.entity.OrderProduct;
import com.vmdev.eshop.entity.Product;
import com.vmdev.eshop.entity.Review;
import com.vmdev.eshop.entity.enums.OrderStatus;
import com.vmdev.eshop.entity.enums.ProductType;
import com.vmdev.eshop.entity.enums.ReviewGrade;
import com.vmdev.eshop.entity.enums.Role;
import jakarta.persistence.EntityManager;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;

@UtilityClass
public class TestDataImporter {

    public static void importData(EntityManager entityManager) {

        Client ivan = Client.builder()
                .firstname("Ivan")
                .lastname("Ivanov")
                .email("vanya@gmail.com")
                .password("vann99")
                .phone("+79034219402")
                .address("Moscow")
                .role(Role.USER)
                .build();

        Client petr = Client.builder()
                .firstname("Petr")
                .lastname("Petrov")
                .email("petya@gmail.com")
                .password("PetrTheFirst")
                .phone("+79037630148")
                .address("Gelenjik")
                .role(Role.USER)
                .build();

        Client sveta = Client.builder()
                .firstname("Sveta")
                .lastname("Svetova")
                .email("svetik@gmail.com")
                .password("byTheHolyLight")
                .phone("+79035716827")
                .address("SPB")
                .role(Role.ADMIN)
                .build();

        Client andrey = Client.builder()
                .firstname("Andrey")
                .lastname("Andreev")
                .email("drus@gmail.com")
                .password("qwer123")
                .phone("+79033079405")
                .address("Vatikan")
                .role(Role.ADMIN)
                .build();

        Client katya = Client.builder()
                .firstname("Ekaterina")
                .lastname("Afanaseva")
                .email("kate@gmail.com")
                .password("catAfun2004")
                .phone("+79039716281")
                .address("Samara")
                .role(Role.USER)
                .build();

        Product monitor = Product.builder()
                .name("HDR12500D")
                .description("144Hz Monitor Full HD")
                .type(ProductType.TV)
                .manufacturer("GIGABYTE")
                .cost(19900)
                .quantity(10)
                .build();

        Product tv = Product.builder()
                .name("32HEN1")
                .description("LED TV 24`")
                .type(ProductType.TV)
                .manufacturer("SAMSUNG")
                .cost(9200)
                .quantity(25)
                .build();

        Product msiLaptop = Product.builder()
                .name("I2P8")
                .description("Gaming laptop")
                .type(ProductType.LAPTOPS)
                .manufacturer("MSI")
                .cost(79000)
                .quantity(5)
                .build();

        Product acerLaptop = Product.builder()
                .name("X12Omega3")
                .description("Laptop i5 4090 15`")
                .type(ProductType.LAPTOPS)
                .manufacturer("ACER")
                .cost(150000)
                .quantity(9)
                .build();

        Product iPhone13 = Product.builder()
                .name("IPhone13 Pro")
                .description("IPhone 13 Pro EU")
                .type(ProductType.PHONES)
                .manufacturer("APPLE")
                .cost(76000)
                .quantity(13)
                .build();

        Product huawei = Product.builder()
                .name("A13P100")
                .description("Huawei android 13 15mp")
                .type(ProductType.PHONES)
                .manufacturer("HUAWEI")
                .cost(30000)
                .quantity(20)
                .build();

        Product printer = Product.builder()
                .name("13PR_01LA")
                .description("Printer 100 print/min")
                .type(ProductType.OFFICE)
                .manufacturer("HP")
                .cost(17600)
                .quantity(4)
                .build();

        Product scanner = Product.builder()
                .name("MBR-pr15D")
                .description("Multifunctional office scanner")
                .type(ProductType.OFFICE)
                .manufacturer("PHILIPS")
                .cost(9500)
                .quantity(14)
                .build();

        Product coffeeMachine = Product.builder()
                .name("CFiP1.4Coffee")
                .description("Coffee Machine 10L")
                .type(ProductType.APPLIANCES)
                .manufacturer("DELONGI")
                .cost(42000)
                .quantity(43)
                .build();

        Product vacuumCleaner = Product.builder()
                .name("PVC 2004RI")
                .description("2000W 2.5L vacuum cleaner")
                .type(ProductType.APPLIANCES)
                .manufacturer("POLARIS")
                .cost(19900)
                .quantity(10)
                .build();

        ClientOrder petrOrder1 = ClientOrder.builder()
                .client(petr)
                .openDate(LocalDate.now())
                .status(OrderStatus.IN_PROGRESS)
                .build();

        ClientOrder petrOrder2 = ClientOrder.builder()
                .client(petr)
                .openDate(LocalDate.of(2024, 1, 12))
                .closeDate(LocalDate.now())
                .status(OrderStatus.COMPLETED)
                .build();

        ClientOrder ivanOrder = ClientOrder.builder()
                .client(ivan)
                .openDate(LocalDate.now())
                .status(OrderStatus.IN_PROGRESS)
                .build();

        ClientOrder svetaOrder = ClientOrder.builder()
                .client(sveta)
                .openDate(LocalDate.now())
                .status(OrderStatus.IN_PROGRESS)
                .build();

        ClientOrder katyaOrder = ClientOrder.builder()
                .client(katya)
                .openDate(LocalDate.now())
                .status(OrderStatus.IN_PROGRESS)
                .build();

        OrderProduct petr1Tv = OrderProduct.builder()
                .product(tv)
                .clientOrder(petrOrder1)
                .quantity(1)
                .build();

        OrderProduct petr1Scanner = OrderProduct.builder()
                .product(scanner)
                .clientOrder(petrOrder1)
                .quantity(1)
                .build();

        OrderProduct petr2Printer = OrderProduct.builder()
                .product(printer)
                .clientOrder(petrOrder2)
                .quantity(2)
                .build();

        OrderProduct petr2Laptop = OrderProduct.builder()
                .product(msiLaptop)
                .clientOrder(petrOrder2)
                .quantity(2)
                .build();

        OrderProduct ivanCoffee = OrderProduct.builder()
                .product(coffeeMachine)
                .clientOrder(ivanOrder)
                .quantity(1)
                .build();

        OrderProduct ivanMonitor = OrderProduct.builder()
                .product(monitor)
                .clientOrder(ivanOrder)
                .quantity(2)
                .build();

        OrderProduct ivanLaptop = OrderProduct.builder()
                .product(acerLaptop)
                .clientOrder(ivanOrder)
                .quantity(1)
                .build();

        OrderProduct svetaIphone = OrderProduct.builder()
                .product(iPhone13)
                .clientOrder(svetaOrder)
                .quantity(1)
                .build();

        OrderProduct svetaMonitor = OrderProduct.builder()
                .product(monitor)
                .clientOrder(svetaOrder)
                .quantity(1)
                .build();

        Review ivanLaptopReview = Review.builder()
                .grade(ReviewGrade.GOOD)
                .date(LocalDate.now())
                .review("Very good laptop za svoi money!")
                .build();

        Review ivanMonitorReview = Review.builder()
                .grade(ReviewGrade.BAD)
                .date(LocalDate.now())
                .review("bad quality")
                .build();

        Review svetaMonitorReview = Review.builder()
                .grade(ReviewGrade.EXCELLENT)
                .date(LocalDate.now())
                .review("Very good!!!")
                .build();

        Review petrLaptopReview = Review.builder()
                .grade(ReviewGrade.GOOD)
                .date(LocalDate.now())
                .review("good lap, but expensive")
                .build();

        entityManager.persist(ivan);
        entityManager.persist(petr);
        entityManager.persist(sveta);
        entityManager.persist(katya);
        entityManager.persist(andrey);

        entityManager.persist(monitor);
        entityManager.persist(tv);
        entityManager.persist(iPhone13);
        entityManager.persist(huawei);
        entityManager.persist(vacuumCleaner);
        entityManager.persist(coffeeMachine);
        entityManager.persist(acerLaptop);
        entityManager.persist(msiLaptop);
        entityManager.persist(printer);
        entityManager.persist(scanner);

        entityManager.persist(ivanOrder);
        entityManager.persist(katyaOrder);
        entityManager.persist(petrOrder1);
        entityManager.persist(petrOrder2);
        entityManager.persist(svetaOrder);

        entityManager.persist(petr1Tv);
        entityManager.persist(petr1Scanner);
        entityManager.persist(petr2Printer);
        entityManager.persist(petr2Laptop);
        entityManager.persist(ivanCoffee);
        entityManager.persist(ivanMonitor);
        entityManager.persist(ivanLaptop);
        entityManager.persist(svetaIphone);
        entityManager.persist(svetaMonitor);

        entityManager.persist(ivanLaptopReview);
        entityManager.persist(ivanMonitorReview);
        entityManager.persist(petrLaptopReview);
        entityManager.persist(svetaMonitorReview);

        ivanOrder.addProduct(ivanCoffee);
        ivanOrder.addProduct(ivanLaptop);
        ivanOrder.addProduct(ivanMonitor);

        petrOrder1.addProduct(petr1Tv);
        petrOrder1.addProduct(petr1Scanner);

        petrOrder2.addProduct(petr2Laptop);
        petrOrder2.addProduct(petr2Printer);

        svetaOrder.addProduct(svetaIphone);
        svetaOrder.addProduct(svetaMonitor);

        ivanLaptopReview.setProduct(acerLaptop);
        ivanLaptopReview.setClient(ivan);

        ivanMonitorReview.setProduct(monitor);
        ivanMonitorReview.setClient(ivan);

        svetaMonitorReview.setProduct(monitor);
        svetaMonitorReview.setClient(sveta);

        petrLaptopReview.setProduct(msiLaptop);
        petrLaptopReview.setClient(petr);
    }
}


package com.vmdev.eshop.util;

import com.vmdev.eshop.entity.Client;
import com.vmdev.eshop.entity.ClientOrder;
import com.vmdev.eshop.entity.OrderProduct;
import com.vmdev.eshop.entity.Product;
import com.vmdev.eshop.entity.Review;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateUtil {

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = buildConfiguration();
        configuration.configure();

        return configuration.buildSessionFactory();
    }

    public static Configuration buildConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.addAnnotatedClass(Client.class);
        configuration.addAnnotatedClass(ClientOrder.class);
        configuration.addAnnotatedClass(OrderProduct.class);
        configuration.addAnnotatedClass(Product.class);
        configuration.addAnnotatedClass(Review.class);
        return configuration;
    }
}
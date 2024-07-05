package com.sprinklr.neo4j;

import com.sprinklr.neo4j.entities.CustomerEntity;
import com.sprinklr.neo4j.entities.EmployeeEntity;

import com.sprinklr.neo4j.entities.OrderEntity;
import com.sprinklr.neo4j.entities.ProductEntity;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class PerformantTest {

    @Autowired
    private Neo4jWrapper neo4jWrapper;

    @Setter
    @Getter
    static class OrderDetail {
        private CustomerEntity customerEntity;
        private String customerID;
        private String contactName;
        private String orderID;
        private String orderDate;
        private String productName;
        private Long quantity;
    }

    @Test
    public void complexQueryTest() {
        String query = """
        MATCH (c:Customer {customerID: "ALFKI"})-[:PURCHASED]->(o:Order)-[:ORDERS]->(p:Product)
        RETURN c as customerEntity, c.customerID AS customerID, c.contactName AS contactName, o.orderID AS orderID, o.orderDate AS orderDate, p.productName AS productName""";

        List<OrderDetail> ALFKIOrders = neo4jWrapper.executeQuery(query, OrderDetail.class);

        for (OrderDetail orderDetail: ALFKIOrders) {
            System.out.println(orderDetail.getContactName());
            System.out.println(orderDetail.getProductName());
        }
    }

    //    db.customers.aggregate([
    //    { $match: { CustomerID: "ALFKI" } },
    //    { $lookup: {
    //        from: "orders",
    //                localField: "CustomerID",
    //                foreignField: "CustomerID",
    //                as: "orders"
    //    }
    //    },
    //    { $unwind: "$orders" },
    //    { $lookup: {
    //        from: "order_details",
    //                localField: "orders.OrderID",
    //                foreignField: "OrderID",
    //                as: "order_details"
    //    }
    //    },
    //    { $unwind: "$order_details" },
    //    { $lookup: {
    //        from: "products",
    //                localField: "order_details.ProductID",
    //                foreignField: "ProductID",
    //                as: "product_info"
    //    }
    //    },
    //    { $unwind: "$product_info" },
    //    { $project: {
    //        _id: 0,
    //                CustomerID: 1,
    //                ContactName: 1,
    //                OrderID: "$orders.OrderID",
    //                OrderDate: "$orders.OrderDate",
    //                ProductName: "$product_info.ProductName",
    //                Quantity: "$order_details.Quantity"
    //    }
    //    }
    //]);

}

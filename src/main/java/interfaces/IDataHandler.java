package interfaces;

import model.Order;

import java.io.IOException;

public interface IDataHandler {
    void writeOrderWithProducts(Order order);
    void writeOrderWithService(Order order);
    void close() throws IOException;
}

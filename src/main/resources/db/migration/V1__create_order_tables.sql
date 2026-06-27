CREATE TABLE orders (
                        id UUID PRIMARY KEY,
                        customer_id UUID NOT NULL,

                        status VARCHAR(40) NOT NULL,

                        total_amount NUMERIC(19, 2) NOT NULL,

                        version BIGINT NOT NULL DEFAULT 0,

                        created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                        updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

                        CONSTRAINT chk_orders_total_amount
                            CHECK (total_amount >= 0),

                        CONSTRAINT chk_orders_status
                            CHECK (
                                status IN (
                                           'CREATED',
                                           'AWAITING_STOCK',
                                           'STOCK_RESERVED',
                                           'AWAITING_PAYMENT',
                                           'PAID',
                                           'PAYMENT_FAILED',
                                           'CANCELLED',
                                           'COMPLETED'
                                    )
                                )
);


CREATE TABLE order_items (
                             id UUID PRIMARY KEY,

                             order_id UUID NOT NULL,
                             product_id UUID NOT NULL,

                             product_name VARCHAR(150) NOT NULL,

                             unit_price NUMERIC(19, 2) NOT NULL,
                             quantity INTEGER NOT NULL,
                             subtotal NUMERIC(19, 2) NOT NULL,

                             created_at TIMESTAMP WITH TIME ZONE NOT NULL,

                             CONSTRAINT fk_order_items_order
                                 FOREIGN KEY (order_id)
                                     REFERENCES orders (id),

                             CONSTRAINT chk_order_items_unit_price
                                 CHECK (unit_price >= 0),

                             CONSTRAINT chk_order_items_quantity
                                 CHECK (quantity > 0),

                             CONSTRAINT chk_order_items_subtotal
                                 CHECK (subtotal >= 0)
);


CREATE TABLE order_status_history (
                                      id UUID PRIMARY KEY,

                                      order_id UUID NOT NULL,

                                      previous_status VARCHAR(40),
                                      new_status VARCHAR(40) NOT NULL,

                                      reason VARCHAR(255),

                                      correlation_id UUID,

                                      occurred_at TIMESTAMP WITH TIME ZONE NOT NULL,

                                      CONSTRAINT fk_order_status_history_order
                                          FOREIGN KEY (order_id)
                                              REFERENCES orders (id)
);


CREATE TABLE processed_events (
                                  event_id UUID PRIMARY KEY,

                                  event_type VARCHAR(120) NOT NULL,
                                  consumer_name VARCHAR(120) NOT NULL,

                                  processed_at TIMESTAMP WITH TIME ZONE NOT NULL
);


CREATE INDEX idx_orders_customer_id
    ON orders (customer_id);


CREATE INDEX idx_orders_status
    ON orders (status);


CREATE INDEX idx_order_items_order_id
    ON order_items (order_id);


CREATE INDEX idx_order_status_history_order_id
    ON order_status_history (order_id);


CREATE INDEX idx_order_status_history_correlation_id
    ON order_status_history (correlation_id);


CREATE INDEX idx_processed_events_type
    ON processed_events (event_type);
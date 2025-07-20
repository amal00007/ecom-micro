package com.ecommerce.order.clients;

import com.ecommerce.order.dto.UserResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import java.util.List;

@HttpExchange
public interface UserServiceClient {
    @GetExchange("/api/users/{id}")
    public List<UserResponse> getUser(@PathVariable String id);

}

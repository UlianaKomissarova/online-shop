package servlet;

import dto.OrderDto;
import exception.NotFoundException;
import service.OrderService;
import service.impl.OrderServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet(urlPatterns = {"/orders/*"})
public class OrderServlet extends AbstractServlet {
    private final OrderService orderService;

    public OrderServlet() {
        orderService = OrderServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);
        String responseAnswer;

        try {
            checkPath(req);
            String[] pathPart = getPathParts(req);

            if ("all".equals(pathPart[1])) {
                List<OrderDto> dtos = orderService.findAll();
                resp.setStatus(HttpServletResponse.SC_OK);
                responseAnswer = objectMapper.writeValueAsString(dtos);
            } else {
                Integer orderId = Integer.parseInt(pathPart[1]);
                OrderDto dto = orderService.findById(orderId);
                resp.setStatus(HttpServletResponse.SC_OK);
                responseAnswer = objectMapper.writeValueAsString(dto);
            }
        } catch (NotFoundException exception) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            responseAnswer = exception.getMessage();
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseAnswer = e.getMessage();
        }

        writeResponse(resp, responseAnswer);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);
        String responseAnswer = "";
        try {
            String[] pathPart = req.getPathInfo().split("/");
            Integer orderId = Integer.parseInt(pathPart[1]);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            orderService.delete(orderId);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseAnswer = e.getMessage();
        }

        writeResponse(resp, responseAnswer);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);
        String json = getJson(req);

        String responseAnswer;
        Optional<OrderDto> orderResponse;
        try {
            orderResponse = Optional.ofNullable(objectMapper.readValue(json, OrderDto.class));
            OrderDto order = orderResponse.orElseThrow(IllegalArgumentException::new);

            responseAnswer = objectMapper.writeValueAsString(orderService.save(order));
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseAnswer = e.getMessage();
        }

        writeResponse(resp, responseAnswer);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);
        String json = getJson(req);

        String responseAnswer = "";
        Optional<OrderDto> orderResponse;
        try {
            orderResponse = Optional.ofNullable(objectMapper.readValue(json, OrderDto.class));
            OrderDto orderUpdateDto = orderResponse.orElseThrow(IllegalArgumentException::new);
            orderService.update(orderUpdateDto);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseAnswer = e.getMessage();
        }

        writeResponse(resp, responseAnswer);
    }
}
package servlet;

import dto.OrderDto;
import service.OrderServiceInterface;
import service.impl.OrderService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet(urlPatterns = {"/orders/*"})
public class OrderServlet extends AbstractServlet {
    private final OrderServiceInterface service;

    public OrderServlet() {
        service = OrderService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);
        String responseAnswer;

        try {
            checkPath(req);
            String[] pathPart = getPathParts(req);

            if ("all".equals(pathPart[1])) {
                List<OrderDto> dtos = service.findAll();
                resp.setStatus(HttpServletResponse.SC_OK);
                responseAnswer = objectMapper.writeValueAsString(dtos);
            } else {
                Integer orderId = Integer.parseInt(pathPart[1]);
                OrderDto dto = service.findById(orderId);
                resp.setStatus(HttpServletResponse.SC_OK);
                responseAnswer = objectMapper.writeValueAsString(dto);
            }
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
            service.delete(orderId);
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

        Optional<OrderDto> orderResponse;
        try {
            orderResponse = Optional.ofNullable(objectMapper.readValue(json, OrderDto.class));
            OrderDto dto = orderResponse.orElseThrow(IllegalArgumentException::new);
            service.save(dto);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writeResponse(resp, e.getMessage());
        }

        resp.sendRedirect("orders/all");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);
        String json = getJson(req);

        String responseAnswer = "";
        Optional<OrderDto> orderResponse;
        try {
            orderResponse = Optional.ofNullable(objectMapper.readValue(json, OrderDto.class));
            OrderDto dto = orderResponse.orElseThrow(IllegalArgumentException::new);
            service.update(dto);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseAnswer = e.getMessage();
        }

        writeResponse(resp, responseAnswer);
    }
}
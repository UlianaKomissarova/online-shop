package servlet;

import dto.DeliveryDto;
import service.DeliveryServiceInterface;
import service.impl.DeliveryService;
import service.mapper.DeliveryFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/deliveries/*"})
public class DeliveryServlet extends AbstractServlet {
    private final DeliveryServiceInterface service;
    private final DeliveryFactory deliveryFactory = new DeliveryFactory(objectMapper);

    public DeliveryServlet() {
        service = DeliveryService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);
        String json = getJson(req);

        try {
            String deliveryType = req.getParameter("type");
            DeliveryDto dto = deliveryFactory.createDeliveryDto(deliveryType, json);
            service.save(dto, deliveryFactory);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writeResponse(resp, e.getMessage());
        }

        resp.sendRedirect("deliveries/all");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);
        String json = getJson(req);
        String responseAnswer = "";

        try {
            String deliveryType = req.getParameter("type");
            DeliveryDto dto = deliveryFactory.createDeliveryDto(deliveryType, json);
            service.update(dto, deliveryFactory);
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
            Integer deliveryId = Integer.parseInt(pathPart[1]);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            service.delete(deliveryId);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseAnswer = e.getMessage();
        }

        writeResponse(resp, responseAnswer);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);
        String responseAnswer;

        try {
            checkPath(req);
            String[] pathPart = getPathParts(req);

            if ("all".equals(pathPart[1])) {
                List<DeliveryDto> dtos = service.findAll();
                resp.setStatus(HttpServletResponse.SC_OK);
                responseAnswer = objectMapper.writeValueAsString(dtos);
            } else {
                Integer deliveryId = Integer.parseInt(pathPart[1]);
                DeliveryDto dto = service.findById(deliveryId);
                resp.setStatus(HttpServletResponse.SC_OK);
                responseAnswer = objectMapper.writeValueAsString(dto);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseAnswer = e.getMessage();
        }

        writeResponse(resp, responseAnswer);
    }
}
package servlet;

import dto.ItemDto;
import exception.NotFoundException;
import service.ItemService;
import service.impl.ItemServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet(urlPatterns = {"/items/*"})
public class ItemServlet extends AbstractServlet {
    private final ItemService itemService;

    public ItemServlet() {
        itemService = ItemServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);
        String responseAnswer;

        try {
            checkPath(req);
            String[] pathPart = getPathParts(req);

            if ("all".equals(pathPart[1])) {
                List<ItemDto> dtos = itemService.findAll();
                resp.setStatus(HttpServletResponse.SC_OK);
                responseAnswer = objectMapper.writeValueAsString(dtos);
            } else {
                Integer itemId = Integer.parseInt(pathPart[1]);
                ItemDto dto = itemService.findById(itemId);
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
            Integer itemId = Integer.parseInt(pathPart[1]);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            itemService.delete(itemId);
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
        Optional<ItemDto> itemResponse;
        try {
            itemResponse = Optional.ofNullable(objectMapper.readValue(json, ItemDto.class));
            ItemDto item = itemResponse.orElseThrow(IllegalArgumentException::new);
            responseAnswer = objectMapper.writeValueAsString(itemService.save(item));
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
        Optional<ItemDto> itemResponse;
        try {
            itemResponse = Optional.ofNullable(objectMapper.readValue(json, ItemDto.class));
            ItemDto itemUpdateDto = itemResponse.orElseThrow(IllegalArgumentException::new);
            itemService.update(itemUpdateDto);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseAnswer = e.getMessage();
        }

        writeResponse(resp, responseAnswer);
    }
}
package servlet;

import dto.UserDto;
import service.UserServiceInterface;
import service.impl.UserService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet(urlPatterns = {"/users/*"})
public class UserServlet extends AbstractServlet {
    private final UserServiceInterface service;

    public UserServlet() {
        service = UserService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);
        String responseAnswer;

        try {
            checkPath(req);
            String[] pathPart = getPathParts(req);

            if ("all".equals(pathPart[1])) {
                List<UserDto> dtos = service.findAll();
                resp.setStatus(HttpServletResponse.SC_OK);
                responseAnswer = objectMapper.writeValueAsString(dtos);
            } else {
                Integer userId = Integer.parseInt(pathPart[1]);
                UserDto dto = service.findById(userId);
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
            Integer userId = Integer.parseInt(pathPart[1]);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            service.delete(userId);
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

        Optional<UserDto> userResponse;
        try {
            userResponse = Optional.ofNullable(objectMapper.readValue(json, UserDto.class));
            UserDto dto = userResponse.orElseThrow(IllegalArgumentException::new);
            service.save(dto);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writeResponse(resp, e.getMessage());
        }

        resp.sendRedirect("users/all");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);
        String json = getJson(req);

        String responseAnswer = "";
        Optional<UserDto> userResponse;
        try {
            userResponse = Optional.ofNullable(objectMapper.readValue(json, UserDto.class));
            UserDto user = userResponse.orElseThrow(IllegalArgumentException::new);
            service.update(user);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseAnswer = e.getMessage();
        }

        writeResponse(resp, responseAnswer);
    }
}
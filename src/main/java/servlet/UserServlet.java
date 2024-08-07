package servlet;

import dto.UserDto;
import exception.NotFoundException;
import service.UserService;
import service.impl.UserServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet(urlPatterns = {"/users/*"})
public class UserServlet extends AbstractServlet {
    private final UserService userService;

    public UserServlet() {
        userService = UserServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);
        String responseAnswer;

        try {
            checkPath(req);
            String[] pathPart = getPathParts(req);

            if ("all".equals(pathPart[1])) {
                List<UserDto> dtos = userService.findAll();
                resp.setStatus(HttpServletResponse.SC_OK);
                responseAnswer = objectMapper.writeValueAsString(dtos);
            } else {
                Integer userId = Integer.parseInt(pathPart[1]);
                UserDto dto = userService.findById(userId);
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
            Integer userId = Integer.parseInt(pathPart[1]);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            userService.delete(userId);
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
        Optional<UserDto> userResponse;
        try {
            userResponse = Optional.ofNullable(objectMapper.readValue(json, UserDto.class));
            UserDto user = userResponse.orElseThrow(IllegalArgumentException::new);
            responseAnswer = objectMapper.writeValueAsString(userService.save(user));
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
        Optional<UserDto> userResponse;
        try {
            userResponse = Optional.ofNullable(objectMapper.readValue(json, UserDto.class));
            UserDto userUpdateDto = userResponse.orElseThrow(IllegalArgumentException::new);
            userService.update(userUpdateDto);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseAnswer = e.getMessage();
        }

        writeResponse(resp, responseAnswer);
    }
}
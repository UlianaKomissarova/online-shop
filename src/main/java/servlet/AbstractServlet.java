package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.*;
import java.io.*;
import java.text.SimpleDateFormat;

public abstract class AbstractServlet extends HttpServlet {
    protected final ObjectMapper objectMapper = new ObjectMapper();

    public AbstractServlet() {
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    }

    protected static void setJsonHeader(HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
    }

    protected static String getJson(HttpServletRequest req) throws IOException {
        req.setCharacterEncoding("UTF-8");

        StringBuilder sb = new StringBuilder();
        BufferedReader postData = req.getReader();
        String line;
        while ((line = postData.readLine()) != null) {
            sb.append(line);
        }

        return sb.toString();
    }

    protected void checkPath(HttpServletRequest req) {
        if (req.getPathInfo() == null) {
            throw new IllegalArgumentException("Path info is missing");
        }
    }

    protected String[] getPathParts(HttpServletRequest req) {
        String[] pathPart = req.getPathInfo().split("/");
        if (pathPart.length < 2) {
            throw new IllegalArgumentException("Invalid path info");
        }

        return pathPart;
    }

    protected void writeResponse(HttpServletResponse resp, String responseAnswer) throws IOException {
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(responseAnswer);
        printWriter.flush();
    }
}
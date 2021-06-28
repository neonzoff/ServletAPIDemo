package com.neonzoff.servletsapidemo.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Tseplyaev Dmitry
 */
@WebServlet(urlPatterns = {"/file"})
@MultipartConfig(location = "c:\\upload_tseplyaev\\")
public class FileServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        //создание каталога для загруженных файлов
        new File("c:\\upload_tseplyaev").mkdir();
    }

    @Override
    public void destroy() {
        super.destroy();
        //удаление загруженных файлов
        try {
            removeUploadedFiles();
        } catch (IOException e) {
            System.out.println("Не удалось удалить загруженные файлы");
        }
    }

    private void removeUploadedFiles() throws IOException {
        Files.walk(Paths.get("c:\\upload_tseplyaev"))
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doPost(req, resp);
        System.out.println("FileServlet: doPost");

        try (PrintWriter writer = resp.getWriter()) {
            for (Part part : req.getParts()) {
                if (part.getName().equals("author")) {
                    InputStream inputStream = part.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    String author = new BufferedReader(inputStreamReader)
                            .lines()
                            .collect(Collectors.joining("\n"));
                    System.out.println("Author: " + author);
                    writer.write("Author: " + author + "\n");
                } else {
                    System.out.println("Первые две строки из файла:\n");
                    writer.write("Two lines of file:\n");
                    BufferedReader in = new BufferedReader(new InputStreamReader(part.getInputStream()));
                    for (int i = 0; i < 2; i++) {
                        String temp = in.readLine();
                        System.out.println(temp);
                        writer.write(temp + "\n");
                    }
                    part.write(UUID.randomUUID() + part.getSubmittedFileName());
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла");
        }
        resp.sendRedirect("/ServletAPIDemo_war_exploded/static/addFileForm.html");
    }
}

import com.google.gson.Gson;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MainServlet extends HttpServlet {

    private static final String PATH_DIRECTORY = "D:\\img_directory";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        List<String> fileNames = new ArrayList<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(PATH_DIRECTORY))) {
            for (Path path : directoryStream) {
                fileNames.add(path.getFileName().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String json = new Gson().toJson(fileNames);
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fileTitle = req.getParameter("fileTitle");
        resp.setContentType("image/jpeg");
        File file = new File(PATH_DIRECTORY + "\\" + fileTitle);
        if (file.exists()) {
            BufferedImage bufferedImage;
            try (OutputStream out = resp.getOutputStream();){
                bufferedImage = ImageIO.read(file);
                ImageIO.write(bufferedImage, "jpg", out);
            } catch (IIOException e) {
                e.printStackTrace();
            }
        }
    }
}

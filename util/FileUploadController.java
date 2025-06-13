package egovframework.com.fivemlist.util;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {
	@RequestMapping(value = "/api/uploadfile.do", method = RequestMethod.POST)
	@ResponseBody
	public String uploadFile(@RequestParam("image") MultipartFile file,
	                         @RequestParam("ext") String ext,
	                         @RequestParam("name") String name,
	                         HttpServletRequest request) throws Exception {

	    // 업로드할 경로
	    String uploadPath = request.getServletContext().getRealPath("/assets/userupload/");
	    System.out.println(uploadPath);
	    File dir = new File(uploadPath);
	    if (!dir.exists()) {
	        dir.mkdirs(); // 디렉토리가 없다면 생성
	    }

	    // 파일 저장
	    String saveFileName = System.currentTimeMillis() + "_" + name.replaceAll("\\s", "_");
	    File saveFile = new File(uploadPath, saveFileName);
	    file.transferTo(saveFile); // 저장

	    // 저장된 파일의 URL 반환 (프론트에 보내줄 경로)
	    String fileUrl = "/assets/userupload/" + saveFileName;
	    return fileUrl;
	}

}

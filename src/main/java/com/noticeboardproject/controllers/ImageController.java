package com.noticeboardproject.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.noticeboardproject.domain.Notice;
import com.noticeboardproject.services.NoticeService;

@Controller
public class ImageController {

	NoticeService noticeService;
	
	@Autowired
	public ImageController(NoticeService noticeService) {
		this.noticeService = noticeService;
	}
	
    @GetMapping("notice/{id}/mainImage")
    public void renderMainImageFromDB(@PathVariable String id, HttpServletResponse response) throws IOException {
        Notice notice = noticeService.findById(Long.valueOf(id));

        if (notice.getMainImage() != null) {
        	addImageToResponse(notice.getMainImage(), response);
        }
    }
    
    @GetMapping("notice/{id}/image1")
    public void renderImage1FromDB(@PathVariable String id, HttpServletResponse response) throws IOException {
        Notice notice = noticeService.findById(Long.valueOf(id));

        if (notice.getImage1() != null) {
        	addImageToResponse(notice.getImage1(), response);
        }
    }
    
    @GetMapping("notice/{id}/image2")
    public void renderImage2FromDB(@PathVariable String id, HttpServletResponse response) throws IOException {
        Notice notice = noticeService.findById(Long.valueOf(id));

        if (notice.getImage2() != null) {
            addImageToResponse(notice.getImage2(), response);
        }
    }
    
    private void addImageToResponse(Byte[] bytes, HttpServletResponse response) throws IOException {
    	byte[] byteArray = new byte[bytes.length];
        int i = 0;

        for (Byte wrappedByte : bytes){
            byteArray[i++] = wrappedByte; //auto unboxing
        }

        response.setContentType("image/jpeg");
        InputStream is = new ByteArrayInputStream(byteArray);
        IOUtils.copy(is, response.getOutputStream());
    }
}

package com.noticeboardproject.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.noticeboardproject.domain.Notice;
import com.noticeboardproject.services.NoticeService;

public class ImageControllerTest {
	MockMvc mockMvc;
	
	ImageController imageController;
	
	@Mock
	NoticeService noticeService;
	
	@Before
	public void setUp() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		
		//Solution for ServletException: Circular view path
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates");
        viewResolver.setSuffix(".html");
        
		imageController = new ImageController(noticeService);
		mockMvc = MockMvcBuilders.standaloneSetup(imageController)
				.setViewResolvers(viewResolver)
				.build();
	}
	
	@Test
    public void renderImageFromDB() throws Exception {

        //given
        Notice notice = new Notice();
        notice.setId(1L);

        String s = "fake image text";
        Byte[] bytesBoxed = new Byte[s.getBytes().length];

        int i = 0;
        for (byte b : s.getBytes()){
            bytesBoxed[i++] = b;
        }

        notice.setMainImage(bytesBoxed);
        notice.setImage1(bytesBoxed);
        notice.setImage2(bytesBoxed);

        when(noticeService.findById(anyLong())).thenReturn(notice);

        //when
        MockHttpServletResponse mainImageResponse = mockMvc.perform(get("/notice/1/mainImage"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        byte[] mainImagereponseBytes = mainImageResponse.getContentAsByteArray();
        
        MockHttpServletResponse image1Response = mockMvc.perform(get("/notice/1/image1"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        byte[] image1ReponseBytes = image1Response.getContentAsByteArray();
        
        MockHttpServletResponse image2Response = mockMvc.perform(get("/notice/1/image2"))
        		.andExpect(status().isOk())
        		.andReturn().getResponse();
        byte[] image2ReponseBytes = image2Response.getContentAsByteArray();
        
        assertEquals(s.getBytes().length, mainImagereponseBytes.length);
        assertEquals(s.getBytes().length, image1ReponseBytes.length);
        assertEquals(s.getBytes().length, image2ReponseBytes.length);
    }
}

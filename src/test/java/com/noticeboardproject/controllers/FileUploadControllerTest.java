package com.noticeboardproject.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.google.common.io.ByteStreams;
import com.noticeboardproject.config.IntegrationTestConfig;
import com.noticeboardproject.storage.StorageException;
import com.noticeboardproject.storage.StorageFileNotFoundException;
import com.noticeboardproject.storage.StorageService;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class FileUploadControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private StorageService storageService;

	@Before
	public void setUp() throws Exception {
		IntegrationTestConfig.setAuthenticationToken(SecurityContextHolder.getContext());
	}

    @Test
    public void shouldSaveUploadedFile() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "Spring Framework".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/uploadFile")
                .file(multipartFile))
            .andExpect(status().is(200))
            .andExpect(content().string("Successfully uploaded - " + multipartFile.getOriginalFilename()));

        then(this.storageService).should().store(any(MockMultipartFile.class), anyString());
    }

    @Test
    public void should404WhenMissingFileGet() throws Exception {
        when(storageService.loadAsResource(anyString(), anyString()))
                .thenThrow(new StorageFileNotFoundException(""));
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> mockMvc.perform(get("/files/test.png")).andExpect(status().isNotFound())).hasCause(new StorageFileNotFoundException(""));
        verify(storageService, times(1)).loadAsResource(anyString(), anyString());
    }

    @Test
    public void shouldDeleteFile() throws Exception {
    	doNothing().when(storageService).delete(anyString(), anyString());
    	mockMvc.perform(get("/files/test.png/delete"))
    	.andExpect(status().isOk())
    	.andExpect(content().string("Successfully deleted"));
    	verify(storageService, times(1)).delete(anyString(), anyString());
    }
    
    @Test
    public void should404WhenMissingFileDelete() {
    	doThrow(new StorageException("")).when(storageService).delete(anyString(), anyString());
    	
    	org.assertj.core.api.Assertions.assertThatThrownBy(() -> 
    		mockMvc.perform(get("/files/test.png/delete"))
    		.andExpect(status().isNotFound()))
    	.hasCause(new StorageException(""));
    	
    	verify(storageService, times(1)).delete(anyString(), anyString());
    }
    
    @Test
    public void shouldDownloadFile() throws Exception {
    	ClassPathResource resource = new ClassPathResource("testUpload.txt", getClass());
    	when(storageService.loadAsResource(anyString(), anyString())).thenReturn(resource);
    	mockMvc.perform(get("/files/test.png"))
    	.andExpect(status().isOk())
    	.andExpect(content().bytes(ByteStreams.toByteArray(resource.getInputStream())));
    	
    	verify(storageService, times(1)).loadAsResource(anyString(), anyString());
    }
}

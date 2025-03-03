package com.wh.franken.shorturl.app.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wh.franken.shorturl.app.service.ShortUrlService;
import com.wh.franken.shorturl.app.vo.Result;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class ExceptionAdviceTest {

    @MockBean
    private ShortUrlService shortUrlService;

    @Autowired
    private MockMvc mvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testRuntimeExceptionHanlder() throws Exception {
        Mockito.when(shortUrlService.generatorShortUrl(Mockito.any(String.class))).thenThrow(new RuntimeException("error"));
        String url = "/api/shortUrl/convert2ShortUrl?longUrl=";
        String longUrl = "http://www.baidu.com/chris/app?id=10";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url + longUrl)).
                andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();
        assertEquals(status, HttpStatus.INTERNAL_SERVER_ERROR.value());
        Result<String> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<Result<String>>() {
        });
        assertNotNull(result);
        assertEquals(Result.INTERNAL_ERROR, result.getErrorCode());
    }
}

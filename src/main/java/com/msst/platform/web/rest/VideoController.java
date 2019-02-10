package com.msst.platform.web.rest;

import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.msst.platform.service.util.MultipartFileSender;

@RestController
@RequestMapping("/videos")
public class VideoController {

    @RequestMapping(value="/test", method = RequestMethod.GET)
    public void getTestVideo(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
    	// Change path with needed string
            MultipartFileSender.fromPath(Paths.get("E:\\videos\\SampleVideo_1280x720_5mb.mp4"))
                    .with(request)
                    .with(response)
                .serveResource();

    }
}

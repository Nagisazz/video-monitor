package com.nagisazz.video.controller;

import com.nagisazz.video.cut.VideoCutUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Date;

@Slf4j
@RestController
public class MonitorController {

    @Autowired
    private VideoCutUtil videoCutUtil;

    @RequestMapping("/start/{start}/{fileName}")
    public void start(@PathVariable int start, @PathVariable String fileName) {
        Long time = System.currentTimeMillis();
        log.info("开始执行分析，当前时间：{}", new Date());
        File videoFile = new File("/data/nagisa/" + fileName);
        videoCutUtil.getVideoFrame(videoFile, start);
    }
}

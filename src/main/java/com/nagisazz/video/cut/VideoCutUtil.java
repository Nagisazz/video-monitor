package com.nagisazz.video.cut;

import com.nagisazz.video.compare.ImageHistogram;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

@Slf4j
@Component
public class VideoCutUtil {

    @Value("${stepTimes}")
    private int stepTimes;

    private ByteArrayInputStream oriInputStream;

    @Autowired
    private ImageHistogram imageHistogram;

    /**
     * 截取视频获得图片
     *
     * @param video
     * @param start
     */
    public void getVideoFrame(File video, int start) {
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(video);
        // 步长
        int step = perSecondFrameNum(video);
        oriInputStream = null;
        try {
            ff.start();
            // 起始位置
            long i = 0;
            int length = ff.getLengthInFrames();
            long nextFrame = start * step;
            while (i < length) {
                Frame frame = ff.grabImage();
                if (i == nextFrame) {
                    long now = ((i - start * step) / step + start);
                    log.info("开始比较第：{}秒截图", now);
                    compareFramePic(frame, now);
                    log.info("完成比较第：{}秒截图", now);
                    nextFrame = i + step * stepTimes;
                }
                i++;
            }
            ff.stop();
        } catch (IOException e) {
            log.error("截取失败", e);
        }
    }

    private void compareFramePic(Frame frame, long now) throws IOException {
        // 截取的帧图片
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage srcImage = converter.getBufferedImage(frame);
        int srcImageWidth = srcImage.getWidth();
        int srcImageHeight = srcImage.getHeight();
        // 对截图进行等比例缩放(缩略图)
        int width = 480;
        int height = (int) (((double) width / srcImageWidth) * srcImageHeight);
        BufferedImage thumbnailImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        thumbnailImage.getGraphics().drawImage(srcImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(thumbnailImage, "jpg", os);
        if (oriInputStream == null) {
            oriInputStream = new ByteArrayInputStream(os.toByteArray());
        } else {
            InputStream input = new ByteArrayInputStream(os.toByteArray());

            ByteArrayOutputStream oriStream = cloneInputStream(oriInputStream);
            double match = imageHistogram.match(new ByteArrayInputStream(oriStream.toByteArray()), input);
            if (match < 0.99) {
                log.error("发现异常，第：{}秒", now);
            }
        }

        File picFile = new File("/data/nagisa/"+now+".jpg");
        ImageIO.write(thumbnailImage, "jpg", picFile);
    }

    private int perSecondFrameNum(File video) {
        int frameNum = 0;
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(video);
        try {
            ff.start();
            long length = ff.getLengthInFrames();
            long duration = ff.getLengthInTime() / (1000 * 1000);
            frameNum = Math.toIntExact(length / duration);
            ff.stop();
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
        return frameNum;
    }

    private ByteArrayOutputStream cloneInputStream(InputStream input) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            input.reset();
            return baos;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

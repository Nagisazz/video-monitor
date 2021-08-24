//package com.nagisazz.video.cut;
//
//import org.bytedeco.javacpp.opencv_core;
//import org.bytedeco.javacpp.opencv_imgcodecs;
//import org.bytedeco.javacv.FFmpegFrameGrabber;
//import org.bytedeco.javacv.Frame;
//import org.bytedeco.javacv.OpenCVFrameConverter;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//public abstract class FrameGrabberKit {
//
//    public static void main(String[] args) throws Exception {
//        String inputFilePth = "/data/1.mp4";
//        randomGrabberFFmpegImage(inputFilePth, 5);
//    }
//
//    public static void randomGrabberFFmpegImage(String filePath, int randomSize)
//            throws Exception {
//        FFmpegFrameGrabber ff = FFmpegFrameGrabber.createDefault(filePath);
//        ff.start();
//
//        int ffLength = ff.getLengthInFrames();
//        List<Integer> randomGrab = random(ffLength, randomSize);
//        int maxRandomGrab = randomGrab.get(randomGrab.size() - 1);
//        Frame f;
//        int i = 0;
//        while (i < ffLength) {
//            f = ff.grabImage();
//            if (randomGrab.contains(i)) {
//                doExecuteFrame(f, i);
//            }
//            if (i >= maxRandomGrab) {
//                break;
//            }
//            i++;
//        }
//        ff.stop();
//    }
//
//    public static void doExecuteFrame(Frame f, int index) {
//        if (null == f || null == f.image) {
//            return;
//        }
//        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
//        String imageMat = "png";
//        String FileName = "D:\\img";
//        opencv_core.Mat mat = converter.convertToMat(f);
//        opencv_imgcodecs.imwrite("/data/" + index + "sls.png", mat);//存储图像
//    }
//
//    public static List<Integer> random(int baseNum, int length) {
//        List<Integer> list = new ArrayList<>(length);
//        while (list.size() < length) {
//            Integer next = (int) (Math.random() * baseNum);
//            if (list.contains(next)) {
//                continue;
//            }
//            list.add(next);
//        }
//        Collections.sort(list);
//        return list;
//    }
//}

package com.tensorflow.siamese.controllers;

import com.tensorflow.siamese.services.ImageProcessingService;
import com.tensorflow.siamese.services.RecognitionService;
import com.tensorflow.siamese.services.RecognitionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
@Slf4j
public class RecognitionController {

    @Value("${images.save.path:src/main/resources/Images}")
    private String path;

    @Autowired
    private RecognitionService recognitionService;

    @RequestMapping(value = "/recog", method = RequestMethod.POST)
    ResponseEntity recognize(@RequestParam("image") MultipartFile image, @RequestParam("minConfidence") Double minConfidence
            , @RequestParam("topTwoMinGap") Double topTwoMinGap) {
        try {
            Path imagePath = ImageProcessingService.write(image, path);
           // System.out.print(imagePath + " ");
            RecognitionResult result = recognitionService.recognise(imagePath, minConfidence, topTwoMinGap);
            System.out.println(", best match =  " + result.bestMatch().name() + ", distance = " + result.userList().get(0).getValue() + ", minConfidence = " + minConfidence + ", topTwoMinGap = " + topTwoMinGap);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.debug("Exception in recognize Api", e);
            System.out.println(" did not matched to anyone");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Signature is Forged");
        }

    }

    // Dont call from UI, call from Postman. Postman se sari files select krke bhejni hai bs (Not folder localtion)
    @RequestMapping(value = "/recoglist", method = RequestMethod.POST)
    ResponseEntity recognizelist(@RequestParam("image") List<MultipartFile> images,  // <-- Change to List
                                 @RequestParam("minConfidence") Double minConfidence
            , @RequestParam("topTwoMinGap") Double topTwoMinGap) {

        List<RecognitionResult> results = new ArrayList<RecognitionResult>(); // <-- create Results
        double count = 0;
        double total_num = 0;
        String s = "";
        String p = "";
        String s1 = "";
        String s2 = "";
        int i = 0;
        for (MultipartFile image : images) {  // <-- Call in for loop
            try {
                Path imagePath = ImageProcessingService.write(image, path);
                System.out.print(imagePath + " ");
                RecognitionResult result = recognitionService.recognise(imagePath, minConfidence, topTwoMinGap);
                System.out.println(", best match =  " + result.bestMatch().name() + ", distance = " + result.userList().get(0).getValue() + ", minConfidence = " + minConfidence + ", topTwoMinGap = " + topTwoMinGap);
//                if(imagePath.toString().charAt(imagePath.toString().length()-7) == result.bestMatch().name().toString().charAt(result.bestMatch().name().toString().length()-1))
//                {
//                    count ++;
//                }
                s1 = "";
                s2 = "";
                s = imagePath.toString();
                for (i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == '_') {
                        break;
                    }
                }
                i++;
                while (s.charAt(i) != '_') {
                    s1 += s.charAt(i);
                    i++;
                }
                p = result.bestMatch().name().toString();
                for (i = 0; i < p.length(); i++) {
                    if (p.charAt(i) >= '0' && p.charAt(i) <= '9') {
                        s2 += p.charAt(i);
                    }
                }
                System.out.println(s1 + " " + s2);
                if (s1.compareTo(s2) == 0) {

                    count++;
                }

                total_num++;
                results.add(result);
            } catch (Exception e) {
                log.debug("Exception in recognize Api", e);
                total_num++;
                System.out.println(" did not matcjhed to anyone " + ", minConfidence = " + minConfidence + ", topTwoMinGap = " + topTwoMinGap);
            }

        }
        // System.out.println(results);  // <-- Print
        System.out.println("Count = " + count + " total num " + total_num + " Accuracy = " + count / total_num);
        return ResponseEntity.ok(results); // <-- Can read in postman as well


    }
}

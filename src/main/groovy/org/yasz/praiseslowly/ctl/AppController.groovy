package org.yasz.praiseslowly.ctl

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yasz.praiseslowly.PPTXHelper
import org.yasz.praiseslowly.dao.Unitdao

import java.text.SimpleDateFormat;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
/**
 * created by yang on 10:54 2018/1/16.
 * describtion:entry rest controller.
 * @param /ppt/:
 */

@RestController
@RequestMapping("/api/")
public class AppController {
     @RequestMapping(value = "/unit2", method = GET)
     ResponseEntity<byte[]> index(@RequestParam(value="title", defaultValue="title1")String title,
                                  @RequestParam(value="lyric", defaultValue="lyric1")String lyric) {
         /**
          * created by yang on 16:22 2018/1/16.
          * describtion:基于unit2模板，在URL填充title lyric
          * @param title:
          * @param lyric:
            @param tmp:默认
          */

         System.out.println("ok /api/unit2 GET\n\n");
         PPTXHelper ppt = new PPTXHelper();

         ppt.parseUnits2(title.split("zzz").toList(),lyric.split("zzz").toList());
         OutputStream os = new ByteArrayOutputStream()
         ppt.presentationMLPackage.save(os)
         HttpHeaders headers = new HttpHeaders()
         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
         String filename=dateFormat.format(Calendar.getInstance().getTime());
         headers.set(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"${filename}.pptx\"")
         headers.setContentType(new MediaType("application","octet-stream"))
         return new ResponseEntity<byte[]>(os.toByteArray(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/unit2", method = POST)
    ResponseEntity<byte[]> index2(@RequestParam(value="title", defaultValue="title1")String title,
                                  @RequestParam(value="lyric", defaultValue="lyric1")String lyric) {
        /**
         * created by yang on 16:22 2018/1/16.
         * describtion:基于unit2模板，在post body填充title lyric
         * @param title:
         * @param lyric:
         @param tmp:默认
         */
        System.out.println("*****post");
        return index(title,lyric.replaceAll(/\r\n/,'\n'));
    }

}
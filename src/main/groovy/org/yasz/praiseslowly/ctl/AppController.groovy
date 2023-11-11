package org.yasz.praiseslowly.ctl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DataSourceUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.yasz.praiseslowly.service.N2Service
import service.ReportViewService
import tool.DocxHelper
import tool.PPTXHelper
import yjh.helper.Excelhelper2

import java.text.SimpleDateFormat

import static org.springframework.web.bind.annotation.RequestMethod.GET
import static org.springframework.web.bind.annotation.RequestMethod.POST

/**
 * created by yang on 10:54 2018/1/16.
 * describtion:entry rest controller.
 * @param /ppt/:
 */

@RestController
@RequestMapping("/api/")
class AppController {



    @Autowired
    private N2Service n2Service;

    @RequestMapping(value = "/kata", method = [GET, POST])
    ResponseEntity<byte[]> kata(@RequestParam(value = "type1", defaultValue = "1") String type1, @RequestParam
            (value = "type2", defaultValue = "1") String type2) {
        OutputStream os = new ByteArrayOutputStream()
        n2Service.exportKata(type1,type2,os)
        HttpHeaders headers = new HttpHeaders()
        def fileName = new java.text.SimpleDateFormat("yyMMddhhmmss").format(new Date())
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"${fileName}.pdf\"")
        headers.setContentType(new MediaType("application", "octet-stream"))
        return new ResponseEntity<byte[]>(os.toByteArray(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/vocabulary", method = [GET, POST])
    ResponseEntity<byte[]> vocabulary(@RequestParam(value = "units", defaultValue = "1") String[] units) {
        OutputStream os = new ByteArrayOutputStream()
        n2Service.exportVocabulary(units,os)
        HttpHeaders headers = new HttpHeaders()
        def fileName = new java.text.SimpleDateFormat("yyMMddhhmmss").format(new Date())
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"${fileName}.pdf\"")
        headers.setContentType(new MediaType("application", "octet-stream"))
        return new ResponseEntity<byte[]>(os.toByteArray(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/unit1", method = [GET, POST])
    ResponseEntity<byte[]> index(@RequestParam(value = "vano", defaultValue = "100101") String vano, @RequestParam
            (value = "sem", defaultValue = "211") String sem) {
        OutputStream os = new ByteArrayOutputStream()
        def conn = DataSourceUtils.getConnection(jdbcTemplate.getDataSource())
        new ReportViewService().getByVano([vano], sem, os, conn)
        conn.close()
        HttpHeaders headers = new HttpHeaders()
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"${vano}.pdf\"")
        headers.setContentType(new MediaType("application", "octet-stream"))
        return new ResponseEntity<byte[]>(os.toByteArray(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/unit2", method = [GET, POST])
    ResponseEntity<byte[]> index(@RequestParam(value = "title", defaultValue = "title1") String title,
                                 @RequestParam(value = "lyric", defaultValue = "lyric1") String lyric,
                                 @RequestParam(value = "template", defaultValue = "korea2.pptx") String template,
                                 @RequestParam(value = "filetype", defaultValue = "pptx") String filetype
    ) {
        /**
         * created by yang on 16:22 2018/1/16.
         * describtion:基于unit2模板，在URL填充title lyric
         * @param title :
         * @param lyric :
         @param tmp :默认
         */

        System.out.println("ok /api/unit2 GET\n\n");

//         lyric=lyric.replaceAll(/\r\n/,'\n')
        PPTXHelper ppt = new PPTXHelper("dat/template/${template}")
        ppt.parseUnits2(title.split("zzz").toList(), lyric.split("zzz").toList());
        OutputStream os = new ByteArrayOutputStream()
//         ppt.presentationMLPackage.save(os)
        if (filetype == 'pdf') {
            ppt.saveAsPDFOutputStream(os)
        } else {
            ppt.saveAsOutputStream(os)
        }
        HttpHeaders headers = new HttpHeaders()
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd")
        String filename = dateFormat.format(Calendar.getInstance().getTime())
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"${filename}\"")
//        headers.setContentType(new MediaType("application", "octet-stream")) // 提示浏览器直接下载
        return new ResponseEntity<byte[]>(os.toByteArray(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/unit3", method = [GET, POST])
    ResponseEntity<byte[]> index3(@RequestParam(value = "title", defaultValue = "test") String title,
                                  @RequestParam(value = "contents", defaultValue = "a\t3\r\nb\t5") String contents) {
        System.out.println("ok /api/unit3 GET\n\n")


        OutputStream os = new ByteArrayOutputStream()
        HttpHeaders headers = new HttpHeaders()
        Excelhelper2.s1(title, contents, os)
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"${title}.pdf\"")
        headers.setContentType(new MediaType("application", "pdf"))

        return new ResponseEntity<byte[]>(os.toByteArray(), headers, HttpStatus.OK);
    }


}
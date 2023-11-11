package org.yasz.praiseslowly.service

import com.alibaba.fastjson.JSON
import common.Const
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RestController
import tool.DocxHelper
import utils.DBHelper
import org.springframework.beans.factory.annotation.Autowired
import java.sql.Connection
import java.sql.DriverManager
import java.text.SimpleDateFormat

/**
 * Created by Peter.Yang on 2019/6/22.
 */
@Service
class N2Service {


    private JdbcTemplate jdbcTemplate;
    @Autowired
    public N2Service(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
     void exportVocabulary(String[] ids,OutputStream os){
        //词汇表
        def unit = ids.join(",")
        def sqlStr = """SELECT japanese,chinese FROM "JapaneseVocabulary" where unit_id in ($unit) and type1 <> '*' limit 55""".toString()
        def data = this.jdbcTemplate.queryForList(sqlStr)


        def time = (int) (data.size()*20/60)
        def q1 = ""
        def a1 = ""
        def b1 = ""
        def list = (0..data.size()-1).toList()
        while(list.size() > 0) {
            def randomNumber = (int) (Math.random() * list.size())
            def num = list.removeAt(randomNumber)
            q1+=data[num].chinese+"\n"
            a1+=data[num].japanese+"\n"
            b1+="_______________________\n"
        }

        def paras = ["b1":newlineToBreakHack(b1),"q1": newlineToBreakHack(q1), "a1": newlineToBreakHack(a1),"unit":unit,"time":time]
        def doc = new DocxHelper("""data/单词默写.docx""")
//        doc.replace(paras).saveAsPDF("${outputFileName}.pdf")
         doc.replace(paras).saveAsPDFOutputStream (os)

    }


    void exportKata(type,type2, OutputStream os) {

        //type： 1 生成清音， 2.只生成 浊音/拗音 3.都生成
        //type2:  1 平仮名 hiragana  2.片仮名 katakana カタカナ
        def typeConditions = [
                "1": "'清音'",
                "2": "'浊音', '半浊音', '拗音', '浊拗音'",
                "3": "'清音', '浊音', '半浊音', '拗音', '浊拗音'"
        ]
        if(type == null) type =1
        if(type2 == null) type2 =2

        def sqlStr = "SELECT id,hiragana,katakana,roman,type1 FROM JapaneseKana where type1 in (${typeConditions[type]}) and status is null".toString()
        println(sqlStr)
        def data =  this.jdbcTemplate.queryForList(sqlStr)

        println(data)
        def q1 = ""
        def a1 = ""
        1.upto(300) { it ->
            def randomNumber = (int) (Math.random() * data.size())
            q1 += data[randomNumber].roman

            if(data[randomNumber].roman.length()==1){
//                println(data[randomNumber].roman.length())
                q1 += "  "
            }else{
                q1 += " "
            }
            a1 += (type2=="2"?data[randomNumber].katakana:data[randomNumber].hiragana)+ " "
            if (it % 20 == 0) {
                q1 += "\n"
                a1 += "\n"
            }
        }
        def doc = new DocxHelper("""data/五十音图测试.docx""")
        def paras = ["q1": newlineToBreakHack(q1), "a1": newlineToBreakHack(a1)]

//        OutputStream os = new ByteArrayOutputStream()
        doc.replace(paras).saveAsPDFOutputStream (os )
//        DocxHelper.toPDF("五十音图测试${type}.docx", "五十音图测试${type}.pdf")

    }
    static String newlineToBreakHack(String r) {

        StringTokenizer st = new StringTokenizer(r, "\t\n\r\f");
        StringBuilder sb = new StringBuilder()

        boolean firsttoken = true;
        while (st.hasMoreTokens()) {
            String line = (String) st.nextToken();
            if (firsttoken) {
                firsttoken = false;
            } else {
                sb.append("</w:t><w:br/><w:t>")
            }
            sb.append(line);
        }
        return sb.toString();
    }

}

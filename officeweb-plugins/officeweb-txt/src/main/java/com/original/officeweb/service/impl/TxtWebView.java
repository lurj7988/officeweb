package com.original.officeweb.service.impl;

import com.original.officeweb.model.WebDocument;
import com.original.officeweb.service.WebView;
import cpdetector.io.parser.EncodingDetect;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.util.HtmlUtils;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class TxtWebView implements WebView {

    private final static Logger logger = LoggerFactory.getLogger(TxtWebView.class);

    @Override
    public String handle(WebDocument document, Model model) throws IOException {
        String fileData = HtmlUtils.htmlEscape(textData(document.getFilepath()));
        model.addAttribute("name", document.getName());
        model.addAttribute("textData", Base64.encodeBase64String(fileData.getBytes()));
        CodeMirrorMode codeMirrorMode = getCodeMirrorMode(document.getSuffix());
        model.addAttribute("mode", codeMirrorMode.getMode());
        model.addAttribute("source", codeMirrorMode.getSource());
        return "txt";
    }

    @Override
    public Collection<String> support() {
        return getCodeMirrorModes().keySet();
    }

    private CodeMirrorMode getCodeMirrorMode(String Suffix) {
        return getCodeMirrorModes().get(Suffix.toLowerCase(Locale.ROOT));
    }

    private Map<String, CodeMirrorMode> getCodeMirrorModes() {
        Map<String, CodeMirrorMode> map = new HashMap<>();
        map.put("txt", new CodeMirrorMode("clike/clike.js", "string"));
        map.put("java", new CodeMirrorMode("clike/clike.js", "text/x-java"));
        map.put("cs", new CodeMirrorMode("clike/clike.js", "text/x-csharp"));
        map.put("c", new CodeMirrorMode("clike/clike.js", "text/x-c"));
        map.put("xhtml", new CodeMirrorMode("htmlmixed/htmlmixed.js", "htmlmixed"));
        map.put("html", new CodeMirrorMode("htmlmixed/htmlmixed.js", "htmlmixed"));
        map.put("aspx", new CodeMirrorMode("htmlmixed/htmlmixed.js", "htmlmixed"));
        map.put("js", new CodeMirrorMode("javascript/javascript.js", "javascript"));
        map.put("json", new CodeMirrorMode("javascript/javascript.js", "javascript"));
        map.put("xml", new CodeMirrorMode("xml/xml.js", "xml"));
        map.put("css", new CodeMirrorMode("css/css.js", "text/css"));
        map.put("sql", new CodeMirrorMode("sql/sql.js", "text/x-sql"));
        map.put("vm", new CodeMirrorMode("htmlmixed/htmlmixed.js", "htmlmixed"));
        map.put("py", new CodeMirrorMode("python/python.js", "python"));
        map.put("go", new CodeMirrorMode("go/go.js", "go"));
        map.put("yaml", new CodeMirrorMode("yaml/yaml.js", "yaml"));
        map.put("yml", new CodeMirrorMode("yaml/yaml.js", "yaml"));
        map.put("sh", new CodeMirrorMode("shell/shell.js", "shell"));
        return map;
    }

    private String textData(String baseUrll) throws IOException {
        File file = new File(baseUrll);
        if (!file.exists() || file.length() == 0) {
            return "";
        } else {
            String charset = EncodingDetect.getJavaEncode(baseUrll);
            logger.info(baseUrll + "编码格式为：" + charset);
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(baseUrll), charset));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line).append("\r\n");
            }
            return result.toString();
        }
    }
}

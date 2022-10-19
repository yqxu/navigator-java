package com.pingpongx.smb.warning.web.parser;

import io.vavr.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ParserFactory {
    @Autowired
    Map<String, AlertParser> origin;

    Map<String,AlertParser> parserMap = new ConcurrentHashMap<>();

    @PostConstruct
    private void init(){
        origin.values().stream()
                .flatMap(p->p.getSupportDepart().stream().map(dep->Tuple.of(dep,p)))
                .forEach(tuple->parserMap.put(tuple._1().toUpperCase(), tuple._2()));
    }

    public AlertParser departOf(String depart){
        return parserMap.get(depart.toUpperCase());
    }
}

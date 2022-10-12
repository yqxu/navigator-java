package com.pingpongx.smb.warning.biz.config;

import com.pingpongx.smb.warning.biz.moudle.Trie;
import com.pingpongx.smb.warning.biz.rules.Rule;
import org.springframework.stereotype.Component;

@Component
public class AlertConf {
    

    Trie<String,Rule> trie = new Trie();

//    Trie loadConf(){
//
//    }
}

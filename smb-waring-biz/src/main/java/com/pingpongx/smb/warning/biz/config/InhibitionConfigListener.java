package com.pingpongx.smb.warning.biz.config;

import com.pingpongx.config.client.listener.ConfigListener;
import com.pingpongx.smb.warning.biz.moudle.Trie;
import com.pingpongx.smb.warning.biz.rules.ConfiguredLeafRule;
import com.pingpongx.smb.warning.biz.rules.Rule;
import com.pingpongx.smb.warning.biz.rules.RuleTrie;
import com.pingpongx.smb.warning.biz.rules.scene.ConfiguredScene;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
public class InhibitionConfigListener implements ConfigListener {

    @Autowired
    RuleTrie trie;
    @Autowired
    ConfiguredScene inhibitionScenes;

    @Override
    public void onChange(String key, String value) {
        if(key.equals("configured.scene")){
            inhibitionScenes.loadConfigStr(value);
            log.info("Inhibition scene add by config upgrade:\n"+value);
        }

    }
}

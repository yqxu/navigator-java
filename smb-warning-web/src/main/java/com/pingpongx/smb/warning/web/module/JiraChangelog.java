package com.pingpongx.smb.warning.web.module;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JiraChangelog {

    private String id;

    private List<Item> items;

    @Getter
    @Setter
    public static class Item {
        private String field;
        private String fieldtype;
        private String from;
        private String fromString;
        private String to;
        private String toString;
    }

}

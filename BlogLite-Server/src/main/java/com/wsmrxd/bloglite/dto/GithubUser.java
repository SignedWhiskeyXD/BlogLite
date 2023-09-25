package com.wsmrxd.bloglite.dto;

import lombok.Data;

@Data
public class GithubUser {

    private String id;

    private String login;

    private String avatar_url;

    private String email;
}

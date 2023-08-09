package com.wsmrxd.bloglite.service;

import com.wsmrxd.bloglite.vo.BlogStream;

public interface BlogStreamService {

    BlogStream getInitStream(int num);

    BlogStream getBlogStream(int startID, int num);
}

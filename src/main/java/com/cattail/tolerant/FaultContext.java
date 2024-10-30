package com.cattail.tolerant;

import com.cattail.common.URL;
import com.cattail.socket.codec.RpcProtocol;

import java.util.List;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/30
 * @Copyright: https://github.com/CatTailzz
 */
public class FaultContext {

    private URL currentURL;

    private List<URL> urls;

    private RpcProtocol rpcProtocol;

    private Long requestId;

    private Exception exception;

    public FaultContext() {
    }

    public FaultContext(URL currentURL, List<URL> urls, RpcProtocol rpcProtocol, Long requestId, Exception exception) {
        this.currentURL = currentURL;
        this.urls = urls;
        this.rpcProtocol = rpcProtocol;
        this.requestId = requestId;
        this.exception = exception;
    }

    public URL getCurrentURL() {
        return currentURL;
    }

    public void setCurrentURL(URL currentURL) {
        this.currentURL = currentURL;
    }

    public List<URL> getUrls() {
        return urls;
    }

    public void setUrls(List<URL> urls) {
        this.urls = urls;
    }

    public RpcProtocol getRpcProtocol() {
        return rpcProtocol;
    }

    public void setRpcProtocol(RpcProtocol rpcProtocol) {
        this.rpcProtocol = rpcProtocol;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}

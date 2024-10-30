package com.cattail.register;

import com.alibaba.fastjson.JSON;
import com.cattail.common.Cache;
import com.cattail.common.URL;
import com.cattail.event.*;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import sun.misc.Cleaner;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/28
 * @Copyright: https://github.com/CatTailzz
 */
public class CuratorZookeeperRegistry extends AbstractZookeeperRegistry{

    // 连接失败等待重试事件
    public static final int BASE_SLEEP_TIME_MS = 1000;
    // 重试次数
    public static final int MAX_RETRIES = 3;
    // 根路径
    public static final String ROOT_PATH = "/cattail_rpc";

    public static final String PROVIDER = "/provider";

    private final CuratorFramework client;

    private static final String addr = "127.0.0.1:2181";

    /**
     * 启动zk
     */
    public CuratorZookeeperRegistry() {
        client = CuratorFrameworkFactory.newClient(addr, new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS, MAX_RETRIES));
        client.start();
    }

    @Override
    public void register(URL url) throws Exception {
        if (!existNode(ROOT_PATH)) {
            client.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(ROOT_PATH, "".getBytes());
        }

        final String providerDataPath = getProviderDataPath(url);

        if (existNode(providerDataPath)) {
            deleteNode(providerDataPath);
        }

        client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(providerDataPath, JSON.toJSONString(url).getBytes());
    }

    @Override
    public void unRegistry(URL url) throws Exception {
        deleteNode(getProviderDataPath(url));
        super.unRegistry(url);
    }

    // 根据服务，获取所有有效的url连接host
    @Override
    public List<URL> discoveries(String serviceName, String version) throws Exception {
        List<URL> urls = super.discoveries(serviceName, version);
        if (null == urls || urls.isEmpty()) {
            final List<String> strings = client.getChildren().forPath(getProviderPath(serviceName, version));
            if (!strings.isEmpty()) {
                urls = new ArrayList<>();
                for (String string : strings) {
                    final String[] split = string.split(":");
                    urls.add(new URL(split[0], Integer.parseInt(split[1])));
                }
            }
        }
        return urls;
    }

    // 订阅服务，出现变更时会触发监听器，同步到本地缓存
    @Override
    public void subscribe(URL url) throws Exception {
        final String path = getProviderPath(url.getServiceName(), url.getVersion());
        Cache.SUBSCRIBE_SERVICE_LIST.add(url);
        this.watchNodeDataChange(path);
    }

    @Override
    public void unSubscribe(URL url) {

    }


    public void watchNodeDataChange(String path) throws Exception {
        // 虽然是缓存，但是会实时变更
        PathChildrenCache cache = new PathChildrenCache(client, path, true);

        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);

        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                final PathChildrenCacheEvent.Type type = event.getType();
                System.out.println("PathChildrenCache event: " + type);
                RpcEventData eventData = null;
                if (type.equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
                    String path = event.getData().getPath();
                    final URL url = parsePath(path);
                    eventData = new DestroyRpcEventData(url);
                } else if (type.equals(PathChildrenCacheEvent.Type.CHILD_UPDATED) || type.equals(PathChildrenCacheEvent.Type.CHILD_ADDED)) {
                    String path = event.getData().getPath();
                    byte[] bytes = client.getData().forPath(path);
                    Object o = JSON.parseObject(bytes, URL.class);
                    eventData = type.equals(PathChildrenCacheEvent.Type.CHILD_UPDATED) ? new UpdateRpcEventData(o) : new AddRpcEventData(o);

                }

                RpcListenerLoader.sendEvent(eventData);
            }
        });
    }


    // 管理路径
    private String getProviderDataPath(URL url) {
        return ROOT_PATH + PROVIDER + "/" + url.getServiceName() + "/" + url.getVersion() + "/" + url.getIp() + ":" + url.getPort();
    }

    private String getProviderPath(URL url) {
        return ROOT_PATH + PROVIDER + "/" + url.getServiceName() + "/" + url.getVersion();
    }

    private String getProviderPath(String serviceName, String version) {
        return ROOT_PATH + PROVIDER + "/" + serviceName + "/" + version;
    }

    private URL parsePath(String path) throws Exception {
        final String[] split = path.split("/");
        String className = split[3];
        String version = split[4];
        final String[] split1 = split[5].split(":");
        String host = split1[0];
        String port = split1[1];
        final URL url = new URL();
        url.setServiceName(className);
        url.setVersion(version);
        url.setIp(host);
        url.setPort(Integer.valueOf(port));
        return url;
    }

    // 管理Node
    public boolean deleteNode(String path) {
        try {
            client.delete().forPath(path);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existNode(String path) {
        try {
            Stat stat = client.checkExists().forPath(path);
            return stat != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

/**
 * FileName: ServerTimeoutDiscardCloseTest
 * Author:   HuangTaiHong
 * Date:     2019/1/16 13:47
 * Description: 
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.group.process.netty.practice.timeout;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import roberto.group.process.netty.practice.common.CONNECTEventProcessor;
import roberto.group.process.netty.practice.common.DISCONNECTEventProcessor;
import roberto.group.process.netty.practice.common.RequestBody;
import roberto.group.process.netty.practice.common.SimpleClientUserProcessor;
import roberto.group.process.netty.practice.common.SimpleServerUserProcessor;
import roberto.group.process.netty.practice.connection.Connection;
import roberto.group.process.netty.practice.connection.enums.ConnectionEventTypeEnum;
import roberto.group.process.netty.practice.entrance.client.RGPDefaultRemoteClient;
import roberto.group.process.netty.practice.entrance.server.impl.RGPDefaultRemoteServer;
import roberto.group.process.netty.practice.exception.RemotingException;
import roberto.group.process.netty.practice.exception.remote.InvokeTimeoutException;
import roberto.group.process.netty.practice.remote.invoke.callback.InvokeCallback;
import roberto.group.process.netty.practice.remote.remote.RPCResponseFuture;
import roberto.group.process.netty.practice.utils.PortScanner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 〈一句话功能简述〉<br> 
 * 〈server process timeout test (timeout check in biz thread).〉
 *
 *  if already timeout waiting in work queue, then discard this request and return timeout exception.
 *
 *  Oneway will not do this.
 *
 * @author HuangTaiHong
 * @create 2019/1/16
 * @since 1.0.0
 */
@Slf4j
public class ServerTimeoutDiscardCloseTest {
    private int max_timeout = 500;

    private int port = PortScanner.select();
    private String address = "127.0.0.1:" + port;

    private RGPDefaultRemoteServer server;
    private RGPDefaultRemoteClient client;

    private int coreThread = 1;
    private int maxThread = 1;
    private int workQueue = 1;

    private CONNECTEventProcessor clientConnectProcessor = new CONNECTEventProcessor();
    private CONNECTEventProcessor serverConnectProcessor = new CONNECTEventProcessor();

    private DISCONNECTEventProcessor clientDisConnectProcessor = new DISCONNECTEventProcessor();
    private DISCONNECTEventProcessor serverDisConnectProcessor = new DISCONNECTEventProcessor();

    private SimpleServerUserProcessor serverUserProcessor = new SimpleServerUserProcessor(max_timeout, coreThread, maxThread, 60, workQueue);
    private SimpleClientUserProcessor clientUserProcessor = new SimpleClientUserProcessor(max_timeout, coreThread, maxThread, 60, workQueue);

    @Before
    public void init() {
        server = new RGPDefaultRemoteServer(port);
        server.addConnectionEventProcessor(ConnectionEventTypeEnum.CONNECT, serverConnectProcessor);
        server.addConnectionEventProcessor(ConnectionEventTypeEnum.CLOSE, serverDisConnectProcessor);
        serverUserProcessor.setTimeoutDiscard(false);
        server.registerUserProcessor(serverUserProcessor);
        server.start();

        client = new RGPDefaultRemoteClient();
        client.addConnectionEventProcessor(ConnectionEventTypeEnum.CONNECT, clientConnectProcessor);
        client.addConnectionEventProcessor(ConnectionEventTypeEnum.CLOSE, clientDisConnectProcessor);
        clientUserProcessor.setTimeoutDiscard(false);
        client.registerUserProcessor(clientUserProcessor);
        client.init();
    }

    @After
    public void stop() {
        server.stop();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            log.error("Stop server failed!", e);
        }
    }

    @Test
    public void testOneway() {
        for (int i = 0; i <= 1; ++i) {
            new Thread(() -> oneway(client, null)).start();
        }
        try {
            Thread.sleep(max_timeout * 2);
        } catch (InterruptedException e) {

        }
        Assert.assertEquals(2, serverUserProcessor.getInvokeTimesEachCallType(RequestBody.InvokeType.ONEWAY));
    }

    @Test
    public void testServerOneway() {
        for (int i = 0; i <= 1; ++i) {
            new Thread(() -> oneway(client, server)).start();
        }
        try {
            Thread.sleep(max_timeout * 2);
        } catch (InterruptedException e) {

        }
        Assert.assertEquals(2, clientUserProcessor.getInvokeTimesEachCallType(RequestBody.InvokeType.ONEWAY));
    }

    @Test
    public void testSync() {
        final int timeout[] = {max_timeout / 2, max_timeout / 3};
        for (int i = 0; i <= 1; ++i) {
            final int j = i;
            new Thread(() -> sync(client, null, timeout[j])).start();
        }
        try {
            Thread.sleep(max_timeout * 2);
        } catch (InterruptedException e) {

        }
        Assert.assertEquals(2, serverUserProcessor.getInvokeTimesEachCallType(RequestBody.InvokeType.SYNC));
    }

    @Test
    public void testServerSync() {
        final int timeout[] = { max_timeout / 2, max_timeout / 3 };
        for (int i = 0; i <= 1; ++i) {
            final int j = i;
            new Thread(() -> sync(client, server, timeout[j])).start();
        }
        try {
            Thread.sleep(max_timeout * 2);
        } catch (InterruptedException e) {

        }
        Assert.assertEquals(2, clientUserProcessor.getInvokeTimesEachCallType(RequestBody.InvokeType.SYNC));
    }

    @Test
    public void testFuture() {
        final int timeout[] = {max_timeout / 2, max_timeout / 3};
        for (int i = 0; i <= 1; ++i) {
            final int j = i;
            new Thread(() -> future(client, null, timeout[j])).start();
        }
        try {
            Thread.sleep(max_timeout * 2);
        } catch (InterruptedException e) {

        }
        Assert.assertEquals(2, serverUserProcessor.getInvokeTimesEachCallType(RequestBody.InvokeType.FUTURE));
    }

    @Test
    public void testServerFuture() {
        final int timeout[] = {max_timeout / 2, max_timeout / 3};
        for (int i = 0; i <= 1; ++i) {
            final int j = i;
            new Thread(() -> future(client, server, timeout[j])).start();
        }
        try {
            Thread.sleep(max_timeout * 2);
        } catch (InterruptedException e) {

        }
        Assert.assertEquals(2, clientUserProcessor.getInvokeTimesEachCallType(RequestBody.InvokeType.FUTURE));
    }

    @Test
    public void testCallBack() {
        final int timeout[] = {max_timeout / 2, max_timeout / 3};
        for (int i = 0; i <= 1; ++i) {
            final int j = i;
            new Thread(() -> callback(client, null, timeout[j])).start();
        }
        try {
            Thread.sleep(max_timeout * 2);
        } catch (InterruptedException e) {

        }
        Assert.assertEquals(2, serverUserProcessor.getInvokeTimesEachCallType(RequestBody.InvokeType.CALLBACK));
    }

    @Test
    public void testServerCallBack() {
        final int timeout[] = {max_timeout / 2, max_timeout / 3};
        for (int i = 0; i <= 1; ++i) {
            final int j = i;
            new Thread(() -> callback(client, server, timeout[j])).start();
        }
        try {
            Thread.sleep(max_timeout * 2);
        } catch (InterruptedException e) {

        }
        Assert.assertEquals(2, clientUserProcessor.getInvokeTimesEachCallType(RequestBody.InvokeType.CALLBACK));
    }

    private void oneway(RGPDefaultRemoteClient client, RGPDefaultRemoteServer server) {
        RequestBody requestBody = new RequestBody(1, RequestBody.DEFAULT_ONEWAY_STR);
        try {
            if (null == server) {
                client.oneway(address, requestBody);
            } else {
                Assert.assertNotNull(serverConnectProcessor.getConnection());
                Connection serverConn = serverConnectProcessor.getConnection();
                server.oneway(serverConn, requestBody);
            }
            Thread.sleep(50);
        } catch (RemotingException e) {
            log.error("Exception caught in oneway!", e);
            Assert.fail("Exception caught!");
        } catch (InterruptedException e) {
            log.error("InterruptedException in oneway", e);
            Assert.fail("Should not reach here!");
        }
    }

    @SuppressWarnings("all")
    private void sync(RGPDefaultRemoteClient client, RGPDefaultRemoteServer server, int timeout) {
        RequestBody requestBody = new RequestBody(1, RequestBody.DEFAULT_SYNC_STR);
        Object result = null;
        try {
            if (null == server) {
                result = client.invokeSync(address, requestBody, timeout);
            } else {
                client.getConnection(address, timeout);
                Assert.assertNotNull(serverConnectProcessor.getConnection());
                Connection serverConnection = serverConnectProcessor.getConnection();
                result = server.invokeSync(serverConnection, requestBody, timeout);
            }
            Assert.fail("Should not reach here!");
        } catch (InvokeTimeoutException e) {
            Assert.assertNull(result);
        } catch (RemotingException e) {
            log.error("Other RemotingException but RpcServerTimeoutException occurred in sync", e);
            Assert.fail("Should not reach here!");
        } catch (InterruptedException e) {
            log.error("InterruptedException in sync", e);
            Assert.fail("Should not reach here!");
        }
    }

    @SuppressWarnings("all")
    private void future(RGPDefaultRemoteClient client, RGPDefaultRemoteServer server, int timeout) {
        RequestBody requestBody = new RequestBody(1, RequestBody.DEFAULT_FUTURE_STR);
        Object result = null;
        try {
            RPCResponseFuture future = null;
            if (null == server) {
                future = client.invokeWithFuture(address, requestBody, timeout);
            } else {
                client.getConnection(address, timeout);
                Assert.assertNotNull(serverConnectProcessor.getConnection());
                Connection serverConn = serverConnectProcessor.getConnection();
                future = server.invokeWithFuture(serverConn, requestBody, timeout);
            }
            result = future.get(timeout);
            Assert.fail("Should not reach here!");
        } catch (InvokeTimeoutException e) {
            Assert.assertNull(result);
        } catch (RemotingException e) {
            log.error("Other RemotingException but RpcServerTimeoutException occurred in sync",
                    e);
            Assert.fail("Should not reach here!");
        } catch (InterruptedException e) {
            log.error("InterruptedException in sync", e);
            Assert.fail("Should not reach here!");
        }
    }

    @SuppressWarnings("all")
    private void callback(RGPDefaultRemoteClient client, RGPDefaultRemoteServer server, int timeout) {
        RequestBody requestBody = new RequestBody(1, RequestBody.DEFAULT_CALLBACK_STR);
        final List<String> resultList = new ArrayList<String>(1);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            if (null == server) {
                client.invokeWithCallback(address, requestBody, new InvokeCallback() {
                    Executor executor = Executors.newCachedThreadPool();

                    @Override
                    public void onResponse(Object result) {
                        log.warn("Result received in callback: " + result);
                        resultList.add((String) result);
                        countDownLatch.countDown();
                    }

                    @Override
                    public void onException(Throwable e) {
                        log.error("Process exception in callback.", e);
                        resultList.add(e.getClass().getName());
                        countDownLatch.countDown();
                    }

                    @Override
                    public Executor getExecutor() {
                        return executor;
                    }
                }, timeout);
            } else {
                client.getConnection(address, timeout);
                Assert.assertNotNull(serverConnectProcessor.getConnection());
                Connection serverConn = serverConnectProcessor.getConnection();
                server.invokeWithCallback(serverConn, requestBody, new InvokeCallback() {
                    Executor executor = Executors.newCachedThreadPool();

                    @Override
                    public void onResponse(Object result) {
                        log.warn("Result received in callback: " + result);
                        resultList.add((String) result);
                        countDownLatch.countDown();
                    }

                    @Override
                    public void onException(Throwable e) {
                        log.error("Process exception in callback.", e);
                        resultList.add(e.getClass().getName());
                        countDownLatch.countDown();
                    }

                    @Override
                    public Executor getExecutor() {
                        return executor;
                    }
                }, timeout);
            }
        } catch (RemotingException e) {
            log.error("Other RemotingException but RpcServerTimeoutException occurred in sync", e);
            Assert.fail("Should not reach here!");
        } catch (InterruptedException e) {
            log.error("InterruptedException but RpcServerTimeoutException occurred in sync", e);
            Assert.fail("Should not reach here!");
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            String errorMessage = "InterruptedException caught in callback!";
            log.error(errorMessage, e);
            Assert.fail(errorMessage);
        }
        Assert.assertEquals(InvokeTimeoutException.class.getName(), resultList.get(0));
        resultList.clear();
    }
}
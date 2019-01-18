/**
 * FileName: ConfigsSupport
 * Author:   HuangTaiHong
 * Date:     2019/1/2 9:47
 * Description: Define the key for a certain config item using system property, and provide the default value for that config item.
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.group.process.netty.practice.configuration.support;

import roberto.group.process.netty.practice.serialize.serialize.manager.SerializerManager;

/**
 * 〈一句话功能简述〉<br>
 * 〈Define the key for a certain config item using system property, and provide the default value for that config item.〉
 *
 * @author HuangTaiHong
 * @create 2019/1/2
 * @since 1.0.0
 */
public class ConfigsSupport {
    /************************************************************
     *        configs and default values for bootstrap          *
     ***********************************************************/
    /** TCP_NODELAY option */
    public static final String TCP_NODELAY = "bolt.tcp.nodelay";
    /** 如果要求高实时性有数据发送时就马上发送就关闭 如果需要减少发送次数减少网络交互就开启 true表示关闭，false表示开启 **/
    public static final String TCP_NODELAY_DEFAULT = "true";

    /** TCP SO_REUSEADDR option */
    public static final String TCP_SO_REUSEADDR = "bolt.tcp.so.reuseaddr";
    /** 端口释放后立即就可以被再次使用 **/
    public static final String TCP_SO_REUSEADDR_DEFAULT = "true";

    /** TCP SO_BACKLOG option */
    public static final String TCP_SO_BACKLOG = "bolt.tcp.so.backlog";
    /** 标识当服务器请求处理线程全满时 用于临时存放已完成三次握手的请求的队列的最大长度 **/
    public static final String TCP_SO_BACKLOG_DEFAULT = "1024";

    /** TCP SO_KEEPALIVE option */
    public static final String TCP_SO_KEEPALIVE = "bolt.tcp.so.keepalive";
    /** 是否开启TCP底层心跳机制 **/
    public static final String TCP_SO_KEEPALIVE_DEFAULT = "true";

    /** Netty ioRatio option **/
    public static final String NETTY_IO_RATIO = "bolt.netty.io.ratio";
    /** Netty默认为50(即执行I/O的时间与非I/O的时间相同) **/
    public static final String NETTY_IO_RATIO_DEFAULT = "70";

    /** Netty buffer allocator, enabled as default **/
    public static final String NETTY_BUFFER_POOLED = "bolt.netty.buffer.pooled";
    public static final String NETTY_BUFFER_POOLED_DEFAULT = "true";

    /** Netty buffer low watermark **/
    public static final String NETTY_BUFFER_LOW_WATERMARK = "bolt.netty.buffer.low.watermark";
    public static final String NETTY_BUFFER_LOW_WATERMARK_DEFAULT = Integer.toString(32 * 1024);

    /** Netty buffer high watermark **/
    public static final String NETTY_BUFFER_HIGH_WATERMARK = "bolt.netty.buffer.high.watermark";
    public static final String NETTY_BUFFER_HIGH_WATERMARK_DEFAULT = Integer.toString(64 * 1024);

    /** Netty epoll switch **/
    public static final String NETTY_EPOLL_SWITCH = "bolt.netty.epoll.switch";
    public static final String NETTY_EPOLL_SWITCH_DEFAULT = "true";

    /** Netty epoll level trigger enabled */
    public static final String NETTY_EPOLL_LT = "bolt.netty.epoll.lt";
    public static final String NETTY_EPOLL_LT_DEFAULT = "true";

    /************************************************************
     *           configs and default values for idle            *
     ***********************************************************/
    /** TCP idle switch */
    public static final String TCP_IDLE_SWITCH = "bolt.tcp.heartbeat.switch";
    public static final String TCP_IDLE_SWITCH_DEFAULT = "true";

    /** TCP idle interval for client */
    public static final String TCP_CLIENT_IDLE = "bolt.tcp.heartbeat.interval";
    public static final String TCP_CLIENT_IDLE_DEFAULT = "15000";

    /** TCP idle interval for server */
    public static final String TCP_SERVER_IDLE = "bolt.tcp.server.idle.interval";
    public static final String TCP_SERVER_IDLE_DEFAULT = "90000";

    /** TCP idle triggered max times if no response */
    public static final String TCP_IDLE_MAXTIMES = "bolt.tcp.heartbeat.maxtimes";
    public static final String TCP_IDLE_MAXTIMES_DEFAULT = "3";

    /************************************************************
     *    configs and default values for connection manager     *
     ***********************************************************/
    /** Thread pool min size for the connection manager executor */
    public static final String CONN_CREATE_TP_MIN_SIZE = "bolt.conn.create.tp.min";
    public static final String CONN_CREATE_TP_MIN_SIZE_DEFAULT = "3";

    /** Thread pool max size for the connection manager executor */
    public static final String CONN_CREATE_TP_MAX_SIZE = "bolt.conn.create.tp.max";
    public static final String CONN_CREATE_TP_MAX_SIZE_DEFAULT = "8";

    /** Thread pool queue size for the connection manager executor */
    public static final String CONN_CREATE_TP_QUEUE_SIZE = "bolt.conn.create.tp.queue";
    public static final String CONN_CREATE_TP_QUEUE_SIZE_DEFAULT = "50";

    /** Thread pool keep alive time for the connection manager executor */
    public static final String CONN_CREATE_TP_KEEPALIVE_TIME = "bolt.conn.create.tp.keepalive";
    public static final String CONN_CREATE_TP_KEEPALIVE_TIME_DEFAULT = "60";

    /** Default connect timeout value, time unit: ms */
    public static final int DEFAULT_CONNECT_TIMEOUT = 1000;

    /** default connection number per url */
    public static final int DEFAULT_CONN_NUM_PER_URL = 1;

    /** max connection number of each url */
    public static final int MAX_CONN_NUM_PER_URL = 100 * 10000;

    /** Whether need to warm up the connection **/
    public static final boolean DEFAULT_CONNECTION_WARMUP = false;

    /************************************************************
     *    configs and default values for processor manager      *
     ***********************************************************/
    /** Thread pool min size for the default executor. **/
    public static final String TP_MIN_SIZE = "bolt.tp.min";
    public static final String TP_MIN_SIZE_DEFAULT = "20";

    /** Thread pool max size for the default executor. **/
    public static final String TP_MAX_SIZE = "bolt.tp.max";
    public static final String TP_MAX_SIZE_DEFAULT = "400";

    /** Thread pool queue size for the default executor. **/
    public static final String TP_QUEUE_SIZE = "bolt.tp.queue";
    public static final String TP_QUEUE_SIZE_DEFAULT = "600";

    /** Thread pool keep alive time for the default executor **/
    public static final String TP_KEEPALIVE_TIME = "bolt.tp.keepalive";
    public static final String TP_KEEPALIVE_TIME_DEFAULT = "60";

    /************************************************************
     *    configs and default values for reconnect manager      *
     ***********************************************************/
    /** 是否自动重连 **/
    public static final String CONN_RECONNECT_SWITCH = "bolt.conn.reconnect.switch";
    public static final String CONN_RECONNECT_SWITCH_DEFAULT = "false";

    /************************************************************
     *    configs and default values for connection monitor     *
     ***********************************************************/
    public static final String CONN_MONITOR_SWITCH = "bolt.conn.monitor.switch";
    public static final String CONN_MONITOR_SWITCH_DEFAULT = "false";

    /** Initial delay to execute schedule task for connection monitor */
    public static final String CONN_MONITOR_INITIAL_DELAY = "bolt.conn.monitor.initial.delay";
    public static final String CONN_MONITOR_INITIAL_DELAY_DEFAULT = "10000";

    /** Period of schedule task for connection monitor */
    public static final String CONN_MONITOR_PERIOD = "bolt.conn.monitor.period";
    public static final String CONN_MONITOR_PERIOD_DEFAULT = "180000";

    /** Connection threshold */
    public static final String CONN_THRESHOLD = "bolt.conn.threshold";
    public static final String CONN_THRESHOLD_DEFAULT = "3";

    /** Retry detect period for ScheduledDisconnectStrategy */
    public static final String RETRY_DETECT_PERIOD = "bolt.retry.delete.period";
    public static final String RETRY_DETECT_PERIOD_DEFAULT = "5000";

    public static final String CONN_SERVICE_STATUS = "bolt.conn.service.status";
    public static final String CONN_SERVICE_STATUS_OFF = "off";
    public static final String CONN_SERVICE_STATUS_ON = "on";

    /************************************************************
     *       configs and default values for serializer          *
     ***********************************************************/
    public static final String SERIALIZER = "bolt.serializer";
    public static final String SERIALIZER_DEFAULT = String.valueOf(SerializerManager.HESSIAN2);

    /** 默认字符编码 */
    public static final String DEFAULT_CHARSET = "UTF-8";
}
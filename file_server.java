//package file_severe;
//
//import org.jboss.netty.channel.ChannelHandlerContext;
//import org.jboss.netty.channel.MessageEvent;
//
//public class file_server {
//
//	private static ChannelHandlerContext ctx;
//	private static MessageEvent e;
//
//	public static void main(String[] args) throws Exception {
//		FileServerHandler Severve = new FileServerHandler();
//    	Severve.messageReceived(ctx, e);
//	}
//
//}

package file_severe;  
  
import static org.jboss.netty.channel.Channels.pipeline;  
  
import java.net.InetSocketAddress;  
import java.util.concurrent.Executors;  
  
import org.jboss.netty.bootstrap.ServerBootstrap;  
import org.jboss.netty.channel.ChannelPipeline;  
import org.jboss.netty.channel.ChannelPipelineFactory;  
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;  
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;  
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;  
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;  
import org.jboss.netty.handler.stream.ChunkedWriteHandler;  
  
public class file_server  
{  
    public static void main(String[] args)  
    {  
//      severe服务启动器
        ServerBootstrap bootstrap = new ServerBootstrap(  
                new NioServerSocketChannelFactory(  
                        Executors.newCachedThreadPool(),  
                        Executors.newCachedThreadPool()));  
//        设置一个处理客户端消息和各种消息事件的类（handle）
        bootstrap.setPipelineFactory(new ChannelPipelineFactory()  
        {  
  
            @Override  
            public ChannelPipeline getPipeline() throws Exception  
            {  
                ChannelPipeline pipeline = pipeline();  
                pipeline.addLast("decoder", new HttpRequestDecoder());  
                pipeline.addLast("aggregator", new HttpChunkAggregator(65536));  
                pipeline.addLast("encoder", new HttpResponseEncoder());  
                pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());  
  
                pipeline.addLast("handler", new FileServerHandler());  
                return pipeline;  
            }  
  
        });  
        //开放端口供客户端访问
        bootstrap.bind(new InetSocketAddress(9000));  
    }  
}  
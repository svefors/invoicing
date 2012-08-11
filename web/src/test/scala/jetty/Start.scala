package jetty

import org.mortbay.jetty.bio.SocketConnector
import org.mortbay.jetty.webapp.WebAppContext
import org.mortbay.jetty.{Connector, Server}

/**
 * Created by IntelliJ IDEA.
 * User: sveffa
 * Date: 7/31/12
 * Time: 6:38 PM
 * To change this template use File | Settings | File Templates.
 */

object Start extends App {
  var server: Server = new Server
  var connector: SocketConnector = new SocketConnector
  // Set some timeout options to make debugging easier.


  connector.setMaxIdleTime(1000 * 60 * 60)


  connector.setSoLingerTime(-1)


  connector.setPort(8081)


  server.setConnectors(Array[Connector](connector))
  var bb: WebAppContext = new WebAppContext
  //      bb.setServer(server);


  bb.setContextPath("/")


  bb.setWar("web/src/main/webapp")



  //




  System.out.println(bb.getWebInf.toString)



  // START JMX SERVER
  // MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
  // MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
  // server.getContainer().addEventListener(mBeanContainer);
  // mBeanContainer.start();



  server.addHandler(bb)



  try {
    System.out.println(">>> STARTING EMBEDDED JETTY SERVER, PRESS ANY KEY TO STOP")
    server.start
    while (System.in.available == 0) {
      Thread.sleep(5000)
    }
    server.stop
    server.join
  }
  catch {
    case e: Exception => {
      e.printStackTrace
      System.exit(100)
    }
  }
}